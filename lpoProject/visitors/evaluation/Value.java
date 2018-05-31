package lpoProject.visitors.evaluation;

public interface Value {
	/* default conversion methods */
	default int asInt() {
		throw new ClassCastException("Expecting an integer value");
	}

	default String asString() {
		throw new ClassCastException("Expecting a string value");
	}

	default ListValue asList() {
		throw new ClassCastException("Expecting a list value");
	}

	default boolean asBool() {
		throw new ClassCastException("Expecting a boolean value");
	}

}
