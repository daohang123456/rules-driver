package org.syq.rule.monitor;

import org.syq.annotation.ActionAnnotation;
import org.syq.annotation.RuleAnnotation;

/**
 * Created by cage on 2020-07-12
 */

@RuleAnnotation(name = "online_rule1", order = 1)
public class OnlineRule1 {

    @ActionAnnotation(order = 1)
    public void action1() {
        System.out.println("online_rule1 --> action1");
    }

    @ActionAnnotation(order = 2)
    public void action2() {
        System.out.println("online_rule1 --> action2");
    }


}
