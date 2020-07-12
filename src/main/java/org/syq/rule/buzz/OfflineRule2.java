package org.syq.rule.buzz;

import org.syq.annotation.ActionAnnotation;
import org.syq.annotation.RuleAnnotation;
import org.syq.annotation.group.ExecuteSequentialRuleGroup;

/**
 * Created by cage on 2020-07-12
 */

@RuleAnnotation(name = "offline_rule2", order = 2)
@ExecuteSequentialRuleGroup(name = "offline_group")
public class OfflineRule2 {

    @ActionAnnotation(order = 1)
    public void action1() {
        System.out.println("offline_rule2 --> action1");
    }

    @ActionAnnotation(order = 2)
    public void action2() {
        System.out.println("offline_rule2 --> action2");
    }

}
