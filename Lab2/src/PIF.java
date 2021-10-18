import java.util.ArrayList;
import java.util.List;

public class PIF {
    //it will be a list of pairs of a code and the position in the symbol table
    List<List<Integer>> pif;

    public PIF() {
        this.pif = new ArrayList<>();
    }

    public void add(int code, int position)
    {
        List<Integer> pair=new ArrayList<>();
        pair.add(code);
        pair.add(position);
        this.pif.add(pair);
    }

    @Override
    public String toString() {
        return "PIF{" +
                "pif=" + pif +
                '}';
    }
}
