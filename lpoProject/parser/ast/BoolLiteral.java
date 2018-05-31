package lpoProject.parser.ast;

import lpoProject.visitors.Visitor;

public class BoolLiteral extends PrimLiteral<Boolean> {

    public BoolLiteral(boolean v) {
        super(v);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitBoolLiteral(value);
    }
}
