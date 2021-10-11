
public class Main {
    public static void main(String[] args) {
        SymTable symTable =new SymTable(10);
        symTable.add("a");
        symTable.add("b");
        symTable.add("k");
        symTable.add("k");
        System.out.println(symTable.find("a"));
        System.out.println(symTable.find("k"));
        System.out.println(symTable.find("z"));
        System.out.println(symTable);

    }
}
