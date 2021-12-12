import java.util.*;
import java.util.stream.Collectors;

public class LR0 {

    private final Grammar grammar;
    private final String starting;

    public LR0(Grammar grammar) {
        this.grammar = grammar;
        List<String> listStrings = new ArrayList<>(Collections.singletonList(this.grammar.S));
        List<List<String>> toBeAdded = new ArrayList<>();
        toBeAdded.add(listStrings);
        this.starting = this.grammar.S;
        this.grammar.getP().put("S'", toBeAdded);
        this.grammar.S = "S'";
    }

    public List<Item> closure(List<Item> items) {

        Queue<Item> queue = new LinkedList<>(items);
        List<Item> closure = new ArrayList<>(items);

        Item currentItem;
        while (!queue.isEmpty()) {
            currentItem = queue.remove();
            if (currentItem.getDotPosition() < currentItem.getRightHandSide().size()) {
                String leftHandSide = currentItem.getRightHandSide().get(currentItem.getDotPosition());
                if (grammar.getN().contains(leftHandSide)) {
                    for (List<String> rightHandSide : grammar.P.get(leftHandSide)) {
                        Item item = new Item(leftHandSide, rightHandSide);
                        if (!checkContains(closure, item)) {
                            closure.add(item);
                            queue.add(item);
                        }
                    }
                }
            }
        }
        return closure;
    }

    public List<Item> goTo(List<Item> state, String element) {
        List<Item> newStates = new ArrayList<>();
        for (Item p : state) {
            if (p.getDotPosition() < p.getRightHandSide().size() && p.getRightHandSide().get(p.getDotPosition()).equals(element)) {
                Item a = new Item(p.getLeftHandSide(), p.getRightHandSide());
                a.setDotPosition(p.getDotPosition() + 1);
                newStates.add(a);
            }
        }
        return closure(newStates);
    }


    public List<List<Item>> canonicalCollection() {
        List<List<Item>> canonicalCollection = new ArrayList<>();
        List<String> rightHandSide = new ArrayList<>();
        rightHandSide.add(this.starting);
        List<Item> productionsS0 = new ArrayList<>();
        productionsS0.add(new Item("S'", rightHandSide));
        List<Item> s0 = closure(productionsS0);
        canonicalCollection.add(s0);
        List<String> nue = new ArrayList<>();
        nue.addAll(this.grammar.getN());
        nue.addAll(this.grammar.getE());
        List<List<Item>> canonicalCollectionCopy;
        canonicalCollectionCopy = canonicalCollection.stream().collect(Collectors.toList());
        while (true) {
            for (List<Item> p : canonicalCollection) {
                for (String n : nue) {
                    List<Item> elem = goTo(p, n);
                    if (!elem.isEmpty() && !check(canonicalCollectionCopy, elem)) {
                        canonicalCollectionCopy.add(elem);
                    }
                }
            }
            int ok = 1;
            for (List<Item> items : canonicalCollectionCopy) {
                if (!check(canonicalCollection, items)) {
                    ok = 0;
                }
            }
            if (ok == 1) {
                break;
            }
            canonicalCollection = List.copyOf(canonicalCollectionCopy);
        }

        return canonicalCollection;
    }

