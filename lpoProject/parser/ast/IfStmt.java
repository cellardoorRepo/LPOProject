package lpoProject.parser.ast;

import lpoProject.visitors.Visitor;

import static java.util.Objects.requireNonNull;

public class IfStmt implements Stmt {

    private Exp guard;
    private StmtSeq block;
    private StmtSeq elseBlock;

    public IfStmt(Exp guard, StmtSeq block, StmtSeq elseBlock) {
        this.guard = requireNonNull(guard);
        this.block = requireNonNull(block);
        this.elseBlock = elseBlock;
    }

    public Exp getGuard() {
        return guard;
    }

    public StmtSeq getBlock() {
        return block;
    }

    public StmtSeq getElseBlock() {
        return elseBlock;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + guard + "," + block + "," + elseBlock + ")";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitIfStmt(guard, block, elseBlock);
    }
}
