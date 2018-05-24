package lpoProject.parser.ast;

import lpoProject.visitors.Visitor;

public class Prefix extends BinaryOp {

	public Prefix(Exp left, Exp right) {
		super(left, right);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitPrefix(left, right);
	}
}