    public boolean check(List<List<Item>> list, List<Item> elem) {
        int ok, nr;
        for (List<Item> items : list) {
            ok = 0;
            nr = 0;
            for (Item item : items) {
                for (Item value : elem) {
                    if (item.getLeftHandSide().equals(value.getLeftHandSide()) && item.getRightHandSide().equals(value.getRightHandSide()) && item.getDotPosition() == value.getDotPosition()) {
                        nr++;
                    }
                }
            }
            if (nr == items.size()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkContains(List<Item> items, Item item) {
        for (Item p : items) {
            if (p.getLeftHandSide().equals(item.getLeftHandSide()) && p.getRightHandSide().equals(item.getRightHandSide()) && p.getDotPosition() == item.getDotPosition()) {
                return true;
            }
        }
        return false;
    }

    public String stateToString(List<List<Item>> states) {
        String s = "";
        for (int i = 0; i < states.size(); i++) {
            s += "state " + i + " " + states.get(i);
            s += "\n";
        }

        return s;
    }


    public Map<List<String>, List<List<String>>> generateLR0Table(List<List<Item>> canonicalCollection) {
        // Map<Pair<State,Action>,List<Pair<NUE,State>>>
        Map<List<String>, List<List<String>>> lr0Table = new LinkedHashMap<>();

        if (!checkReduceReduceConflict(canonicalCollection) && !checkShiftReduceConflict(canonicalCollection)) {
            for (int i = 0; i < canonicalCollection.size(); i++) {
                List<Item> items = canonicalCollection.get(i);
                if (items.size() == 1 &&
                        Objects.equals(items.get(0).getLeftHandSide(), "S'") &&
                        items.get(0).getDotPosition() == 1) {
                    lr0Table.put(new ArrayList<>(Arrays.asList(String.valueOf(i), "Accept")), new ArrayList<>());
                } else {
                    if (items.size() == 1) {
                        Item item = items.get(0);
                        if (item.getRightHandSide().size() == item.getDotPosition()) {
                            HashMap<String, List<String>> production = new HashMap<>();
                            production.put(item.getLeftHandSide(), item.getRightHandSide());
                            lr0Table.put(
                                    new ArrayList<>(Arrays.asList(String.valueOf(i), "Reduce " + this.grammar.numberProduction().get(production))),
                                    new ArrayList<>()
                            );
                        }
                    }
                }
                if (allShift(items)) {
                    for (Item item : items) {
                        String elem = item.getRightHandSide().get(item.getDotPosition());
                        var goToValue = goTo(items, elem);
                        int size = -1;
                        for (int k = 0; k < canonicalCollection.size(); k++) {
                            if (checkContainsAll(canonicalCollection.get(k), goToValue)) {
                                size = k;
                            }
                        }
                        List<String> value = new ArrayList<>();
                        value.add(elem);
                        value.add(String.valueOf(size));
                        List<List<String>> values = new ArrayList<>();
                        values.add(value);
                        List<String> key = new ArrayList<>();
                        key.add(String.valueOf(i));
                        key.add("Shift");
                        if (!lr0Table.containsKey(key))
                            lr0Table.put(key, values);
                        else if (!lr0Table.get(key).contains(value))
                            lr0Table.get(key).add(value);
                    }
                }
//            System.out.println(allShift(productions));
            }
        }
        return lr0Table;
    }

    private boolean checkContainsAll(List<Item> state, List<Item> goToResult) {
        for (int i = 0; i < state.size(); i++) {
            if (!state.get(i).equals(goToResult.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean allShift(List<Item> items) {
        for (Item item : items) {
            if (item.getDotPosition() == item.getRightHandSide().size()) {
                return false;
            }
        }
        return true;
    }


    public String tableToString(Map<List<String>, List<List<String>>> table) {
        String s = "";
        s += "   " + "Action" + "    ";
        List<String> nue = new ArrayList<>();
        nue.addAll(this.grammar.getN());
        nue.addAll(this.grammar.getE());
        for (String i : nue)
            s += i + "   ";
        s += "\n";
        for (Map.Entry<List<String>, List<List<String>>> entry : table.entrySet()) {

            List<String> key = entry.getKey();
            List<List<String>> value = entry.getValue();

            //System.out.println(key);
            s += key.get(0) + "  " + key.get(1);
            if (key.get(1).equals("Accept"))
                s += "    ";
            else if (key.get(1).contains("Reduce"))
                s += "  ";
            else if (key.get(1).contains("Shift"))
                s += "     ";
            for (String i : nue) {
                if (checkValueIsContained(value, i)) {
                    String elem = checkValueIsContainedAndReturnIt(value, i);
                    s += elem + "   ";
                } else
                    s += "-" + "   ";
            }
            s += "\n";
        }
        return s;
    }

    private boolean checkValueIsContained(List<List<String>> value, String i) {
        for (List<String> j : value) {
            String elem = j.get(0);
            if (elem.equals(i))
                return true;
        }
        return false;
    }

    private String checkValueIsContainedAndReturnIt(List<List<String>> value, String i) {
        for (List<String> j : value) {
            String elem = j.get(0);
            if (elem.equals(i))
                return j.get(1);
        }
        return " ";
    }

    public Stack<String> outputStack(List<String> w, Map<List<String>, List<List<String>>> table) {
        Stack<String> workingStack = new Stack<>();
        Stack<String> inputStack = new Stack<>();
        Stack<String> outputStack = new Stack<>();
        workingStack.add("$");
        workingStack.add("0");
        inputStack.add("$");
        for (int i = w.size() - 1; i >= 0; i--) {
            inputStack.add(w.get(i));
        }
        //System.out.println(workingStack);
        //System.out.println(inputStack);
        while (true) {
            String currentState = workingStack.get(workingStack.size() - 1);
            //System.out.println(currentState);
            if (getKey(table, currentState).equals("Accept")) {
                break;
            } else {
                if (getKey(table, currentState).equals("Shift")) {
                    String currentHead = inputStack.pop();
                    String nextState = getValue(table, currentHead, currentState);
                    //System.out.println(nextState);
                    workingStack.add(currentHead);
                    workingStack.add(nextState);
                    System.out.println(workingStack);
                } else {
                    if (getKey(table, currentState).contains("Reduce")) {
                        String action = getKey(table, currentState);
                        String[] actionSplit = action.split(" ");
                        String productionNumber = actionSplit[1];
                        outputStack.add(productionNumber);
                        HashMap<String, List<String>> production = this.grammar.numberProduction2().get(Integer.valueOf(productionNumber));
                        //System.out.println(production);
                        String lhs = "";
                        List<String> rhs = new ArrayList<>();
                        for (Map.Entry<String, List<String>> entry : production.entrySet()) {
                            lhs = entry.getKey();
                            rhs = entry.getValue();
                            break;
                        }

                        List<String> rhsCopy = new ArrayList<>(List.copyOf(rhs));
                        while (!rhsCopy.isEmpty()) {
                            workingStack.pop();
                            workingStack.pop();
                            rhsCopy.remove(rhsCopy.size() - 1);
                        }
                        String previousElement = workingStack.get(workingStack.size() - 1);
                        workingStack.add(lhs);
                        String nextState = getValue(table, lhs, previousElement);
                        //System.out.println(nextState);
                        workingStack.add(nextState);
//                        System.out.println(inputStack);
//                        System.out.println(workingStack);
                    }
                }
            }
        }
        Stack<String> outputStackReversed = new Stack<>();
        while (!outputStack.isEmpty()) {
            String elem = outputStack.pop();
            outputStackReversed.push(elem);
        }
        return outputStackReversed;
    }

    public String getKey(Map<List<String>, List<List<String>>> table, String state) {
        for (Map.Entry<List<String>, List<List<String>>> entry : table.entrySet()) {
            List<String> key = entry.getKey();
            if (key.get(0).equals(state))
                return key.get(1);
        }
        return "";
    }

    public String getValue(Map<List<String>, List<List<String>>> table, String currentHead, String state) {
        for (Map.Entry<List<String>, List<List<String>>> entry : table.entrySet()) {
            List<String> key = entry.getKey();
            //List<List<String>> values=entry.getValue();
            if (key.get(0).equals(state)) {
                List<List<String>> values = table.get(key);
                for (List<String> value : values)
                    if (value.get(0).equals(currentHead))
                        return value.get(1);
            }
        }
        return "";
    }

    public boolean checkShiftReduceConflict(List<List<Item>> canonicalCollection) {
        int cnt1 = 0;
        for (int i = 0; i < canonicalCollection.size(); i++) {
            List<Item> items = canonicalCollection.get(i);
            int cnt = 0;
            int cnt2 = 0;
            for (Item it : items) {
                if (it.getRightHandSide().size() == it.getDotPosition()) {
                    cnt++;
                }
                if (it.getRightHandSide().size() > it.getDotPosition()) {
                    cnt2++;
                }
            }
            //System.out.println(cnt+" "+cnt2);
            if (cnt >= 1 && cnt2 >= 1) {
                System.out.println("Shift-reduce conflict! At state: " + i + items);
                cnt1 += 1;
            }
        }
        return cnt1 != 0;
    }

    public boolean checkReduceReduceConflict(List<List<Item>> canonicalCollection) {
        int cnt1 = 0;
        for (int i = 0; i < canonicalCollection.size(); i++) {
            List<Item> items = canonicalCollection.get(i);
            int cnt = 0;
            for (Item it : items) {
                if (it.getRightHandSide().size() == it.getDotPosition()) {
                    cnt++;
                }
            }
            if (cnt > 1) {
                System.out.println("Reduce-reduce conflict! At state: " + i + items);
                cnt1 += 1;
            }
        }
        return cnt1 != 0;
    }

    public List<HashMap<String, List<String>>> productionsInOutput(Stack<String> outputStack) {
        List<HashMap<String, List<String>>> productions = new ArrayList<>();
        for (String i : outputStack) {
            productions.add(this.grammar.numberProduction2().get(Integer.valueOf(i)));
        }
        return productions;
    }

    public String printParsingTable(List<HashMap<String, List<String>>> productions) {
        List<ParserOutput> tree = new ArrayList<>();
        tree.add(new ParserOutput(0, -1, -1, this.starting));
        getRecursive(tree, productions, 0, 1, 0);
        return tree.stream().map(ParserOutput::toString)
                .collect(Collectors.joining("\n"));
    }

    public void getRecursive(List<ParserOutput> tree, List<HashMap<String, List<String>>> productions, int parent, int rowIndex, int currentProductionIndex) {

        HashMap<String, List<String>> production = productions.get(currentProductionIndex);
        //System.out.println(productions);
        List<String> rhs = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : production.entrySet())
            rhs = entry.getValue();
        List<ParserOutput> auxRows = new ArrayList<>();
        for (int i = 0; i < rhs.size(); i++) {
            ParserOutput row = new ParserOutput();
            row.setIndex(rowIndex);
            rowIndex++;
            row.setSymbol(rhs.get(i));
            //System.out.println(rhs);
            row.setParent(parent);

            if (i < rhs.size() - 1) {
                row.setRightSibling(rowIndex);
            } else {
                row.setRightSibling(-1);
            }
            //System.out.println(row);
            auxRows.add(row);
        }
        tree.addAll(auxRows);
        for (ParserOutput row : auxRows) {
            if (grammar.getN().contains(row.getSymbol())) {
                currentProductionIndex = currentProductionIndex + 1;
                getRecursive(tree, productions, row.getIndex(), rowIndex, currentProductionIndex);
            }
        }

    }
}

