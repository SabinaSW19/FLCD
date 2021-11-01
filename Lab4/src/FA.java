import java.util.HashMap;
import java.util.List;

public class FA {
    List<String> Q;
    List<String> E;
    HashMap<String,List<String>> d;
    String q0;
    List<String> F;


    public FA(List<String> q, List<String> e, HashMap<String, List<String>> d, String q0, List<String> f) {
        Q = q;
        E = e;
        this.d = d;
        this.q0 = q0;
        F = f;
    }

    public FA() {

    }
    public boolean checkIfDFA(String s){
        return true;
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
