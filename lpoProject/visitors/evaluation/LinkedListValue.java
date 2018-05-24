package lpoProject.visitors.evaluation;

import java.util.Iterator;
import java.util.LinkedList;
import static java.util.Objects.requireNonNull;

public class LinkedListValue implements ListValue {

	private final LinkedList<Value> list = new LinkedList<>();

	public LinkedListValue() {
	}

	public LinkedListValue(ListValue otherList) {
		for (Value el : otherList)
			list.add(el);
	}

	public LinkedListValue(Value val, ListValue tail) {
		this(tail);
		list.addFirst(requireNonNull(val));
	}

	@Override
	public Iterator<Value> iterator() {
		return list.iterator();
	}

	@Override
	public ListValue prefix(Value el) {
		LinkedListValue res = new LinkedListValue(this);
		res.list.addFirst(requireNonNull(el));
		return res;
	}

	@Override
	public String toString() {
		return list.toString();
	}

	@Override
	public int hashCode() {
		return list.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return list.equals(((LinkedListValue) obj).list);
	}
}
