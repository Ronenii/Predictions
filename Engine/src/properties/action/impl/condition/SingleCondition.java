package properties.action.impl.condition;

import properties.property.api.Property;

public class SingleCondition extends AbstractConditionAction{
    private final String operator;

    public SingleCondition(String property, String contextEntity, Object value, ThenOrElse thenActions, ThenOrElse elseActions, String operator) {
        super(property, contextEntity, value, thenActions, elseActions);
        this.operator = operator;
    }

    @Override
    public void Invoke() {

    }
}
