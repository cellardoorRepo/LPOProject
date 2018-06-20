package lpoProject.parser;

import static lpoProject.parser.TokenType.*;

import lpoProject.parser.ast.*;
import lpoProject.visitors.typechecking.PrimtType;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/*
Prog ::= StmtSeq 'EOF'
 StmtSeq ::= Stmt (';' StmtSeq)?
 Stmt ::= 'var'? ID '=' Exp | 'print' Exp |  'for' ID ':' Exp '{' StmtSeq '}'
 ExpSeq ::= Exp (',' ExpSeq)?
 Exp ::= Add ('::' Exp)? | Exp&&Exp | Exp==Exp | !Exp | opt Exp | empty Exp | def Exp | get Exp
 Add ::= Mul ('+' Mul)*
 Mul::= Atom ('*' Atom)*
 Atom ::= '-' Atom | '[' ExpSeq ']' | NUM | ID | '(' Exp ')'

*/

public class StreamParser implements Parser {

	private final Tokenizer tokenizer;

	private void tryNext() throws ParserException {
		try {
			tokenizer.next();
		} catch (TokenizerException e) {
			throw new ParserException(e);
		}
	}

	private void match(TokenType expected) throws ParserException {
		final TokenType found = tokenizer.tokenType();
		if (found != expected)
			throw new ParserException(
					"Expecting " + expected + ", found " + found + "('" + tokenizer.tokenString() + "')");
	}

	private void consume(TokenType expected) throws ParserException {
		match(expected);
		tryNext();
	}

	private void unexpectedTokenError() throws ParserException {
		throw new ParserException("Unexpected token " + tokenizer.tokenType() + "('" + tokenizer.tokenString() + "')");
	}

	public StreamParser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	@Override
	public Prog parseProg() throws ParserException {
		tryNext(); // one look-ahead symbol
		Prog prog = new ProgClass(parseStmtSeq());
		match(EOF);
		return prog;
	}

	private StmtSeq parseStmtSeq() throws ParserException {
		Stmt stmt = parseStmt();
		if (tokenizer.tokenType() == STMT_SEP) {
			tryNext();
			return new MoreStmt(stmt, parseStmtSeq());
		}
		return new SingleStmt(stmt);
	}

	private ExpSeq parseExpSeq() throws ParserException {
		Exp exp = parseAnd();
		if (tokenizer.tokenType() == EXP_SEP) {
			tryNext();
			return new MoreExp(exp, parseExpSeq());
		}
		return new SingleExp(exp);
	}

	private Stmt parseStmt() throws ParserException {
		switch (tokenizer.tokenType()) {
		default:
			unexpectedTokenError();
		case PRINT:
			return parsePrintStmt();
		case VAR:
			return parseVarStmt();
		case IDENT:
			return parseAssignStmt();
		case FOR:
			return parseForEachStmt();
			case IF:
				return parseIfStmt();
			case DO:
				return parseDoWhileStmt();
		}
	}

	private PrintStmt parsePrintStmt() throws ParserException {
		consume(PRINT); // or tryNext();
		return new PrintStmt(parseAnd());
	}

	private VarStmt parseVarStmt() throws ParserException {
		consume(VAR); // or tryNext();
		Ident ident = parseIdent();
		consume(ASSIGN);
		return new VarStmt(ident, parseAnd());
	}

	private AssignStmt parseAssignStmt() throws ParserException {
		Ident ident = parseIdent();
		consume(ASSIGN);
		return new AssignStmt(ident, parseAnd());
	}

	private ForEachStmt parseForEachStmt() throws ParserException {
		consume(FOR); // or tryNext();
		Ident ident = parseIdent();
		consume(IN);
		Exp exp = parseAnd();
		consume(OPEN_BLOCK);
		StmtSeq stmts = parseStmtSeq();
		consume(CLOSE_BLOCK);
		return new ForEachStmt(ident, exp, stmts);
	}

	private IfStmt parseIfStmt() throws ParserException {
		consume(IF);
//		consume(OPEN_PAR);
//		BoolLiteral guard = parseBool();
//		consume(CLOSE_PAR);
		Exp guard = parseRoundPar();
		consume(OPEN_BLOCK);
		StmtSeq block = parseStmtSeq();
		consume(CLOSE_BLOCK);
		if(tokenizer.tokenType() == ELSE) {
			consume(ELSE);
			consume(OPEN_BLOCK);
			StmtSeq elseBlock = parseStmtSeq();
			consume(CLOSE_BLOCK);
			return new IfStmt(guard, block, elseBlock);
		}
		return new IfStmt(guard, block, null);
	}

