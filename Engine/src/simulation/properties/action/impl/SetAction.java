package simulation.properties.action.impl;

import manager.value.update.object.api.UpdateObject;
import manager.value.update.object.impl.OneObjectUpdate;
import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.property.api.Property;

public class SetAction extends AbstractAction {
    private Object value;

    public SetAction(String property, String contextEntity, String contextValue) {
        super(ActionType.SET, property, contextEntity, contextValue);
    }

    @Override
    public void updateValue(UpdateObject updateObject) {
        OneObjectUpdate oneObjectUpdate = (OneObjectUpdate)updateObject;
        value = oneObjectUpdate.getObjectForUpdate();
    }
    @Override
    public void Invoke(EntityInstance entityInstance) {
        Property toSet = entityInstance.getPropertyByName(getContextProperty());
        toSet.setValue(value);
    }
}
