

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Grammar {
    List<String> N;
    List<String> E;
    HashMap<String, List<List<String>>> P;
    String S;

    public Grammar(List<String> n, List<String> e, HashMap<String, List<List<String>>> p, String s) {
        N = n;
        E = e;
        P = p;
        S = s;
    }

    public Grammar() {

    }

    public List<String> getN() {
        return N;
    }

    public void setN(List<String> n) {
        N = n;
    }

    public List<String> getE() {
        return E;
    }

    public void setE(List<String> e) {
        E = e;
    }

    public HashMap<String, List<List<String>>> getP() {
        return P;
    }

    public void setP(HashMap<String, List<List<String>>> p) {
        P = p;
    }

    public String getS() {
        return S;
    }

    public void setS(String s) {
        S = s;
    }

    public void readFromFile(String file) throws IOException {
        this.N = new ArrayList<>();
        this.E = new ArrayList<>();
        this.P = new HashMap<>();
        this.S = null;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String text = reader.readLine();
        while (text != null) {
            String[] elements = text.split("\\{");//see what we have on the line
            if (elements[0].equals("N")) {
                String list = elements[1].replace("}", "");
                String[] first = list.split("\\^");
                Collections.addAll(N, first);
            }
            if (elements[0].equals("E")) {
                String list = elements[1].replace("}", "");
                String[] first = list.split("\\^");
                //System.out.println(elements[1]);
                Collections.addAll(E, first);
            }
            if (elements[0].equals("P")) {
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
                    if (indexFirst == 0) {
                        firstPart = first[indexFirst];
                    } else {
                        firstPart = first[indexFirst].split("\\^")[1];
                    }
                    String[] second = first[indexFirst + 1].split("\\^");
                    String[] secondPart = second[0].split("\\|");
                    List<String> listStrings;
                    List<List<String>> listStringSecond=new ArrayList<>();
                    if (P.containsKey(firstPart)) {
                        for(String s:secondPart)
                        {
                            String[] part=s.split(" ");
                            listStrings=new ArrayList<>(Arrays.asList(part));
                            listStringSecond.addAll(Collections.singleton(listStrings));
                        }
                        P.get(firstPart).addAll(listStringSecond);
                    } else {
                        for(String s:secondPart)
                        {
                            String[] part=s.split(" ");
                            listStrings = new ArrayList<>(Arrays.asList(part));
                            listStringSecond.addAll(Collections.singleton(listStrings));
                        }
                        P.put(firstPart, listStringSecond);
                    }
                    indexFirst += 1;
                }

            }
            if (elements[0].equals("S")) {
                S = elements[1].replace("}", "");
            }
            text = reader.readLine();
        }

    }

    public void printNonTerminals() {
        System.out.println(this.N);
    }

    public void printTerminals() {
        System.out.println(this.E);
    }

    public void printProductions() {
        System.out.println(this.P);
    }

    public void printProductionsForAGivenNonTerminal(String nonTerminal) {
        if (this.P.containsKey(nonTerminal))
            System.out.println(this.P.get(nonTerminal));
    }

    public LinkedHashMap<HashMap<String, List<String>>,Integer> numberProduction()
    {
        LinkedHashMap<HashMap<String, List<String>>,Integer> number=new LinkedHashMap<>();
        int index=1;
        HashMap<String, List<List<String>>> productions=this.getP();
        for(Map.Entry<String,List<List<String>>>entry : productions.entrySet())
        {
            String key=entry.getKey();
            if(!key.equals("S'")) {
                List<List<String>> values = productions.get(key);
                for (List<String> value : values) {

                    HashMap<String, List<String>> elem = new HashMap<>();
                    elem.put(key, value);
                    number.put(elem,index);
                    index += 1;
                }
            }
        }
        return number;
    }
    public LinkedHashMap<Integer,HashMap<String, List<String>>> numberProduction2()
    {
        LinkedHashMap<Integer,HashMap<String, List<String>>> number=new LinkedHashMap<>();
        int index=1;
        HashMap<String, List<List<String>>> productions=this.getP();
        for(Map.Entry<String,List<List<String>>>entry : productions.entrySet())
        {
            String key=entry.getKey();
            if(!key.equals("S'")) {
                List<List<String>> values = productions.get(key);
                for (List<String> value : values) {

                    HashMap<String, List<String>> elem = new HashMap<>();
                    elem.put(key, value);
                    number.put(index,elem);
                    index += 1;
                }
            }
        }
        return number;
    }

    public boolean isCFG() {
        for (String i : this.P.keySet())
            if (!this.N.contains(i))
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
