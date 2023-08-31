package simulation.properties.action.impl.proximity;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;

public class Proximity extends AbstractAction {
    private final String targetEntityName;

    private final Expression depth;

    private final ProximityActions proximityActions;

    public Proximity(ActionType type, String property, String contextEntity, String targetEntityName, Expression depth, ProximityActions proximityActions) {
        super(type, property, contextEntity);
        this.targetEntityName = targetEntityName;
        this.depth = depth;
        this.proximityActions = proximityActions;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void Invoke(EntityInstance entityInstance, int lastChangeTickCount) {

    }
}
