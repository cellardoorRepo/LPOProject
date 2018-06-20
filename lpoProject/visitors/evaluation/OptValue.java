package lpoProject.visitors.evaluation;


public class OptValue implements Value {

    private Value value;

    public OptValue(Value value) {
        this.value = value;
    }


    public Value asValue() {
        return value;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof OptValue))
            return false;
        return value.equals(((OptValue) obj).value);
    }


    public String toString() {
        return "opt" + " " + value;
    }
}
