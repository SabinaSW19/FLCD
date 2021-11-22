import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Grammar grammar = new Grammar();
        String file = "src\\g3.txt";
        grammar.readFromFile(file);
        grammar.printNonTerminals();
        grammar.printTerminals();
        grammar.printProductions();
        grammar.printProductionsForAGivenNonTerminal("assignstmt");
        System.out.println(grammar.isCFG());
        LR0 lr0 = new LR0(grammar);

//        List<Production> productions = new ArrayList<>();
//        grammar.getP().keySet().forEach(
//                key -> productions.add(new Production(key, grammar.getP().get(key))));
//
//        System.out.println(lr0.closure(productions));
//        System.out.println(lr0.goTo(productions, "1"));
//        System.out.println(lr0.goTo(productions, "0"));
    }
}
