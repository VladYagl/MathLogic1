import java.lang.reflect.InvocationTargetException;

public class ExpressionParser {
    private static final String functions[] = {"->", "|", "&"};
    private static final Class functionsClass[] = {Implication.class, Disjunction.class, Conjunction.class};

    static private Expression parseToken(String token) {
        if (token.charAt(0) == '!') {
            return new Negation(parseToken(token.substring(1)));
        } else if (token.charAt(0) == '(') {
            return parse(token.substring(1, token.length() - 1));
        } else {
            return new Variable(token);
        }
    }

    static private Expression parseFunction(String text, int functionLevel) {
        if (functionLevel > 2) {
            return parseToken(text);
        }
        int position = -1;
        int balance = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '(') balance++;
            if (c == ')') balance--;
            if (balance == 0 && c == functions[functionLevel].charAt(0)) {
                position = i;
                if (functionLevel == 0) { // For implication.
                    break;
                }
            }
        }
        if (position == -1) {
            return parseFunction(text, functionLevel + 1);
        } else {
            try {
                //noinspection unchecked
                return (Expression) functionsClass[functionLevel].getConstructor(Expression.class, Expression.class)
                        .newInstance(
                                parseFunction(text.substring(0, position), functionLevel + 1),
                                parseFunction(text.substring(position + functions[functionLevel].length()), functionLevel));
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    static Expression parse(String text) {
        text = text.replaceAll(" |\t|\r", "");
        return parseFunction(text, 0);
    }
}
