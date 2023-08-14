package jaxb.unmarshal.converter;

import jaxb.schema.generated.*;
import jaxb.unmarshal.converter.expression.converter.ExpressionConverterAndValidator;
import jaxb.unmarshal.converter.functions.HelperFunctionsType;
import jaxb.unmarshal.converter.functions.StaticHelperFunctions;
import simulation.objects.entity.Entity;
import simulation.properties.ending.conditions.EndingConditionType;
import simulation.properties.rule.Rule;
import simulation.objects.world.World;
import simulation.properties.action.api.Action;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.impl.DecreaseAction;
import simulation.properties.action.impl.IncreaseAction;
import simulation.properties.action.impl.KillAction;
import simulation.properties.action.impl.SetAction;
import simulation.properties.action.impl.calculation.CalculationAction;
import simulation.properties.action.impl.calculation.ClaculationType;
import simulation.properties.action.impl.condition.AbstractConditionAction;
import simulation.properties.action.impl.condition.MultipleCondition;
import simulation.properties.action.impl.condition.SingleCondition;
import simulation.properties.action.impl.condition.ThenOrElse;
import simulation.properties.activition.Activation;
import simulation.properties.ending.conditions.EndingCondition;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;
import simulation.properties.property.impl.BooleanProperty;
import simulation.properties.property.impl.DoubleProperty;
import simulation.properties.property.impl.IntProperty;
import simulation.properties.property.impl.StringProperty;

import java.util.*;

/**
 * A class used to convert the objects generated from the scheme to objects that will be used in the program.
 */
public class PRDConverter {
    // In order to get values from the world object on the run.

    private Map<String, Property> environmentProperties;
    private Map<String, Entity> entities;

    public World PRDWorld2World(PRDWorld prdWorld) {

        Map<String, Property> environmentProperties;
        Map<String, Entity> entities;
        Map<String, Rule> rules;
        Set<EndingCondition> endingConditions;

        // Iterates over all PRDEnvironmentProperties, converts each property and adds it to 'environmentProperties'
        environmentProperties = getEnvironmentProperties(prdWorld);
        this.environmentProperties = environmentProperties;

        // Iterates over all PRDEntities, converts each entity and adds it to 'entities'
        entities = getEntities(prdWorld);
        this.entities = entities;

        // Iterates over all PRDRules, converts each rule and adds it to 'rules'
        rules = getRules(prdWorld);

        // Iterates over all PRDByTicks or PRDBySecond, converts each ending condition and adds it to 'endingConditions'
        endingConditions = getEndingConditions(prdWorld.getPRDTermination());

        return new World(environmentProperties, entities, rules, endingConditions);
    }

    private Map<String, Property> getEnvironmentProperties(PRDWorld prdWorld){
        Map<String, Property> environmentProperties = new HashMap<>();
        List<PRDEnvProperty> prdEnvProperties = prdWorld.getPRDEvironment().getPRDEnvProperty();
        Property propertyToAdd;

        for(PRDEnvProperty envProperty : prdEnvProperties){
            propertyToAdd = PRDEnvProperty2Property(envProperty);
            if(propertyToAdd != null){
                environmentProperties.put(envProperty.getPRDName(), propertyToAdd);
            }
        }

        return environmentProperties;
    }

    private Map<String, Entity> getEntities(PRDWorld prdWorld){
        Map<String, Entity> entities = new HashMap<>();
        List<PRDEntity> prdEntities = prdWorld.getPRDEntities().getPRDEntity();
        Entity entityToAdd;

        for(PRDEntity prdEntity : prdEntities){
            entityToAdd = PRDEntity2Entity(prdEntity);
            if(entityToAdd != null){
                entities.put(prdEntity.getName(), entityToAdd);
            }
        }

        return entities;
    }

    private Map<String, Rule> getRules(PRDWorld prdWorld){
        Map<String, Rule> rules = new HashMap<>();
        List<PRDRule> prdRules = prdWorld.getPRDRules().getPRDRule();
        Rule ruleToAdd;

        for(PRDRule prdRule : prdRules){
            ruleToAdd = PRDRule2Rule(prdRule);
            if(ruleToAdd != null){
                rules.put(prdRule.getName(), ruleToAdd);
            }
        }

        return rules;
    }

    private Map<String, Property> getEntityProperties(PRDEntity prdEntity){
        Map<String, Property> entityProperties = new HashMap<>();
        List<PRDProperty> prdEntityProperties = prdEntity.getPRDProperties().getPRDProperty();
        Property propertyToAdd;

        for(PRDProperty property : prdEntityProperties){
            propertyToAdd = PRDProperty2Property(property);
            if(propertyToAdd != null){
                entityProperties.put(property.getPRDName(), propertyToAdd);
            }
        }

        return entityProperties;
    }

    private Set<Action> getActionsWithGivenList(List<PRDAction> prdActions){
        Set<Action> actions = new HashSet<>();
        Action actionToAdd;

        for(PRDAction action: prdActions){
            actionToAdd = PRDAction2Action(action);
            if(actionToAdd != null){
                actions.add(actionToAdd);
            }
        }

        return actions;
    }

