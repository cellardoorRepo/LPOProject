package lpoProject.visitors.evaluation;


public class OptValue implements Value {

    private String type;
    protected Value value;

    public OptValue(Value value) {
        if(value instanceof OptValue)
            this.type = "opt";
        else if(value instanceof IntValue)
            this.type = "int opt";
        else if (value instanceof BoolValue)
            this.type = "bool opt";
        else if (value instanceof ListValue)
            this.type = "list opt";
        this.value = value;
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
        return type + " " + value;
    }
}
