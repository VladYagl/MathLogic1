import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private final ArrayList<Expression> suppositions = new ArrayList<>();
    private final ArrayList<Expression> proof = new ArrayList<>();
    private final ArrayList<String> lines = new ArrayList<>();

    private void run() {
        File file = new File("res\\input.txt");
        String header = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            header = reader.readLine();
            String[] headres = header.split(",|\\|-");
            for (int i = 0; i < header.length() - 1; i++) {
                if (headres[i].isEmpty()) {
                    break;
                }
                suppositions.add(ExpressionParser.parse(headres[i]));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                proof.add(ExpressionParser.parse(line));
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProofChecker checker = new ProofChecker(suppositions, proof);
        checker.annotate();
        System.out.println(header);
        for (int i = 0; i < lines.size(); i++) {
            System.out.printf("(" + (i + 1) + ") " + lines.get(i) + " (" + checker.getAnnotation(i) + ")\n");
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
