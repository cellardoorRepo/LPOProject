package lpoProject.parser.ast;


import lpoProject.visitors.Visitor;


public class OptLiteral implements Exp {

   // private String type;
    protected Exp value;

    public OptLiteral(){
      //  this.value = null;
    }

    public OptLiteral(Exp n) {
        this.value = n;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + value + ")";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitOptLiteral(value);
    }
}
