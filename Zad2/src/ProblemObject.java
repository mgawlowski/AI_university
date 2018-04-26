import javafx.util.Pair;

import java.util.*;

@SuppressWarnings("WeakerAccess")
public class ProblemObject {

    /**
     * Contains pairs consisting of name of variable and its domain (possible values).
     */
    private List<Pair<String, List<Integer>>> variables = new ArrayList<>();
    private Set<Integer> indexesOfVarsToGo = new HashSet<>();
    private Map<Integer, Integer> chosenValues = new HashMap<>();

    /**
     * List used to keep track of changes, allowing to revert them when needed.
     * Contains lists for each step (choice point) containing pairs of variable index and value.
     * First element of inner list indicates chosen value, rest - values deleted due constraints satisfaction.
     */
    private List<List<Pair<Integer, Integer>>> steps = new ArrayList<>();
    private int currentStep = -1;

    public ProblemObject() {
    }

    public void addVariable(String name, List<Integer> domain) {
        variables.add(new Pair<>(name, domain));
        indexesOfVarsToGo.add(variables.size() - 1);
    }

    public Set<Integer> getVariablesIndToGo() {
        return indexesOfVarsToGo;
    }

    public int getChosenValue(int index) {
        return chosenValues.get(index);
    }

    public Map<Integer, Integer> getChosenValues() {
        return chosenValues;
    }

    public List<Integer> getDomain(int index) {
        return variables.get(index).getValue();
    }

    public void chooseValue(int index, int value) {
        currentStep++;
        steps.add(new ArrayList<>());
        indexesOfVarsToGo.remove(index);
        chosenValues.put(index, value);

        steps.get(currentStep).add(new Pair<>(index, value));
    }

    public void deleteValue(int index, int value) {
        if (indexesOfVarsToGo.contains(index)) {
            if (variables.get(index).getValue().remove((Integer) value)) {
                steps.get(currentStep).add(new Pair<>(index, value));
            }
        }
    }

    public void revert() {
        // first pair is chosen value at this step and it gets deleted
        // fact of deleting particular value is added to previous step so it's not permanent and can be reverted as well
        Pair<Integer, Integer> valueToRemove = steps.get(currentStep).remove(0);

        variables.get(valueToRemove.getKey()).getValue().remove(valueToRemove.getValue());
        if (currentStep > 0) {
            steps.get(currentStep - 1).add(valueToRemove);
        }

        indexesOfVarsToGo.add(valueToRemove.getKey());
        chosenValues.remove(valueToRemove.getKey());

        for (Pair<Integer, Integer> p : steps.get(currentStep)) {
            variables.get(p.getKey()).getValue().add(p.getValue());
        }
        steps.remove(currentStep--);
    }

    public boolean isAnyDomainEmpty() {
        for (Pair<String, List<Integer>> p : variables) {
            if (p.getValue().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
