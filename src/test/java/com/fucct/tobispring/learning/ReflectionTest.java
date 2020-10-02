package com.fucct.tobispring.learning;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.jupiter.api.Test;

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
}
