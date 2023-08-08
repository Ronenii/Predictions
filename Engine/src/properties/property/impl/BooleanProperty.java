package properties.property.impl;

import properties.property.api.AbstractProperty;
import properties.property.api.PropertyType;

public class BooleanProperty extends AbstractProperty {

    public BooleanProperty(String name, boolean isRandInit, boolean value) {
        super(name, isRandInit, PropertyType.BOOLEAN, value);
    }
}