    /**
     * Converts the given PRDEnvProperty to Property
     *
     * @param prdEnvProperty the given PRDEnvProperty generated from reading the XML file
     * @return a Property representation of PRDEnvProperty.
     * <p>
     * TODO: Right now I hard coded placeholders for thing such as @value & @is_Random_init in the ctors. We need to make this more elegant.
     */
    private Property PRDEnvProperty2Property(PRDEnvProperty prdEnvProperty) {
        Property ret = null;
        String name = prdEnvProperty.getPRDName();
        double to = prdEnvProperty.getPRDRange().getTo();
        double from = prdEnvProperty.getPRDRange().getFrom();

        switch (PropertyType.valueOf(prdEnvProperty.getType())) {
            case INT:
                ret = new IntProperty(name, false, 0, (int) from, (int) to);
                break;
            case DOUBLE:
                ret = new DoubleProperty(name, false, 0.0, from, to);
                break;
            case BOOLEAN:
                ret = new BooleanProperty(name, false, false);
                break;
            case STRING:
                ret = new StringProperty(name, false, "");
                break;
            default:
                String err = String.format("\"%s\" is not a valid Property type.", prdEnvProperty.getType());
                throw new IllegalArgumentException(err);
        }

        return ret;
    }


    /**
     * Converts the given PRDProperty to Property
     *
     * @param prdProperty the given prdProperty generated from reading the XML file
     * @return a Property representation of prdProperty.
     */
    private Property PRDProperty2Property(PRDProperty prdProperty) {
        Property ret = null;
        String name = prdProperty.getPRDName();

        double to = prdProperty.getPRDRange().getTo();
        double from = prdProperty.getPRDRange().getFrom();

        boolean isRandomInit = prdProperty.getPRDValue().isRandomInitialize();
        String value = prdProperty.getPRDValue().getInit();


        try {
            switch (PropertyType.valueOf(prdProperty.getType())) {
                case INT:
                    ret = new IntProperty(name, isRandomInit, Integer.parseInt(value), (int) from, (int) to);
                    break;
                case DOUBLE:
                    ret = new DoubleProperty(name, isRandomInit, Double.parseDouble(value), from, to);
                    break;
                case BOOLEAN:
                    ret = new BooleanProperty(name, isRandomInit, Boolean.parseBoolean(value));
                    break;
                case STRING:
                    ret = new StringProperty(name, isRandomInit, value);
                    break;
            }
        } catch (Exception e) {
            String err = String.format("\"%s\" is not a valid Property type.", prdProperty.getType());
            throw new IllegalArgumentException(err);
        }

        return ret;
    }

    /**
     * Converts the given PRDEntity to Entity
     *
     * @param prdEntity the given PRDEntity generated from reading the XML file
     * @return an Entity representation of PRDEntity.
     */
    private Entity PRDEntity2Entity(PRDEntity prdEntity) {
        String name = prdEntity.getName();
        int population = prdEntity.getPRDPopulation();
        Map<String, Property> properties;

        // Iterates over all PRDProperties, converts each property and adds it to 'properties'
        properties = getEntityProperties(prdEntity);

        return new Entity(population, name, properties);
    }

    /**
     * Converts the given PRDRule to Rule
     *
     * @param prdRule the given PRDRule generated from reading the XML file
     * @return a Rule representation of PRDRule.
     */
    private Rule PRDRule2Rule(PRDRule prdRule) {
        String name = prdRule.getName();
        Activation activation = PRDActivation2Activation(prdRule.getPRDActivation());
        Set<Action> actions;

        // Iterates over all prdActions inside the prdRule and convert them to action
        actions = getActionsWithGivenList(prdRule.getPRDActions().getPRDAction());

        return new Rule(name, activation, actions);
    }

