import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymTable {
    int capacity;
    HashMap<Integer, List<String>> table;
    public SymTable(int capacity) {
        this.capacity = capacity;
        this.table=new HashMap<>();
        for(int i=0; i<this.capacity; i++)
            this.table.put(i, new ArrayList<>());
    }

    public int hash(String s)
    {
        int sum=0;
        for(int i=0; i<s.length(); i++)
            sum += s.charAt(i);
        return sum%this.capacity;
    }

    public List<Integer> find(String id){
        int hash=this.hash(id);
        List<String> list=this.table.get(hash);
        List<Integer> map=new ArrayList<>();
        for(int i=0; i<list.size(); i++)
            if(list.get(i).equals(id))
            {
                map.add(hash);
                map.add(i);
                return map;
            }
        map.add(-1);
        map.add(-1);
        return map;
    }

    public List<Integer> add(String id) {
        int hash = hash(id);
        if(!find(id).contains(-1))
            return find(id);
        else
            this.table.get(hash).add(id);
        return find(id);
    }

    @Override
    public String toString() {
        String symTableString="";
        for(Map.Entry<Integer, List<String>> i:this.table.entrySet())
            symTableString+=i.getKey()+"   "+i.getValue()+"\n";
        return symTableString;
    }

}
