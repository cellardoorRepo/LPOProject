package lpoProject;

import static java.lang.System.err;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lpoProject.parser.*;
import lpoProject.parser.StreamTokenizer;
import lpoProject.parser.ast.Prog;
import lpoProject.visitors.evaluation.Eval;
import lpoProject.visitors.evaluation.EvaluatorException;
import lpoProject.visitors.typechecking.TypeCheck;
import lpoProject.visitors.typechecking.TypecheckerException;

public class Main {

	private static enum Option {
		INPUT("-i"), OUTPUT("-o");
		private final String lexem;

		private Option(String lexem) {
			this.lexem = lexem;
		}
	}

	private static final Map<String, Option> optionLexems = new HashMap<>();
	static {
		for (Option opt : Option.values())
			optionLexems.put(opt.lexem, opt);
	}

	private static final Map<Option, String> optionValues = new HashMap<>();
	static {
		for (Option opt : Option.values())
			optionValues.put(opt, null);
	}

	private static void argError() {
		System.err.println("Illegal argument");
		System.exit(1);
	}

	private static String readNext(Iterator<String> it) {
		if (!it.hasNext())
			argError();
		return it.next();
	}

	private static void processArgs(String[] args) {
		Iterator<String> it = Arrays.asList(args).iterator();
		while (it.hasNext()) {
			String curr = it.next();
			if (!optionLexems.containsKey(curr))
				argError();
			optionValues.put(optionLexems.get(curr), readNext(it));
		}
	}

	private static FileReader tryOpenInput(String inputPath) throws FileNotFoundException {
		return new FileReader(inputPath);
	}

	private static void tryOpenOutput(String outputPath) throws FileNotFoundException {
		System.setOut(new PrintStream(new FileOutputStream(outputPath, false)));
	}



	public static void main(String[] args) {
		FileReader fd = null;
		if(args.length > 0) {
			processArgs(args);
			try {
				fd = tryOpenInput(optionValues.get(Option.INPUT));
				tryOpenOutput(optionValues.get(Option.OUTPUT));
			} catch (IOException e) {
				err.println("I/O error: " + e.getMessage());
			}
		}
		try (Tokenizer tokenizer = new StreamTokenizer(
				fd != null ? fd : new InputStreamReader(System.in))) {
			Parser parser = new StreamParser(tokenizer);
			Prog prog = parser.parseProg();
			prog.accept(new TypeCheck());
			prog.accept(new Eval());
		} catch (ParserException e) {
			err.println("Syntax error: " + e.getMessage());
		} catch (TypecheckerException e) {
			err.println("Static error: " + e.getMessage());
		} catch (EvaluatorException e) {
			err.println("Dynamic error: " + e.getMessage());
		} catch (Throwable e) {
			err.println("Unexpected error.");
			e.printStackTrace();
		}
	}
}