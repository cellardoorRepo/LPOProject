package lpoProject.parser.ast;

import lpoProject.visitors.Visitor;

public class Def extends UnaryOp {

    public Def(Exp exp) {
        super(exp);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitDef(exp);
    }
}
