package lpoProject.visitors.evaluation;

import static java.util.Objects.requireNonNull;

public abstract class PrimValue<T> implements Value {
	protected T value;

	protected PrimValue(T value) {
		this.value = requireNonNull(value);
	}

	public T getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toString() {
		return value.toString();
	}
}