import java.util.ArrayList;
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
    public boolean checkIfDFA(){
        for(List<String> i:this.d.keySet()) {
            if (this.d.get(i).size() > 1)
                return false;
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
