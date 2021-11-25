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
        this.starting=this.grammar.S;
        this.grammar.getP().put("S'", toBeAdded);
        this.grammar.S = "S'";
    }

    public List<Production> closure(List<Production> productions) {

        Queue<Production> queue = new LinkedList<>(productions);
        List<Production> closure = new ArrayList<>(productions);

        Production currentProduction;
        while (!queue.isEmpty()) {
            currentProduction = queue.remove();
            if (currentProduction.getDotPosition() < currentProduction.getRightHandSide().size()) {
                String leftHandSide = currentProduction.getRightHandSide().get(currentProduction.getDotPosition());
                if (grammar.getN().contains(leftHandSide)) {
                    for (List<String> rightHandSide : grammar.P.get(leftHandSide)) {
                        Production production = new Production(leftHandSide, rightHandSide);
                        if (!closure.contains(production)) {
                            closure.add(production);
                            queue.add(production);
                        }
                    }
                }
            }
        }
        return closure;
    }

    public List<Production> goTo(List<Production> state, String element) {
        List<Production> newStates = new ArrayList<>();
        for (Production p : state) {
            if (p.getDotPosition() < p.getRightHandSide().size() && p.getRightHandSide().get(p.getDotPosition()).equals(element)) {
                Production a = new Production(p.getLeftHandSide(), p.getRightHandSide());
                a.setDotPosition(p.getDotPosition() + 1);
                newStates.add(a);
            }
        }
        return closure(newStates);
    }

    public List<List<Production>> canonicalCollection() {
        List<List<Production>> canonicalCollection = new ArrayList<>();
        List<String> rightHandSide = new ArrayList<>();
        rightHandSide.add(this.starting);
        List<Production> productionsS0 = new ArrayList<>();
        productionsS0.add(new Production("S'", rightHandSide));
        List<Production> s0 = closure(productionsS0);
        canonicalCollection.add(s0);
        List<String> nue = new ArrayList<>();
        nue.addAll(this.grammar.getN());
        nue.addAll(this.grammar.getE());
        List<List<Production>> canonicalCollectionCopy;
        canonicalCollectionCopy = canonicalCollection.stream().collect(Collectors.toList());
        while (true) {
            for (List<Production> p : canonicalCollection) {
                for (String n : nue) {
                    List<Production> elem = goTo(p, n);
                    if (!elem.isEmpty() && !check(canonicalCollectionCopy, elem)) {
                        canonicalCollectionCopy.add(elem);
                    }
                }
            }
            int ok = 1;
            for (List<Production> productions : canonicalCollectionCopy) {
                if (!check(canonicalCollection, productions)) {
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

    public boolean check(List<List<Production>> list, List<Production> elem) {
        int ok, nr;
        for (List<Production> productions : list) {
            ok = 0;
            nr = 0;
            for (Production production : productions) {
                for (Production value : elem) {
                    if (production.getLeftHandSide().equals(value.getLeftHandSide()) && production.getRightHandSide().equals(value.getRightHandSide()) && production.getDotPosition() == value.getDotPosition()) {
                        nr++;
                    }
                }
            }
            if (nr == productions.size()) {
                return true;
            }
        }
        return false;
    }

}

