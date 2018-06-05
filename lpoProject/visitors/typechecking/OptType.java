package lpoProject.visitors.typechecking;


public class OptType implements Type {

    private Type elemType;

    public static final String TYPE_NAME = "opt";

    public OptType(Type elemType) {
        if(elemType == null)
            this.elemType =
        this.elemType = elemType;
    }

    @Override
    public String toString() {
        return elemType + " " + TYPE_NAME;
    }
}
