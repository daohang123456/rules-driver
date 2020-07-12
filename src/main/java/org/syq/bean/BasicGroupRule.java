package org.syq.bean;

import org.syq.core.RuleFunction;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by cage on 2020-07-12
 */
public class BasicGroupRule {

    private String groupName;
    private String groupType;
    private String groupDesc;

    private Map<Integer, RuleFunction> rules;

    public BasicGroupRule() {
        rules = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public BasicGroupRule(String groupName, String groupType, String groupDesc) {
        this.groupName = groupName;
        this.groupType = groupType;
        this.groupDesc = groupDesc;
    }

    public BasicGroupRule(String groupName, String groupType, String groupDesc, RuleFunction ruleFunction) {
        this.groupName = groupName;
        this.groupType = groupType;
        this.groupDesc = groupDesc;
        addRuleToGroup(ruleFunction);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public void addRuleToGroup(RuleFunction ruleFunction) {
        rules.put(ruleFunction.getOrder(), ruleFunction);
    }

    public void removeRuleToGroup(RuleFunction ruleFunction) {
        rules.remove(ruleFunction.getOrder());
    }

    public int size() {
        return rules.size();
    }

    @Override
    public String toString() {
        String toString = "";
        Iterator<Map.Entry<Integer, RuleFunction>> iterator = rules.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, RuleFunction> entry = iterator.next();
            toString += "[order: " + entry.getValue().getOrder() + ", name: " + entry.getValue().getName() + "]";
            if (iterator.hasNext()) {
                toString = ",";
            }
        }
        return toString;
    }
}
