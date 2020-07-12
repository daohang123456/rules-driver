package org.syq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cage on 2020-07-12
 */

@Inherited
@Retention(RetentionPolicy.RUNTIME) // 永久保存
@Target(ElementType.METHOD) //作用于方法
public @interface ConditionAnnotation {

}
