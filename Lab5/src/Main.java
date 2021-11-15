import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Grammar grammar=new Grammar();
        String file="C:\\Users\\sdumi\\OneDrive\\Desktop\\InfoYear3\\Formal Languages and Compiler Design\\Lab5\\src\\g1.txt";
        grammar.readFromFile(file);
        grammar.printNonTerminals();
        grammar.printTerminals();
        grammar.printProductions();
        grammar.printProductionsForAGivenNonTerminal("A");
        System.out.println(grammar.isCFG());

    }
}
