import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PIF {
    //it will be a list of pairs of a code and the position in the symbol table
    List<List<Integer>> pif;

    public PIF() {
        this.pif = new ArrayList<>();
    }

    public void add(int code,List<Integer> position)
    {
        List<Integer> pair=new ArrayList<>();
        pair.add(code);
        pair.add(position.get(0));
        pair.add(position.get(1));
        this.pif.add(pair);
    }

    @Override
    public String toString() {
        String pifString="";
        for (List<Integer> integers : this.pif)
            if(integers.get(2)==-1)
                pifString += integers.get(0) + "    " + integers.get(1) +"\n";
            else
                pifString += integers.get(0) + "    " + integers.get(1)+"   "+integers.get(2) +"\n";
        return pifString;
    }
}
