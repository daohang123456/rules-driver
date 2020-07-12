package org.syq.core;

import org.syq.bean.RuleEnvironments;

/**
 * Created by cage on 2020-07-12
 */
public interface RuleFunction extends Comparable<RuleFunction> {

    String DEFAULT_RULE_GROUP = "DEFAULT_RULE_GROUP";

    String DEFAULT_RULE_NAME = "DEFAULT_RULE_NAME";

    String DEFAULT_RULE_DESCRIPTION = "DEFAULT_RULE_DESCRIPTION";

    int DEFAULT_RULE_ORDER = 0;

    boolean isApply(RuleEnvironments environment);

    void doAction(RuleEnvironments environment);

    default String getName() {
        return DEFAULT_RULE_NAME;
    }

    default String getDescription() {
        return DEFAULT_RULE_DESCRIPTION;
    }

    default int getOrder() {
        return DEFAULT_RULE_ORDER;
    }

    default String getGroup() {
        return DEFAULT_RULE_GROUP;
    }

}
