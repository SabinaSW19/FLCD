import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static FA readFromFile() throws IOException {
        List<String> Q=new ArrayList<>();
        List<String> E=new ArrayList<>();
        HashMap<String,List<String>> d=new HashMap<>();
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
                    if(indexFirst==0)
                        firstPart=first[indexFirst];
                    else
                    {
                        firstPart=first[indexFirst].split(",")[1];
                        firstPart+=",";
                        firstPart+=first[indexFirst].split(",")[2];

                    }
                    String secondPart=first[indexFirst+1].split(",")[0];
                    if(d.containsKey(firstPart)) {
                        d.get(firstPart).add(secondPart);
                    }
                    else {
                        List<String> value = new ArrayList<>();
                        value.add(secondPart);
                        d.put(firstPart,value);
                    }
                    indexFirst+=1;
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

    private static void displayElements(FA fa) {
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
                default:
                    break;


            }
        }
    }

    private static void verify() {
    }

    public static void main(String[] args) throws IOException {
        FA fa=new FA();
        while (true) {
            System.out.println("1.Read FA from file.");
            System.out.println("2.Print FA. ");
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
                    verify();
                    break;
                }
                default:
                    break;

            }
        }
    }
}
