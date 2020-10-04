package com.fucct.tobispring.learning;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

public class PointcutTest {
    @Test
    void methodSignaturePointcut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(
            "execution(public int com.fucct.tobispring.learning.Target.minus(int, int) throws java.lang.RuntimeException)");

        assertThat(pointcut.getClassFilter().matches(Target.class)).isTrue();
        assertThat(
            pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class), null)).isTrue();
    }
}
