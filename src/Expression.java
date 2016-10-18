import java.util.Objects;

abstract class Expression {
    private Expression left;
    private Expression right;

    Expression() {
        left = null;
        right = null;
    }

    Expression(Expression left) {
        this.left = left;
        right = null;
    }

    Expression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    final public boolean equals(Object obj) {
        if (nodeEquals(obj)) {
            Expression expression = (Expression) obj;
            return Objects.equals(left, expression.left) && Objects.equals(right, expression.right);
        } else {
            return false;
        }
    }

    boolean nodeEquals(Object expression) {
        return (this.getClass().isInstance(expression));
    }

    @Override
    final public String toString() {
        return "\n" + toStringTree(0);
    }

    private String toStringTree(int level) {
        String tabs = "";
        for (int i = 0; i < level; i++) {
            tabs += "   ";
        }
        if (left == null && right == null) {
            return tabs + nodeToString() + '\n';
        } else {
            return tabs + nodeToString() + "{\n" + (left == null ? "" : left.toStringTree(level + 1)) + (right == null ? "" : right.toStringTree(level + 1)) + tabs + "}\n";
        }
    }

    protected String nodeToString() {
        return this.getClass().getName();
    }
}
