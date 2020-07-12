package org.syq.bean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Created by cage on 2020-07-12
 */

public class RuleEnvironments implements Iterable<Map.Entry<String, RuleEnvironment<?>>> {

    private Map<String, RuleEnvironment<?>> ruleEnvironmentMap = new HashMap();

    public int size() {
        return ruleEnvironmentMap.size();
    }

    public void addRuleEnvironment(RuleEnvironment<?> ruleEnvironment) {
        Objects.requireNonNull(ruleEnvironment, "RuleEnvironment can not be null!");
        ruleEnvironmentMap.put(ruleEnvironment.getName(), ruleEnvironment);
    }

    public <V> void addRuleEnvironment(String name, V value) {
        Objects.requireNonNull(name, "RuleEnvironment's name can not be null!");
        Objects.requireNonNull(value, "RuleEnvironment's value can not be null!");
        ruleEnvironmentMap.put(name, new RuleEnvironment<V>(name, value));
    }

    public void removeRuleEnvironment(RuleEnvironment<?> ruleEnvironment) {
        ruleEnvironmentMap.remove(ruleEnvironment.getName());
    }

    public void removeRuleEnvironment(String name) {
        Objects.requireNonNull(name, "RuleEnvironment's name can not be null!");
        ruleEnvironmentMap.remove(name);
    }

    public void clear() {
        ruleEnvironmentMap.clear();
    }

    public RuleEnvironment<?> getRuleEnvironment(String name) {
        return ruleEnvironmentMap.get(name);
    }

    @Override
    public Iterator<Map.Entry<String, RuleEnvironment<?>>> iterator() {
        return ruleEnvironmentMap.entrySet().iterator();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        while (iterator().hasNext()) {
            Map.Entry<String, RuleEnvironment<?>> next = iterator().next();
            String objStr = next.getValue().toString();
            stringBuilder.append(objStr);
            if (iterator().hasNext()) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }
}
