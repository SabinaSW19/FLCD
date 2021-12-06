import java.util.*;
import java.util.stream.Collectors;

public class LR0 {

    private Grammar grammar;
    private String starting;

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
        Map<List<String>, List<List<String>>> lr0Table = new HashMap<>();
        int reduceIndex = 1;

        for (int i = 0; i < canonicalCollection.size(); i++) {
            List<Item> items = canonicalCollection.get(i);
            if (items.size() == 1 &&
                    Objects.equals(items.get(0).getLeftHandSide(), "S'") &&
                    Objects.equals(items.get(0).getRightHandSide().get(0), "S") &&
                    items.get(0).getDotPosition() == 1) {
                lr0Table.put(new ArrayList<>(Arrays.asList(String.valueOf(i), "Accept")), new ArrayList<>());
            } else {
                for (Item item : items) {
                    if (item.getRightHandSide().size() == item.getDotPosition()) {
                        lr0Table.put(
                                new ArrayList<>(Arrays.asList(String.valueOf(i), "Reduce " + reduceIndex++)),
                                new ArrayList<>()
                        );
                    }
                }
            }
            if (allShift(items)) {
                for (Item item : items) {
                    var lmao = goTo(items, item.getRightHandSide().get(item.getDotPosition()));
                    int size = -1;
                    for (int k = 0; k < canonicalCollection.size(); k++) {
                        if (checkContainsAll(canonicalCollection.get(k), lmao)) {
                            size = k;
                        }
                    }
                    System.out.println(size);
                    lr0Table.put(
                            new ArrayList<>(Arrays.asList(String.valueOf(i), "Shift")),
                            new ArrayList<>()
                    );
                }
            }
//            System.out.println(allShift(productions));
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
}

