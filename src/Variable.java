public class Variable extends Expression {
    private final String name;

    Variable(String name) {
        super();
        this.name = name;
    }

    String getName() {
        return name;
    }

    @Override
    protected boolean nodeEquals(Object expression) {
        if (super.nodeEquals(expression)) {
            return name.equals(((Variable) expression).name);
        } else {
            return false;
        }
    }

    @Override
    protected String nodeToString() {
        return "Variable: " + name;
    }
}
