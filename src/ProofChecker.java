import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

class ProofChecker {
    private Expression axioms[] = {
            ExpressionParser.parse("A->B->A"),
            ExpressionParser.parse("(A->B)->(A->B->C)->(A->C)"),
            ExpressionParser.parse("A->B->A&B"),
            ExpressionParser.parse("A&B->A"),
            ExpressionParser.parse("A&B->B"),
            ExpressionParser.parse("A->A|B"),
            ExpressionParser.parse("B->A|B"),
            ExpressionParser.parse("(A->C)->(B->C)->(A|B->C)"),
            ExpressionParser.parse("(A->B)->(A->!B)->!A"),
            ExpressionParser.parse("!!A->A")
    };

    private final HashMap<String, Integer> suppositions = new HashMap<>();
    private ArrayList<Expression> proof;

    private final HashMap<String, ArrayList<Integer>> RightExpressions = new HashMap<>();
    private final HashMap<String, Integer> proofed = new HashMap<>();

    private final HashMap<String, Expression> variables = new HashMap<>();

    private final ArrayList<String> annotations = new ArrayList<>();

    ProofChecker(ArrayList<Expression> suppositions, ArrayList<Expression> proof) {
        int i = 0;
        for (Expression e : suppositions) {
            this.suppositions.put(e.toString(), i++);
        }
        this.proof = proof;
        /*
        System.out.println(proof);
        System.out.println("\n");
        System.out.println(Arrays.toString(axioms));
        */
    }

    private boolean matchVariables(Expression axiom, Expression expression) {
        if (axiom instanceof Variable) {
            if (variables.containsKey(((Variable) axiom).getName())) {
                return variables.get(((Variable) axiom).getName()).equals(expression);
            } else {
                variables.put(((Variable) axiom).getName(), expression);
                return true;
            }
        } else
            return !(expression instanceof Variable) &&
                    axiom.nodeEquals(expression) &&
                    matchVariables(axiom.getLeft(), expression.getLeft()) &&
                    (axiom instanceof Negation || matchVariables(axiom.getRight(), expression.getRight()));
    }

    private int isAxiom(Expression expression) {
        int i = 0;
        for (Expression e : axioms) {
            variables.clear();
            if (matchVariables(e, expression)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void addToProofed(Expression expression, Integer number) {
        proofed.put(expression.toString(), number);
        if (expression instanceof Implication) {
            String key = expression.getRight().toString();
            if (!RightExpressions.containsKey(key)) {
                RightExpressions.put(key, new ArrayList<>());
            }
            RightExpressions.get(key).add(number);
        }
    }

    private Pair<Integer, Integer> ModusPonens(Expression expression) {
        if (RightExpressions.containsKey(expression.toString())) {
            ArrayList<Integer> variants = RightExpressions.get(expression.toString());
            for (Integer pos : variants) {
                Expression left = proof.get(pos).getLeft();
                if (proofed.containsKey(left.toString())) {
                    return new Pair<>(pos, proofed.get(left.toString()));
                }
            }
        }
        return null;
    }

    void annotate() {
        int number = 0;
        for (Expression expression : proof) {
            int axiom = isAxiom(expression);
            Pair<Integer, Integer> mp = ModusPonens(expression);
            if (axiom != -1) {
                addToProofed(expression, number);
                annotations.add("Сх. акс. " + (axiom + 1));
            } else if (suppositions.containsKey(expression.toString())) {
                addToProofed(expression, number);
                annotations.add("Предп. " + suppositions.get(expression.toString()));
            } else if (mp != null) {
                addToProofed(expression, number);
                annotations.add("М.Р. " + (mp.getValue() + 1) + ", " + (mp.getKey() + 1));
            } else {
                annotations.add("Не доказано");
            }
            number++;
        }
    }

    String getAnnotation(int position) {
        return annotations.get(position);
    }
}
