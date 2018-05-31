package lpoProject.visitors.typechecking;

import static lpoProject.visitors.typechecking.PrimtType.*;

import lpoProject.environments.EnvironmentException;
import lpoProject.environments.GenEnvironment;
import lpoProject.parser.ast.Exp;
import lpoProject.parser.ast.ExpSeq;
import lpoProject.parser.ast.Ident;
import lpoProject.parser.ast.SimpleIdent;
import lpoProject.parser.ast.Stmt;
import lpoProject.parser.ast.StmtSeq;
import lpoProject.visitors.Visitor;

public class TypeCheck implements Visitor<Type> {

	private final GenEnvironment<Type> env = new GenEnvironment<>();

	private void checkBinOp(Exp left, Exp right, Type type) {
		left.accept(this).checkEqual(type);
		right.accept(this).checkEqual(type);
	}

	// static semantics for programs; no value returned by the visitor

	@Override
	public Type visitProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
		} catch (EnvironmentException e) { // undefined variable
			throw new TypecheckerException(e);
		}
		return null;
	}

	// static semantics for statements; no value returned by the visitor

	@Override
	public Type visitAssignStmt(Ident ident, Exp exp) {
		Type t = env.lookup(ident);
		exp.accept(this).checkEqual(t);
		return null;
	}

	@Override
	public Type visitPrintStmt(Exp exp) {
		exp.accept(this);
		return null;
	}

	@Override
	public Type visitForEachStmt(Ident ident, Exp exp, StmtSeq block) {
		Type ty = exp.accept(this).getListElemType();
		env.enterLevel();
		env.dec(ident, ty);
		block.accept(this);
		env.exitLevel();
		return null;
	}

	@Override
	public Type visitVarStmt(Ident ident, Exp exp) {
		env.dec(ident, exp.accept(this));
		return null;
	}

	// static semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Type visitSingleStmt(Stmt stmt) {
		stmt.accept(this);
		return null;
	}

	@Override
	public Type visitMoreStmt(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}

	// static semantics of expressions; a type is returned by the visitor

	@Override
	public Type visitAdd(Exp left, Exp right) {
		checkBinOp(left, right, INT);
		return INT;
	}

	@Override
	public Type visitIntLiteral(int value) {
		return INT;
	}

	@Override
    public Type visitBoolLiteral(boolean value) {
	    return BOOL;
    }

	@Override
	public Type visitListLiteral(ExpSeq exps) {
		return new ListType(exps.accept(this));
	}

	@Override
	public Type visitMul(Exp left, Exp right) {
		checkBinOp(left, right, INT);
		return INT;
	}

	@Override
    public Type visitNot(Exp exp) {
	    return exp.accept(this).checkEqual(BOOL);
    }

    @Override
    public Type visitAnd(Exp left, Exp right) {
//	    Type expected = left.accept(this);
//	    right.accept(this).checkEqual(expected);
		checkBinOp(left, right, BOOL);
	    return BOOL;
    }

    @Override
    public Type visitEq(Exp left, Exp right) {
	    Type expected  = left.accept(this);
	    expected.checkEqual(right.accept(this));
		//right.accept(this).checkEqual(expected);
	    return BOOL;
    }

	@Override
	public Type visitPrefix(Exp left, Exp right) {
		Type elemType = left.accept(this);
		return new ListType(elemType).checkEqual(right.accept(this));
	}

	@Override
	public Type visitSign(Exp exp) {
		return INT.checkEqual(exp.accept(this));
	    //exp.accept(this).checkEqual(INT);
		//return INT;
	}

	@Override
	public Type visitIdent(String name) {
		return env.lookup(new SimpleIdent(name));
	}

	// static semantics of sequences of expressions
	// a type is returned by the visitor

	@Override
	public Type visitSingleExp(Exp exp) {
	    return exp.accept(this);
	}

	@Override
	public Type visitMoreExp(Exp first, ExpSeq rest) {
		Type check = first.accept(this);
		return rest.accept(this).checkEqual(check);
	}

}
