import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Scanner {
    //hash map with key-atom type and value-its code; increasing, 0-identifier, 1-constant
    HashMap<String,Integer> scanner;
    String[] atoms;

    public Scanner(){
        this.scanner=new HashMap<>();
        this.atoms= new String[]{"identifier", "constant","int","char","if","else","while","input","print","begin","end",
                                "(",")","[","]",",",";","+","-","*","%","<","<=",">=",">","!=","==","="};
        int nr=0;
        for(String i:this.atoms)
        {
            scanner.put(i,nr);
            nr+=1;
        }
    }

    public List<String> tokenize(String line)
    {
        List<String> tokens=new ArrayList<>();
        int index=0;
        while(index<line.length()) {
            if(line.charAt(index)=='"')
            {
                //for strings
                HashMap<String,Integer> pair=addString(line,index);
                tokens.add((String) pair.keySet().toArray()[0]);
                index= (int) pair.values().toArray()[0];

            }
            else
            {
                if (isAtom("" + line.charAt(index))) {
                    if(index<line.length()-1)
                    {
                        int cnt=index;
                        cnt+=1;
                        if(line.charAt(cnt)=='=') {
                            if (isAtom("" + line.charAt(index) + line.charAt(cnt))) {
                                tokens.add("" + line.charAt(index) + line.charAt(cnt));
                                index += 1;
                            }
                        }
                        else {
                            tokens.add("" + line.charAt(index));
                        }
                    }
                    else {
                        tokens.add("" + line.charAt(index));
                    }
                }
                else
                    if (isIdentifier("" + line.charAt(index))) {
                        String sth=""+line.charAt(index);
                        int cnt = index;
                        if(index<line.length()-1) {
                            cnt += 1;
                            while (isIdentifier(""+line.charAt(cnt))) {
                                sth+=""+line.charAt(cnt);
                                index+=1;
                                if(cnt<line.length()-1) {
                                    cnt += 1;
                                }
                                else
                                    break; }
                        }
                        tokens.add(sth);
                    }
                    else
                        if (isConstant("" + line.charAt(index)))
                        {
                            if(index!=0) {
                                String sth = "";
                                if (tokens.get(tokens.size() - 1).equals("-") && (tokens.get(tokens.size() - 2).equals("=") || tokens.get(tokens.size() - 2).equals("<") || tokens.get(tokens.size() - 2).equals(">") || tokens.get(tokens.size() - 2).equals("<=") || tokens.get(tokens.size() - 2).equals(">="))) {
                                    tokens.remove(tokens.size() - 1);
                                    sth += "-";
                                }
                                sth += line.charAt(index);
                                int cnt = index;
                                if (index < line.length() - 1) {
                                    cnt += 1;
                                    while (isConstant("" + line.charAt(cnt))) {
                                        sth += "" + line.charAt(cnt);
                                        index += 1;
                                        if (cnt < line.length() - 1) {
                                            cnt += 1;
                                        } else
                                            break;
                                    }
                                }
                                if(!isIdentifier(""+line.charAt(index+1)))
                                    tokens.add(sth);
                                else
                                {
                                    sth+=line.charAt(index+1);
                                    tokens.add(sth);
                                    index+=1;
                                }
                            }
                        }
                        else
                            if(line.charAt(index)=='!' && line.charAt(index+1)=='=' ) {
                                String sth=""+line.charAt(index)+line.charAt(index+1);
                                index+=1;
                                tokens.add(sth);
                            }
                            else
                                {
                                //this will go as error
                                String sth=""+line.charAt(index);
                                if(line.charAt(index)!=' ') {
                                    int cnt = index;
                                    if(index<line.length()-1) {
                                        cnt += 1;
                                        while(line.charAt(index)!='(' && line.charAt(index)!=')' && line.charAt(index)!='[' && line.charAt(index)!=']' && line.charAt(index)!=',' && line.charAt(index)!=';') {
                                            sth += "" + line.charAt(cnt);
                                            index+=1;
                                            if(cnt<line.length()-1) {
                                                cnt += 1;
                                            }
                                            else
                                                break; }
                                        }
                                    tokens.add(sth);
                                    }
                                }

                        index += 1;

            }
        }
        return tokens;
    }

    private HashMap<String,Integer> addString(String line, int index) {
        HashMap<String,Integer> pair=new HashMap<>();
        String token="";
        int count = 0;
        while (index < line.length() && count < 2) {
            if (line.charAt(index) == '"')
                count += 1;
            if(count==2) {
                token+="\"";
                break;
            }
            token += line.charAt(index);
            index+=1;
        }
        index+=1;
        pair.put(token,index);
        return pair;
    }

    public boolean isIdentifier(String token)
    {
        return token.matches("^[a-zA-Z]([a-zA-Z]|[0-9]){0,255}$");
    }

    public boolean isConstant(String token)
    {
        if(token.matches("^(0|[+-]?[1-9][0-9]*)$"))
            return true;
        return token.charAt(0) == '"' && token.charAt(token.length() - 1) == '"';
    }

    public boolean isAtom(String token)
    {
        for(String s:this.atoms) {
            if (s.equals(token)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return "Scanner{" +
                "scanner=" + scanner +
                '}';
    }

}