    /**
     * Converts the given PRDAction to Action
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @return an Action representation of PRDAction.
     */
    private Action PRDAction2Action(PRDAction prdAction) {
        Action ret = null;
        ExpressionConverterAndValidator expressionConverterAndValidator = new ExpressionConverterAndValidator(environmentProperties, entities);

        try {
            switch (ActionType.valueOf(prdAction.getType())) {
                case INCREASE:
                    ret = new IncreaseAction(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, prdAction.getBy()));
                case DECREASE:
                    ret = new DecreaseAction(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, prdAction.getBy()));
                case CALCULATION:
                    ret = getMulOrDiv(prdAction, expressionConverterAndValidator);
                case CONDITION:
                    ret = getSingleOrMultiple(prdAction, expressionConverterAndValidator);
                case SET:
                    ret = new SetAction(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction,prdAction.getValue()));
                case KILL:
                    ret = new KillAction(prdAction.getProperty(), prdAction.getEntity());
                case REPLACE:
                    break;
                case PROXIMITY:
                    break;
            }
        } catch (Exception e) {
            String err = String.format("\"%s\" is not a valid Action type.", prdAction.getType());
            throw new IllegalArgumentException(err);
        }
        return ret;
    }

    /**
     * Converts the given PRDAction to multiply or divide calculation action.
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @return a CalculationAction representation of the given PRDActivation.
     */
    private CalculationAction getMulOrDiv(PRDAction prdAction, ExpressionConverterAndValidator expressionConverterAndValidator){
        CalculationAction ret = null;
        PRDMultiply mul = prdAction.getPRDMultiply();
        PRDDivide div = prdAction.getPRDDivide();

        // Without loss of generality, if mul equals null - the calculation action is not a multiply action.
        if(mul != null){
            ret = new CalculationAction(prdAction.getProperty(),prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, mul.getArg1()),
                    expressionConverterAndValidator.analyzeAndGetValue(prdAction, mul.getArg2()), ClaculationType.MULTIPLY);
        } else if (div != null) {
            ret = new CalculationAction(prdAction.getProperty(),prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, div.getArg1()),
                    expressionConverterAndValidator.analyzeAndGetValue(prdAction, div.getArg2()), ClaculationType.DIVIDE);
        }
        else {
            // Throw exception.
        }
        return ret;
    }

    /**
     * Converts the given PRDAction to single or multiple condition action.
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @return an AbstractConditionAction representation of the given PRDActivation.
     */
    private AbstractConditionAction getSingleOrMultiple(PRDAction prdAction, ExpressionConverterAndValidator expressionConverterAndValidator){
        AbstractConditionAction ret = null;
        PRDCondition prdCondition = prdAction.getPRDCondition();
        ThenOrElse thenActions = null, elseActions = null;
        // Then and else objects are created in this method.
        getAndCreateThenOrElse(prdAction, thenActions, elseActions);

        if(prdCondition.getSingularity().equals("single")){
            ret = new SingleCondition(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, prdAction.getValue()), thenActions, elseActions, prdCondition.getOperator());
        } else if (prdCondition.getSingularity().equals("multiple")) {
            ret = new MultipleCondition(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, prdAction.getValue()), thenActions, elseActions, prdCondition.getLogical());
        }
        else {
            // Throw exception.
        }

        return ret;
    }

    /**
     * Converts the given PRDAction to Then and Else objects which contain a set of actions to invoke.
     * According to the XML file, if one of them has no actions to invoke, the object remains null.
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @param thenActions empty ThenOrElse object to be created.
     * @param elseActions empty ThenOrElse object to be created.
     */
    private void getAndCreateThenOrElse(PRDAction prdAction, ThenOrElse thenActions, ThenOrElse elseActions){
        // 'getThenOrElseActionSet' creates the Set of Actions for them both.
        // Because 'PRDThen' and 'PRDElse' are different objects, when we want to create the set for 'Then'
        // we send null for 'prdElse', same for Else.
        Set<Action> thenActionsSet = getThenOrElseActionSet(prdAction.getPRDThen(), null);
        Set<Action> elseActionsSet = getThenOrElseActionSet(null, prdAction.getPRDElse());

        if(!thenActionsSet.isEmpty()){
            thenActions = new ThenOrElse(thenActionsSet);
        }

        if(!elseActionsSet.isEmpty()){
            elseActions = new ThenOrElse(elseActionsSet);
        }
    }

    /**
     * Converts the given PRDThen or PRDElse to The set of actions to invoke.
     * For example: if prdThen equals null, the method creates the set from the prdElse.
     *
     * @param prdThen the given PRDThen generated from reading the XML file
     * @param prdElse the given PRDElse generated from reading the XML file
     * @return a Set of actions representation of the given PRDThen or PRDElse.
     */
    private Set<Action> getThenOrElseActionSet(PRDThen prdThen, PRDElse prdElse){
        Set<Action> ret = null;

        if(prdThen != null){
            ret = getActionsWithGivenList(prdThen.getPRDAction());
        } else if (prdElse != null) {
            ret = getActionsWithGivenList(prdElse.getPRDAction());
        }

        return ret;
    }


    /**
     * Converts the given PRDActivation to Activation
     *
     * @param prdActivation the given PRDAction generated from reading the XML file
     * @return an Activation representation of PRDActivation.
     */
    private Activation PRDActivation2Activation(PRDActivation prdActivation) {
        int ticks = prdActivation.getTicks();
        double probability = prdActivation.getProbability();

        return new Activation(ticks, probability);
    }

    private Set<EndingCondition> getEndingConditions(PRDTermination prdTermination){
        Set<EndingCondition> endingConditions = new HashSet<>();

        for (Object endingConditionObj : prdTermination.getPRDByTicksOrPRDBySecond()){
            if(endingConditionObj.getClass() == PRDByTicks.class){
                endingConditions.add(PRDByTicks2EndingCondition((PRDByTicks)endingConditionObj));
            }
            else{
                endingConditions.add(PRDBySecond2EndingCondition((PRDBySecond)endingConditionObj));
            }
        }

        return endingConditions;
    }

    private EndingCondition PRDByTicks2EndingCondition (PRDByTicks prdByTicks){
        return new EndingCondition(EndingConditionType.TICKS, prdByTicks.getCount());
    }

    private EndingCondition PRDBySecond2EndingCondition (PRDBySecond prdBySecond){
        return new EndingCondition(EndingConditionType.TIME, prdBySecond.getCount());
    }
}
