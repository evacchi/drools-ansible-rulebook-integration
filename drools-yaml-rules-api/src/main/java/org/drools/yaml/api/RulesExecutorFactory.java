package org.drools.yaml.api;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import org.drools.model.impl.ModelImpl;
import org.drools.model.view.ViewItem;
import org.drools.modelcompiler.KieBaseBuilder;
import org.drools.yaml.api.domain.Rule;
import org.drools.yaml.api.domain.RulesSet;
import org.drools.yaml.api.rulesmodel.PrototypeFactory;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;

import static org.drools.model.DSL.execute;
import static org.drools.model.PatternDSL.rule;

public class RulesExecutorFactory {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public static RulesExecutor createFromYaml(String yaml) {
        return createFromYaml(RuleNotation.CoreNotation.INSTANCE, yaml);
    }

    public static RulesExecutor createFromYaml(RuleNotation notation, String yaml) {
        return create(RuleFormat.YAML, notation, yaml);
    }

    public static RulesExecutor createFromJson(String json) {
        return createFromJson(RuleNotation.CoreNotation.INSTANCE, json);
    }

    public static RulesExecutor createFromJson(RuleNotation notation, String json) {
        return create(RuleFormat.JSON, notation, json);
    }

    private static RulesExecutor create(RuleFormat format, RuleNotation notation, String text) {
        return createRulesExecutor(notation.toRulesSet(format, text));
    }

    public static RulesExecutor createRulesExecutor(RulesSet rulesSet) {
        RulesExecutor rulesExecutor = new RulesExecutor(createRulesExecutorSession(rulesSet), ID_GENERATOR.getAndIncrement());
        RulesExecutorContainer.INSTANCE.register(rulesExecutor);
        return rulesExecutor;
    }

    public static RulesExecutorSession createRulesExecutorSession(RulesSet rulesSet) {
        PrototypeFactory prototypeFactory = new PrototypeFactory();
        RulesExecutorSession.RulesExecutorHolder rulesExecutorHolder = new RulesExecutorSession.RulesExecutorHolder();
        AtomicInteger ruleCounter = new AtomicInteger(0);

        ModelImpl model = new ModelImpl();
        rulesSet.getRules().stream()
                .map(rule -> toExecModelRule(rulesSet, rule, prototypeFactory, rulesExecutorHolder, ruleCounter))
                .forEach(model::addRule);
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );
        KieSession kieSession = kieBase.newKieSession();

        return new RulesExecutorSession(prototypeFactory, kieSession, rulesExecutorHolder);
    }

    private static org.drools.model.Rule toExecModelRule(RulesSet rulesSet, Rule rule, PrototypeFactory prototypeFactory, Supplier<RulesExecutor> rulesExecutorSupplier, AtomicInteger ruleCounter) {
        String ruleName = rule.getName();
        if (ruleName == null) {
            ruleName = "r_" + ruleCounter.getAndIncrement();
        }

        RuleGenerationContext ruleContext = new RuleGenerationContext(prototypeFactory, rulesSet.getOptions());
        ViewItem pattern = rule.getCondition().toPattern(ruleContext);
        var consequence = execute(drools -> rule.getAction().execute(rulesExecutorSupplier.get(), drools));

        return rule( ruleName ).build(pattern, consequence);
    }
}
