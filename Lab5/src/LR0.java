import java.util.*;

public class LR0 {

    private final Grammar grammar;
    private final ArrayList<String> states;

    public LR0(Grammar grammar) {
        this.grammar = grammar;
        this.grammar.getP().put("Q'", Arrays.asList(this.grammar.S));
        this.grammar.S = "Q'";
        this.states = new ArrayList<>();
    }

    public Set<Production> closure(List<Production> productions) {

        Queue<Production> queue = new LinkedList<>(productions);
        Set<Production> closure = new HashSet(productions);

        Production currentProduction;
        while (!queue.isEmpty()) {
            currentProduction = queue.remove();
            if (currentProduction.getDotPosition() < currentProduction.getRightHandSide().size()) {
                String leftHandSide = currentProduction.getRightHandSide().get(currentProduction.getDotPosition());
                if (grammar.getN().contains(leftHandSide)) {
                    for (var rightHandSide : grammar.P.get(leftHandSide)) {
                        Production production = new Production(leftHandSide, List.of(rightHandSide));
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

    public Set<Production> goTo(List<Production> state, String element) {
        List<Production> newStates = new ArrayList<>();

        state.forEach(production -> {
            if (production.getDotPosition() < production.getRightHandSide().size() &&
                    Objects.equals(production.getRightHandSide().get(production.getDotPosition()), element)) {
                production.setDotPosition(production.getDotPosition() + 1);
                newStates.add(production);
            }
        });
        System.out.println(newStates);
        return closure(newStates);
    }


}
