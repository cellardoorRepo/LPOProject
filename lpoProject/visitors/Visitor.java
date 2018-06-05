package lpoProject.visitors;

import lpoProject.parser.ast.*;
import lpoProject.visitors.evaluation.PrimValue;

import java.util.Optional;

public interface Visitor<T> {
	T visitAdd(Exp left, Exp right);

	T visitAssignStmt(Ident ident, Exp exp);

	T visitForEachStmt(Ident ident, Exp exp, StmtSeq block);

	T visitIfStmt(Exp guard, StmtSeq block, StmtSeq elseBlock);

	T visitDoWhileStmt(StmtSeq block, Exp guard);

	T visitIntLiteral(int value);

	T visitBoolLiteral(boolean value);

	T visitOptLiteral(Exp value);

	//T visitIntOptLiteral(Integer value);

	T visitListLiteral(ExpSeq exps);

	T visitMoreExp(Exp first, ExpSeq rest);

	T visitMoreStmt(Stmt first, StmtSeq rest);

	T visitMul(Exp left, Exp right);

    T visitNot(Exp exp);

    T visitAnd(Exp left, Exp right);

    T visitEq(Exp left, Exp right);

	T visitPrefix(Exp left, Exp right);

	T visitPrintStmt(Exp exp);

	T visitProg(StmtSeq stmtSeq);

	T visitSign(Exp exp);

	T visitIdent(String name);

	T visitSingleExp(Exp exp);

	T visitSingleStmt(Stmt stmt);

	T visitVarStmt(Ident ident, Exp exp);
}
