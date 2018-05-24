package lpoProject.visitors.evaluation;

public interface ListValue extends Value, Iterable<Value> {
	ListValue prefix(Value el);

	@Override
	default ListValue asList() {
		return this;
	}
}
