package org.drools.ansible.rulebook.integration.api.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.drools.ansible.rulebook.integration.api.RuleConfigurationOption;
import org.drools.ansible.rulebook.integration.api.RuleConfigurationOptions;
import org.drools.ansible.rulebook.integration.api.RulesExecutionController;
import org.drools.ansible.rulebook.integration.api.domain.actions.Action;
import org.drools.ansible.rulebook.integration.api.domain.actions.MapAction;
import org.drools.ansible.rulebook.integration.api.domain.conditions.AstCondition;
import org.drools.ansible.rulebook.integration.api.domain.conditions.Condition;
import org.drools.ansible.rulebook.integration.api.domain.conditions.OnceWithinDefinition;
import org.drools.ansible.rulebook.integration.api.domain.conditions.TimeWindowDefinition;
import org.drools.ansible.rulebook.integration.api.domain.conditions.TimedOutDefinition;
import org.drools.model.Drools;
import org.drools.model.RuleItemBuilder;

import static org.drools.model.DSL.execute;
import static org.drools.model.DSL.on;
import static org.drools.model.PatternDSL.rule;

public class Rule {
    private String name;
    private Condition condition;
    private Action action;
    private boolean enabled;

    private String onceWithin;
    private List<String> uniqueAttributes;

    private final RuleGenerationContext ruleGenerationContext = new RuleGenerationContext();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Rule withOptions(RuleConfigurationOptions options) {
        this.ruleGenerationContext.addOptions(options.getOptions());
        return this;
    }

    public RuleGenerationContext getRuleGenerationContext() {
        return ruleGenerationContext;
    }

    public boolean hasTimeConstraint() {
        return ruleGenerationContext.hasTimeConstraint();
    }

    public AstCondition withCondition() {
        condition = new AstCondition(ruleGenerationContext);
        return (AstCondition) condition;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(MapAction action) {
        this.action = action;
    }

    public void setGenericAction(Action action) {
        this.action = action;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setOnce_within(String onceWithin) {
        this.onceWithin = onceWithin;
        if (uniqueAttributes != null) {
            ruleGenerationContext.setTimeConstraint(OnceWithinDefinition.parseOnceWithin(onceWithin, uniqueAttributes));
        }
    }

    public void setUnique_attributes(List<String> uniqueAttributes) {
        this.uniqueAttributes = uniqueAttributes;
        if (onceWithin != null) {
            ruleGenerationContext.setTimeConstraint(OnceWithinDefinition.parseOnceWithin(onceWithin, uniqueAttributes));
        }
    }

    public void setTime_window(String timeWindow) {
        ruleGenerationContext.setTimeConstraint(TimeWindowDefinition.parseTimeWindow(timeWindow));
    }

    public void setTimed_out(String timedOut) {
        ruleGenerationContext.setTimeConstraint(TimedOutDefinition.parseTimedOut(timedOut));
    }

    @Override
    public String toString() {
        return "Rule{" +
                "name='" + name + '\'' +
                ", condition='" + condition + '\'' +
                ", action='" + action + '\'' +
                '}';
    }

    List<org.drools.model.Rule> toExecModelRules(RulesSet rulesSet, RulesExecutionController rulesExecutionController, AtomicInteger ruleCounter) {
        if (name == null) {
            name = "r_" + ruleCounter.getAndIncrement();
        }
        ruleGenerationContext.setRuleName(name);

        List<org.drools.model.Rule> rules = ruleGenerationContext.generateRules(rulesExecutionController, condition, action);
        if (ruleGenerationContext.hasTimeConstraint()) {
            rulesSet.withOptions(RuleConfigurationOption.EVENTS_PROCESSING);
        }
        return rules;
    }
}
