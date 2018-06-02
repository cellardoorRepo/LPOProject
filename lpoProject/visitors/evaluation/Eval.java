package lpoProject.visitors.evaluation;

import lpoProject.environments.EnvironmentException;
import lpoProject.environments.GenEnvironment;
import lpoProject.parser.ast.Exp;
import lpoProject.parser.ast.ExpSeq;
import lpoProject.parser.ast.Ident;
import lpoProject.parser.ast.SimpleIdent;
import lpoProject.parser.ast.Stmt;
import lpoProject.parser.ast.StmtSeq;
import lpoProject.visitors.Visitor;

public class Eval implements Visitor<Value> {

	private final GenEnvironment<Value> env = new GenEnvironment<>();

	// dynamic semantics for programs; no value returned by the visitor

	@Override
	public Value visitProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
		} catch (EnvironmentException e) { // undefined variable
			throw new EvaluatorException(e);
		}
		return null;
	}

	// dynamic semantics for statements; no value returned by the visitor

	@Override
	public Value visitAssignStmt(Ident ident, Exp exp) {
		env.update(ident, exp.accept(this));
		return null;
	}

	@Override
	public Value visitForEachStmt(Ident ident, Exp exp, StmtSeq block) {
		ListValue lv = exp.accept(this).asList();
		for(Value v : lv) {
			env.enterLevel();
			env.dec(ident, v);
			block.accept(this);
			env.exitLevel();
		}
		return null;
	}

	@Override
	public Value visitIfStmt(Exp guard, StmtSeq block, StmtSeq elseBlock) {
		if(guard.accept(this).asBool()) {
			env.enterLevel();
			block.accept(this);
			env.exitLevel();
			return null;
		}
		else if(elseBlock != null) {
			env.enterLevel();
			elseBlock.accept(this);
			env.exitLevel();
			return null;
		}
		else
			return null;
	}

	@Override
	public Value visitDoWhileStmt(StmtSeq block, Exp guard) {
		do {
			env.enterLevel();
			block.accept(this);
			env.exitLevel();
		} while (guard.accept(this).asBool());
		return null;
	}

	@Override
	public Value visitPrintStmt(Exp exp) {
		System.out.println(exp.accept(this));
		return null;
	}

	@Override
	public Value visitVarStmt(Ident ident, Exp exp) {
		env.dec(ident, exp.accept(this));
		return null;
	}

	// dynamic semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Value visitSingleStmt(Stmt stmt) {
		stmt.accept(this);
		return null;
	}

	@Override
	public Value visitMoreStmt(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}

	// dynamic semantics of expressions; a value is returned by the visitor

	@Override
	public Value visitAdd(Exp left, Exp right) {
		return new IntValue(left.accept(this).asInt() + right.accept(this).asInt());
	}

	@Override
	public Value visitIntLiteral(int value) {
		return new IntValue(value);
	}

	@Override
	public Value visitBoolLiteral(boolean value) {
		return new BoolValue(value);
	}

	@Override
	public Value visitListLiteral(ExpSeq exps) {
		return exps.accept(this);
	}

	@Override
	public Value visitMul(Exp left, Exp right) {
		return new IntValue(left.accept(this).asInt() * right.accept(this).asInt());
	}

	@Override
	public Value visitNot(Exp exp) {
		return new BoolValue(! exp.accept(this).asBool());
	}

	@Override
	public Value visitAnd(Exp left, Exp right) {
		return new BoolValue(left.accept(this).asBool() && right.accept(this).asBool());
	}

	@Override
	public Value visitEq(Exp left, Exp right) {
		return new BoolValue(left.accept(this).equals(right.accept(this)));
	}

	@Override
	public Value visitPrefix(Exp left, Exp right) {
		Value el = left.accept(this);
		return right.accept(this).asList().prefix(el);
	}

	@Override
	public Value visitSign(Exp exp) {
		return new IntValue(-exp.accept(this).asInt());
	}

	@Override
	public Value visitIdent(String name) {
		return env.lookup(new SimpleIdent(name));
	}

	// dynamic semantics of sequences of expressions
	// a list of values is returned by the visitor

	@Override
	public Value visitSingleExp(Exp exp) {
		return new LinkedListValue(exp.accept(this), new LinkedListValue());
	}

	@Override
	public Value visitMoreExp(Exp first, ExpSeq rest) {
		return new LinkedListValue(first.accept(this), rest.accept(this).asList());
	}

}
