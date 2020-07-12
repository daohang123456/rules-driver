package org.syq.rule;

import org.syq.annotation.DriverAnnotation;
import org.syq.core.RuleDriver;


@DriverAnnotation(basePackages = {"org.syq.rule.buzz","org.syq.rule.monitor"})
public class Test {

    public static void main(String[] args) {
        RuleDriver ruleDriver = new RuleDriver(Test.class);
        ruleDriver.start();
    }


}

