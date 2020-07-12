package org.syq.annotation.group;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by cage on 2020-07-12
 */
public class RuleGroupType {

    public static final String ExecuteSequentialGroup = "ExecuteSequential";
    public static final String ExitOnFailureGroup = "ExitOnFailure";
    public static final String SkipOnFailureGroup = "SkipOnFailure";

    private static Set<String> groupTypes = new HashSet<>();
    static {
        groupTypes.add(ExecuteSequentialGroup);
        groupTypes.add(ExitOnFailureGroup);
        groupTypes.add(SkipOnFailureGroup);
    }

    public static boolean isValidGroup(String groupType) {
        return groupTypes.contains(groupType);
    }
}
