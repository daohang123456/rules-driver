package org.syq.core;

import org.syq.annotation.group.RuleGroupAnnotation;
import org.syq.bean.BasicGroupRule;
import org.syq.bean.RuleFunctionProxy;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by cage on 2020-07-12
 */
public class Rules {

    // key: name value: ruleGroup
    private static Map<String, BasicGroupRule> groupRulesMap = new HashMap<>();

    private Rules() {

    }

    public void addRules(Object... rules) {
        Objects.requireNonNull(rules);
        for (Object rule : rules) {
            Objects.requireNonNull(rule);
            RuleFunction transfer = transfer(rule);
            putRule(transfer);
        }
    }

    public void removeRules(Object... rules) {
        Objects.requireNonNull(rules);
        for (Object rule : rules) {
            Objects.requireNonNull(rule);
            RuleFunction transfer = transfer(rule);
            removeRule(transfer);
        }
    }

    public RuleFunction transfer(Object rule) {
        RuleFunction ruleFunction = null;
        if (rule instanceof RuleFunction) {
            ruleFunction = (RuleFunction) rule;
        } else {
            if (isValidateRuleDefinition(rule)) {
                ruleFunction = (RuleFunction) Proxy.newProxyInstance(
                        RuleFunction.class.getClassLoader(),
                        new Class[]{RuleFunction.class, Comparable.class},
                        new RuleFunctionProxy(rule));
            }
        }
        return ruleFunction;
    }

    public void putRule(RuleFunction ruleFunction) {
        Objects.requireNonNull(ruleFunction);
        String group = ruleFunction.getGroup();
        if (groupRulesMap.containsKey(group)) {
            BasicGroupRule basicGroupRule = groupRulesMap.get(group);
            basicGroupRule.addRuleToGroup(ruleFunction);
        } else {
            RuleGroupAnnotation groupAnnotation = ruleFunction.getClass().getAnnotation(RuleGroupAnnotation.class);
            String name = groupAnnotation.name();
            String type = groupAnnotation.type();
            String description = groupAnnotation.description();
            BasicGroupRule basicGroupRule = new BasicGroupRule(name, type, description, ruleFunction);
            groupRulesMap.put(name, basicGroupRule);
        }
    }

    public void removeRule(RuleFunction ruleFunction) {
        Objects.requireNonNull(ruleFunction);
        String group = ruleFunction.getGroup();
        if (groupRulesMap.containsKey(group)) {
            BasicGroupRule basicGroupRule = groupRulesMap.get(group);
            basicGroupRule.removeRuleToGroup(ruleFunction);
        }
    }

    private boolean isValidateRuleDefinition(Object rule) {
        return true;
    }

}
