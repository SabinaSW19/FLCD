import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Main {

    public static void main(String[] args) throws IOException {
        Grammar grammar = new Grammar();
        String file = "src\\g4.txt";
        String outputTablePath = "src\\tableOutput.txt";
        String parsingOutputPath = "src\\parsingOutput.txt";
        grammar.readFromFile(file);
        grammar.printNonTerminals();
        grammar.printTerminals();
        grammar.printProductions();
        grammar.printProductionsForAGivenNonTerminal("stmtlist");
        System.out.println(grammar.isCFG());
        LR0 lr0 = new LR0(grammar);

        List<Item> items = new ArrayList<>();
        for (String s : grammar.getP().keySet()) {
            for (List<String> list : grammar.getP().get(s))
                items.add(new Item(s, list));

        }
        //System.out.println(productions);
        //lr0.closure(productions);
        //System.out.println(lr0.closure(productions));
        //System.out.println(lr0.goTo(productions, "b"));
//        List<String> rightHandSide = new ArrayList<>();
//        rightHandSide.add("S");
//        List<Production> productionsS0 = new ArrayList<>();
//        productionsS0.add(new Production("S'", rightHandSide));
//        List<Production> s0 = lr0.closure(productionsS0);
//        System.out.println(s0);
//        //System.out.println(lr0.goTo(s0,"a"));
//        List<Production> s2=lr0.goTo(s0,"a");
//        System.out.println();
//        System.out.println(s2);
//        System.out.println(lr0.goTo(s2,"A"));
        //System.out.println(lr0.canonicalCollection());
        List<List<Item>> states = lr0.canonicalCollection();
        System.out.println(lr0.stateToString(states));
        Map<List<String>, List<List<String>>> table = lr0.generateLR0Table(states);
        //System.out.println(lr0.generateLR0Table(states));
        String tableString = lr0.tableToString(table);
        System.out.println(tableString);
        Files.write(Path.of(outputTablePath), tableString.getBytes(StandardCharsets.UTF_8));
        System.out.println(grammar.numberProduction().toString());
        List<String> word = new ArrayList<>();
        word.add("a");
        word.add("b");
        word.add("b");
        word.add("c");
        //word.add("end");
//        word.add("c");
        Stack<String> outputStack = lr0.outputStack(word, table);
        System.out.println(outputStack);
        //System.out.println(lr0.productionsInOutput(outputStack));
        String parsingOutput = lr0.printParsingTable(lr0.productionsInOutput(outputStack));
        System.out.println(parsingOutput);
        parsingOutput += "\n\n Productions used " + outputStack;
        Files.write(Path.of(parsingOutputPath), parsingOutput.getBytes(StandardCharsets.UTF_8));
    }
}
