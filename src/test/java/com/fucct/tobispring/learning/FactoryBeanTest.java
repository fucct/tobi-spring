package com.fucct.tobispring.learning;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.fucct.tobispring.dao.DaoFactory;
import com.fucct.tobispring.user.Message;
import com.fucct.tobispring.user.MessageFactoryBean;

@SpringJUnitConfig
@Import(DaoFactory.class)
public class FactoryBeanTest {
    @Autowired ApplicationContext context;

    @Test
    void factory() {
        final Object message = context.getBean("message");
        final Object messageFactory = context.getBean("&message");
        assertThat(message.getClass()).isEqualTo(Message.class);
        assertThat(messageFactory.getClass()).isEqualTo(MessageFactoryBean.class);
        assertThat(((Message)message).getText()).isEqualTo("Factory Bean");
    }
}
