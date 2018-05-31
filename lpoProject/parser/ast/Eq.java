package lpoProject.parser.ast;

import lpoProject.visitors.Visitor;

public class Eq extends BinaryOp {
    public Eq(Exp left, Exp right) {
        super(left, right);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitEq(left, right);
    }
}
