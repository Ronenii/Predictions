package objects.rule;

import properties.action.api.Action;
import properties.activition.Activation;

import java.util.Map;

public class Rule {
    private final String name;
    private final Activation activation;
    private final Map<String,Action> actions;

    public Rule(String name, Activation activation, Map<String, Action> actions) {
        this.name = name;
        this.activation = activation;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public Activation getActivation() {
        return activation;
    }

    public Map<String, Action> getActions() {
        return actions;
    }

    @Override
    public String toString() {
        StringBuilder ruleToString = new StringBuilder("Rule{" +
                "name='" + name + '\'' +
                ", activation=" + activation +
                ", actions=[");

        for (Action a : actions.values()
        ) {
            int counter = 0;
            ruleToString.append(a);
            if (++counter != actions.size())
                ruleToString.append(", ");
            else
                ruleToString.append(']');
        }
        ruleToString.append('}');
        return ruleToString.toString();
    }
}
