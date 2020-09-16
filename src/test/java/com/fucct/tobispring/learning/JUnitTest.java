package com.fucct.tobispring.learning;

import static org.assertj.core.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.fucct.tobispring.dao.DaoFactory;

@SpringJUnitConfig
@Import(DaoFactory.class)
public class JUnitTest {
    static Set<JUnitTest> jUnitTest = new HashSet<>();

    @Autowired
    ApplicationContext context;

    static ApplicationContext contextObject = null;

    @Test
    void test1() {
        assertThat(jUnitTest).doesNotContain(this);
        jUnitTest.add(this);
    }

    @Test
    void test2() {
        assertThat(jUnitTest).doesNotContain(this);
        jUnitTest.add(this);
    }

    @Test
    void test3() {
        assertThat(jUnitTest).doesNotContain(this);
        jUnitTest.add(this);
    }

    @Test
    void test4() {
        assertThat(contextObject == null || contextObject == this.context).isTrue();
        contextObject = this.context;
    }

    @Test
    void test5() {
        assertThat(contextObject == null || contextObject == this.context).isTrue();
        contextObject = this.context;
    }

    @Test
    void test6() {
        assertThat(contextObject == null || contextObject == this.context).isTrue();
        contextObject = this.context;
    }
}
