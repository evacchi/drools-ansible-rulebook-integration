package org.drools.ansible.rulebook.integration.api.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.drools.ansible.rulebook.integration.api.RuleNotation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RulesSet {
    private String name;
    private List<String> hosts;
    private List<RuleContainer> rules;

    private RuleNotation.RuleConfigurationOption[] options;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public List<Rule> getRules() {
        return rules.stream().map(RuleContainer::getRule).collect(Collectors.toList());
    }

    public void setRules(List<RuleContainer> rules) {
        this.rules = rules;
    }

    public RuleNotation.RuleConfigurationOption[] getOptions() {
        return options;
    }

    public RulesSet withOptions(RuleNotation.RuleConfigurationOption[] options) {
        this.options = options;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RulesSet rulesSet = (RulesSet) o;
        return Objects.equals(name, rulesSet.name) && Objects.equals(hosts, rulesSet.hosts) && Objects.equals(rules, rulesSet.rules) && Arrays.equals(options, rulesSet.options);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, hosts, rules);
        result = 31 * result + Arrays.hashCode(options);
        return result;
    }

    @Override
    public String toString() {
        return "RulesSet{" +
                "name='" + name + '\'' +
                ", hosts='" + hosts + '\'' +
                ", rules=" + rules +
                '}';
    }
}