	private DoWhileStmt parseDoWhileStmt() throws ParserException {
		consume(DO);
		consume(OPEN_BLOCK);
		StmtSeq block = parseStmtSeq();
		consume(CLOSE_BLOCK);
		consume(WHILE);
		Exp guard = parseRoundPar();
		return new DoWhileStmt(block, guard);
	}

	private Exp parseExp() throws ParserException {
		Exp exp = parseAdd();
		if (tokenizer.tokenType() == PREFIX) {
			tryNext();
			exp = new Prefix(exp, parseAdd());
		}
		return exp;
	}


	private Exp parseAnd() throws ParserException {
		Exp exp = parseEq();
		while(tokenizer.tokenType() == AND) {
			tryNext();
			exp = new And(exp, parseEq());
		}
		return exp;
	}

	private Exp parseEq() throws ParserException {
		Exp exp	= parseExp();
		while (tokenizer.tokenType() == EQ) {
			tryNext();
			exp = new Eq(exp, parseExp());
		}
		return exp;
	}

	private Exp parseAdd() throws ParserException {
		Exp exp = parseMul();
		while (tokenizer.tokenType() == PLUS) {
			tryNext();
			exp = new Add(exp, parseMul());
		}
		return exp;
	}

	private Exp parseMul() throws ParserException {
		Exp exp = parseAtom();
		while (tokenizer.tokenType() == TIMES) {
			tryNext();
			exp = new Mul(exp, parseAtom());
		}
		return exp;
	}

	private Exp parseAtom() throws ParserException {
		switch (tokenizer.tokenType()) {
		default:
			unexpectedTokenError();
		case NUM:
			return parseNum();
            case BOOL:
                return parseBool();
		case IDENT:
			return parseIdent();
			case NOT:
				return parseNot();
		case MINUS:
			return parseMinus();
		case OPEN_LIST:
			return parseList();
		case OPEN_PAR:
			return parseRoundPar();
			case OPT:
				return parseOpt();
			case GET:
				return parseGet();
			case EMPTY:
				return parseEmpty();
			case DEF:
				return parseDef();
		}
	}

	private IntLiteral parseNum() throws ParserException {
		int val = tokenizer.intValue();
		consume(NUM); // or tryNext();
		return new IntLiteral(val);
	}

	private BoolLiteral parseBool() throws ParserException {
	    boolean val = tokenizer.boolValue();
	    consume(BOOL);
	    return new BoolLiteral(val);
    }

	private Ident parseIdent() throws ParserException {
		String name = tokenizer.tokenString();
		consume(IDENT); // or tryNext();
		return new SimpleIdent(name);
	}

	private OptLiteral parseOpt() throws ParserException {
		consume(OPT);
		return new OptLiteral(parseExp());
	}

	private Get parseGet() throws ParserException {
		consume(GET);
		if(tokenizer.tokenType() == IDENT)
			return new Get(parseIdent());
		return new Get(parseExp());
	}

	private Empty parseEmpty() throws ParserException {
		consume(EMPTY);
		if(tokenizer.tokenType() == IDENT)
			return new Empty(parseIdent());
		return new Empty(parseExp());
	}

	private Def parseDef() throws ParserException {
		consume(DEF);
		return new Def(parseExp());
	}

	private Not parseNot() throws ParserException {
		consume(NOT);
		return new Not(parseAtom());
	}

	private Sign parseMinus() throws ParserException {
		consume(MINUS); // or tryNext();
		return new Sign(parseAtom());
	}

	private ListLiteral parseList() throws ParserException {
		consume(OPEN_LIST); // or tryNext();
		ExpSeq exps = parseExpSeq();
		consume(CLOSE_LIST);
		return new ListLiteral(exps);
	}

	private Exp parseRoundPar() throws ParserException {
		consume(OPEN_PAR); // or tryNext();
		Exp exp = parseAnd();
		consume(CLOSE_PAR);
		return exp;
	}

}
