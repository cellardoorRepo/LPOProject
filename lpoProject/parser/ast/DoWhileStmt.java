package lpoProject.parser.ast;

import lpoProject.visitors.Visitor;

import static java.util.Objects.requireNonNull;

public class DoWhileStmt implements Stmt {

    private StmtSeq block;
    private Exp guard;

    public DoWhileStmt(StmtSeq block, Exp guard) {
        this.block = requireNonNull(block);
        this.guard = requireNonNull(guard);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + block + "," + guard + ")";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitDoWhileStmt(block, guard);
    }
}
