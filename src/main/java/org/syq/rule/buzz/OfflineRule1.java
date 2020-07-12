package org.syq.rule.buzz;

import org.syq.annotation.ActionAnnotation;
import org.syq.annotation.RuleAnnotation;
import org.syq.annotation.group.RuleGroupAnnotation;
import org.syq.annotation.group.RuleGroupType;

/**
 * Created by cage on 2020-07-12
 */

@RuleAnnotation(name = "offline_rule1", order = 1)
@RuleGroupAnnotation(type = RuleGroupType.ExecuteSequentialGroup, name = "offline_rule_group")
public class OfflineRule1 {

    @ActionAnnotation(order = 1)
    public void action1() {
        System.out.println("offline_rule1 --> action1");
    }

    @ActionAnnotation(order = 2)
    public void action2() {
        System.out.println("offline_rule1 --> action2");
    }

}
