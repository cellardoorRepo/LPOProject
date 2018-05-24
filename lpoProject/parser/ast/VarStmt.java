package lpoProject.parser.ast;

import lpoProject.visitors.Visitor;

public class VarStmt extends AbstractAssignStmt {

	public VarStmt(Ident ident, Exp exp) {
		super(ident, exp);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitVarStmt(ident, exp);
	}
}
