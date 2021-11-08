import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static FA readFromFile() throws IOException {
        List<String> Q=new ArrayList<>();
        List<String> E=new ArrayList<>();
        HashMap<List<String>,List<String>> d=new HashMap<>();
        String q0 = null;
        List<String> F=new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\sdumi\\OneDrive\\Desktop\\InfoYear3\\Formal Languages and Compiler Design\\Lab4\\src\\FA.in"));
        String text = reader.readLine();
        while(text != null) {
            String[] elements = text.split("=");//see what we have on the line
            if(elements[0].equals("Q"))
            {
                String list=elements[1].replace("{","");
                list=list.replace("}","");
                String[] first=list.split(",");
                Collections.addAll(Q, first);
            }
            if(elements[0].equals("E"))
            {
                String list=elements[1].replace("{","");
                list=list.replace("}","");
                String[] second=list.split(",");
                Collections.addAll(E, second);
            }
            if(elements[0].equals("d")) {
                String list = elements[1].replace("{", "");
                list = list.replace("}", "");

                int index = list.indexOf("->");
                String copy = list;
                int count = 0;
                while (index != -1) {
                    count++;
                    copy = copy.substring(index + 1);
                    index = copy.indexOf("->");
                }
                list = list.replace("(", "");
                list = list.replace(")", "");
                String[] first = list.split("->");
                //System.out.println(Arrays.toString(first));
                int indexFirst=0;
                for (int i = 0; i < count; i++)
                {
                    String firstPart;
                    List<String> key=new ArrayList<>();
                    if(indexFirst==0) {
                        firstPart = first[indexFirst];
                        String[] firstKey=firstPart.split(",");
                        key.add(firstKey[0]);
                        key.add(firstKey[1]);
                    }
                    else
                    {
                        firstPart=first[indexFirst].split(",")[1];
                        key.add(firstPart);
                        //System.out.println(key);
                        key.add(first[indexFirst].split(",")[2]);


                    }
                    //System.out.println(key);
                    String secondPart=first[indexFirst+1].split(",")[0];
                    if(d.containsKey(key)) {
                        if(!d.get(key).contains(secondPart))
                            d.get(key).add(secondPart);
                    }
                    else {
                        List<String> value = new ArrayList<>();
                        value.add(secondPart);
                        //System.out.println(key);
                        //System.out.println(value);
                        d.put(key,value);
                    }
                    indexFirst+=1;
                    //System.out.println(indexFirst);
                }

            }
            if(elements[0].equals("q0"))
            {
                q0=elements[1];
            }
            if(elements[0].equals("F"))
            {
                String list=elements[1].replace("{","");
                list=list.replace("}","");
                String[] fifth=list.split(",");
                Collections.addAll(F, fifth);
            }
            text= reader.readLine();

        }
        FA fa=new FA(Q,E,d,q0,F);
        return fa;

    }

    private static void displayElements(FA fa) throws IOException {
        while (true) {
            System.out.println("1.States.");
            System.out.println("2.Alphabet. ");
            System.out.println("3.Transitions. ");
            System.out.println("4.Initial state. ");
            System.out.println("5.Final states.");
            System.out.println("Choose something:");
            Scanner in = new Scanner(System.in);
            String s = in.nextLine();
            switch (s) {
                case "1": {
                    System.out.println(fa.Q.toString());
                    break;
                }
                case "2": {
                    System.out.println(fa.E.toString());
                    break;
                }
                case "3": {
                    System.out.println(fa.d.toString());
                    break;
                }

                case "4": {
                    System.out.println(fa.q0);
                    break;
                }
                case "5": {
                    System.out.println(fa.F.toString());
                    break;
                }
                case "0":
                {
                    printMenu(fa);
                }
                default:
                    break;


            }
        }
    }

    private static void printMenu(FA fa) throws IOException {
        while (true) {
            System.out.println("1.Read FA from file.");
            System.out.println("2.Print FA. ");
            System.out.println("3.Check if a sequence is accepted by the DFA. ");
            System.out.println("Choose something:");
            Scanner in = new Scanner(System.in);
            String s = in.nextLine();
            switch (s) {
                case "1": {
                    fa=readFromFile();
                    break;
                }
                case "2": {
                    displayElements(fa);
                    break;
                }
                case "3": {
                    verify(fa);
                    break;
                }
                default:
                    break;

            }
        }
    }

    private static void verify(FA fa) {
        System.out.println("Enter a sequence.");
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        System.out.println(fa.checkIfSequenceIsAccepted(s));

    }

    public static void main(String[] args) throws IOException {
        FA fa=new FA();
        printMenu(fa);

    }
}
