import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Grammar {
    List<String> N;
    List<String> E;
    HashMap<String, List<String>> P;
    String S;

    public Grammar(List<String> n, List<String> e, HashMap<String, List<String>> p, String s) {
        N = n;
        E = e;
        P = p;
        S = s;
    }

    public Grammar() {

    }

    public void readFromFile(String file) throws IOException {
        this.N=new ArrayList<>();
        this.E=new ArrayList<>();
        this.P=new HashMap<>();
        this.S=null;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String text=reader.readLine();
        while(text!=null)
        {
            String[] elements = text.split("\\{");//see what we have on the line
            if(elements[0].equals("N"))
            {
                String list=elements[1].replace("}","");
                String[] first=list.split(",");
                Collections.addAll(N, first);
            }
            if(elements[0].equals("E"))
            {
                String list=elements[1].replace("}","");
                String[] first=list.split(",");
                //System.out.println(elements[1]);
                Collections.addAll(E, first);
            }
            if(elements[0].equals("P")) {
                String list = elements[1].replace("}", "");

                int index = list.indexOf("->");
                String copy = list;
                int count = 0;
                while (index != -1) {
                    count++;
                    copy = copy.substring(index + 1);
                    index = copy.indexOf("->");
                }
                String[] first = list.split("->");
                //System.out.println(Arrays.toString(first));
                int indexFirst = 0;
                for (int i = 0; i < count; i++) {
                    String firstPart;
                    if(indexFirst==0) {
                        firstPart = first[indexFirst];
                    }
                    else
                    {
                        firstPart=first[indexFirst].split(",")[1];
                    }
                    String[] second=first[indexFirst+1].split(",");
                    String[] secondPart=second[0].split("\\|");
                    if(P.containsKey(firstPart)) {
                        P.get(firstPart).addAll(Arrays.asList(secondPart));
                    }
                    else {
                        List<String> value = new ArrayList<>(Arrays.asList(secondPart));
                        P.put(firstPart,value);
                    }
                    indexFirst+=1;
                }

            }
            if(elements[0].equals("S"))
            {
                S=elements[1];
            }
            text=reader.readLine();
        }

    }

    public void printNonTerminals()
    {
        System.out.println(this.N);
    }

    public void printTerminals()
    {
        System.out.println(this.E);
    }

    public void printProductions()
    {
        System.out.println(this.P);
    }
    public void printProductionsForAGivenNonTerminal(String nonTerminal)
    {
        if(this.P.containsKey(nonTerminal))
            System.out.println(this.P.get(nonTerminal));
    }

    public boolean isCFG(){
        for(String i:this.P.keySet())
            if(!this.N.contains(i))
                return false;
        return true;
    }

    @Override
    public String toString() {
        return "Grammar{" +
                "N=" + N +
                ", E=" + E +
                ", P=" + P +
                ", S='" + S + '\'' +
                '}';
    }
}
