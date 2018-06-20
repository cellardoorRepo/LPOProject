package lpoProject.visitors.evaluation;

public class EmptyOpt implements Value {

    public EmptyOpt() {

    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof EmptyOpt))
            return false;
        return true;
    }

    public String toString() {
        return "opt empty";
    }
}
