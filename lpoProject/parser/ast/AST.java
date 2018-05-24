package lpoProject.parser.ast;

import lpoProject.visitors.Visitor;

public interface AST {
	<T> T accept(Visitor<T> visitor);
}
