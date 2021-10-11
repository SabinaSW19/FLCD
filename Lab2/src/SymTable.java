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

    public Map<Integer,Integer> find(String id){
        int hash=this.hash(id);
        List<String> list=this.table.get(hash);
        HashMap<Integer,Integer> map=new HashMap<>();
        for(int i=0; i<list.size(); i++)
            if(list.get(i).equals(id))
            {
                map.put(hash,i);
                return map;
            }
        map.put(-1,-1);
        return map;
    }

    public int add(String id) {
        int hash = hash(id);
        if(!find(id).containsValue(-1))
            return hash;
        else
            this.table.get(hash).add(id);
        return hash;
    }

    @Override
    public String toString() {
        return "SymTable{" +
                "capacity=" + capacity +
                ", table=" + table +
                '}';
    }

}
