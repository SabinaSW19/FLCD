import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        SymTable symTable =new SymTable(26);
//        symTable.add("a");
//        symTable.add("b");
//        symTable.add("k");
//        symTable.add("k");
//        System.out.println(symTable.find("a"));
//        System.out.println(symTable.find("k"));
//        System.out.println(symTable.find("z"));
//        System.out.println(symTable);
        Scanner scanner=new Scanner();
        PIF pif=new PIF();
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\sdumi\\OneDrive\\Desktop\\InfoYear3\\Formal Languages and Compiler Design\\Lab2Git\\FLCD\\Lab2\\p1.in"));
        String text = reader.readLine();
        int lineNumber=1;
        int cnt=0;
        while(text!=null) {
            List<String> tokens = scanner.tokenize(text);
            System.out.println(tokens);
            for (String i : tokens) {
                if (scanner.isAtom(i))
                    pif.add(scanner.scanner.get(i), Arrays.asList( -1,-1));
                else
                    if (scanner.isIdentifier(i))
                        pif.add(0, symTable.add(i));
                    else
                        if (scanner.isConstant(i))
                            pif.add(1, symTable.add(i));

                        else {cnt=1;
                            System.out.println("lexical error " + i + " at location " + lineNumber);
                        }
            }
            lineNumber+=1;
            //System.out.println(pif);
            text=reader.readLine();
        }
        reader.close();
        if(cnt==0) {
            System.out.println("lexically correct");
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\sdumi\\OneDrive\\Desktop\\InfoYear3\\Formal Languages and Compiler Design\\Lab2Git\\FLCD\\Lab2\\PIF.out"));
            BufferedWriter writer1 = new BufferedWriter(new FileWriter("C:\\Users\\sdumi\\OneDrive\\Desktop\\InfoYear3\\Formal Languages and Compiler Design\\Lab2Git\\FLCD\\Lab2\\ST.out"));
            writer.write(pif.toString());
            writer.newLine();
            writer.close();
            writer1.write(symTable.toString());
            writer1.newLine();
            writer1.close();

        }

    }
}
