import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FA {
    List<String> Q;
    List<String> E;
    HashMap<List<String>,List<String>> d;
    String q0;
    List<String> F;


    public FA(List<String> q, List<String> e, HashMap<List<String>, List<String>> d, String q0, List<String> f) {
        Q = q;
        E = e;
        this.d = d;
        this.q0 = q0;
        F = f;
    }

    public FA() {

    }

    public FA readFromFile(String file) throws IOException {
        List<String> Q = new ArrayList<>();
        List<String> E = new ArrayList<>();
        HashMap<List<String>, List<String>> d = new HashMap<>();
        String q0 = null;
        List<String> F = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String text = reader.readLine();
        while (text != null) {
            String[] elements = text.split("=");//see what we have on the line
            if (elements[0].equals("Q")) {
                String list = elements[1].replace("{", "");
                list = list.replace("}", "");
                String[] first = list.split(",");
                Collections.addAll(Q, first);
            }
            if (elements[0].equals("E")) {
                String list = elements[1].replace("{", "");
                list = list.replace("}", "");
                String[] second = list.split(",");
                Collections.addAll(E, second);
            }
            if (elements[0].equals("d")) {
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
                int indexFirst = 0;
                for (int i = 0; i < count; i++) {
                    String firstPart;
                    List<String> key = new ArrayList<>();
                    if (indexFirst == 0) {
                        firstPart = first[indexFirst];
                        String[] firstKey = firstPart.split(",");
                        key.add(firstKey[0]);
                        key.add(firstKey[1]);
                    } else {
                        firstPart = first[indexFirst].split(",")[1];
                        key.add(firstPart);
                        key.add(first[indexFirst].split(",")[2]);


                    }

                    String secondPart = first[indexFirst + 1].split(",")[0];
                    if (d.containsKey(key)) {
                        d.get(key).add(secondPart);
                    } else {
                        List<String> value = new ArrayList<>();
                        value.add(secondPart);
                        d.put(key, value);
                    }
                    indexFirst += 1;
                }

            }
            if (elements[0].equals("q0")) {
                q0 = elements[1];
            }
            if (elements[0].equals("F")) {
                String list = elements[1].replace("{", "");
                list = list.replace("}", "");
                String[] fifth = list.split(",");
                Collections.addAll(F, fifth);
            }
            text = reader.readLine();

        }
        FA fa = new FA(Q, E, d, q0, F);
        return fa;
    }
    public boolean checkIfDFA(){
        for(List<String> i:this.d.keySet()) {
            if (this.d.get(i).size() > 1) {
                System.out.println(i);
                System.out.println(d.get(i));
                System.out.println("sdfs");
                return false;
            }
        }
        return true;
    }

    public boolean checkIfSequenceIsAccepted(String seq)
    {
        if(checkIfDFA())
        {
            String element=this.q0;
            for(int i = 0; i<seq.length(); i++)
            {
                List<String> key=new ArrayList<>();
                key.add(element);
                key.add(String.valueOf(seq.charAt(i)));
                if(this.d.containsKey(key))
                    element= this.d.get(key).get(0);
                else
                    return false;
            }
            return this.F.contains(element);
        }
        return false;
    }

    @Override
    public String toString() {
        return "FA{" +
                "Q=" + Q +
                ", E=" + E +
                ", d=" + d +
                ", q0='" + q0 + '\'' +
                ", F=" + F +
                '}';
    }

}
