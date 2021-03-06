package com.fucct.tobispring.learning;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import sun.jvm.hotspot.HelloWorld;

public class ReflectionTest {

    @Test
    void length() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method lengthMethod = String.class.getMethod("length");
        assertThat(lengthMethod.invoke("String Length")).isEqualTo(13);
    }

    @Test
    void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("dd")).isEqualTo("Hello, dd");

        Hello helloProxy = new HelloUpperCase(hello);

        assertThat(helloProxy.sayHello("dd")).isEqualTo("Hello, DD");
    }

    @Test
    void dynamicProxy() {
        final Hello proxyHello = (Hello)Proxy.newProxyInstance(getClass().getClassLoader(),
            new Class[] {Hello.class}, new UppercaseHandler(new HelloTarget()));
        assertThat(proxyHello.sayHello("dd")).isEqualTo("HELLO, DD");
    }

    @Test
    void proxyFactoryBean() {
        final ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());
        Hello proxiedHello = (Hello)pfBean.getObject();
        assertThat(proxiedHello.sayHello("dd")).isEqualTo("HELLO, DD");
    }

    @Test
    void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        final Hello proxiedHello = (Hello)pfBean.getObject();

        assertThat(proxiedHello.sayHello("dd")).isEqualTo("HELLO, DD");
        assertThat(proxiedHello.sayHi("dd")).isEqualTo("HI, DD");
        assertThat(proxiedHello.sayThankYou("dd")).isEqualTo("Thank you, dd");
    }

    @Test
    void classNamePointcutAdvisor() {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut(){
            @Override
            public ClassFilter getClassFilter() {
                return clazz -> clazz.getSimpleName().startsWith("HelloT");
            }
        };
        classMethodPointcut.setMappedName("sayH*");

        class HelloWorld extends HelloTarget{}
        class HelloToby extends HelloTarget{}
        checkAdviced(new HelloTarget(), classMethodPointcut, true);
        checkAdviced(new HelloWorld(), classMethodPointcut, false);
        checkAdviced(new HelloToby(), classMethodPointcut, true);

    }

    private void checkAdviced(final Object target, final NameMatchMethodPointcut pointcut, final boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        Hello proxiedHello = (Hello)pfBean.getObject();

        if (adviced) {
            assertThat(proxiedHello.sayHello("dd")).isEqualTo("HELLO, DD");
            assertThat(proxiedHello.sayHi("dd")).isEqualTo("HI, DD");
            assertThat(proxiedHello.sayThankYou("dd")).isEqualTo("Thank you, dd");
        } else {
            assertThat(proxiedHello.sayHello("dd")).isEqualTo("Hello, dd");
            assertThat(proxiedHello.sayHi("dd")).isEqualTo("Hi, dd");
            assertThat(proxiedHello.sayThankYou("dd")).isEqualTo("Thank you, dd");
        }
    }
}
