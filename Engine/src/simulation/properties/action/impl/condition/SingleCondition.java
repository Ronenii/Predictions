package simulation.properties.action.impl.condition;

import simulation.objects.entity.EntityInstance;

public class SingleCondition extends AbstractConditionAction{
    private final String operator;

    public SingleCondition(String property, String contextEntity, ThenOrElse thenActions, ThenOrElse elseActions, String operator, String contextValue) {
        super(property, contextEntity, thenActions, elseActions, contextValue);
        this.operator = operator;
    }

    @Override
    public void Invoke(EntityInstance entityInstance) {

    }
}
