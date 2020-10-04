package com.fucct.tobispring.learning;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fucct.tobispring.dao.DaoFactory;
import com.fucct.tobispring.user.UserService;

@SpringJUnitConfig
@Import(DaoFactory.class)
public class TransactionTest {

    @Autowired PlatformTransactionManager transactionManager;

    @Autowired UserService userService;

    @Test
    void transactionSync() {
        final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setReadOnly(true);

        final TransactionStatus status = transactionManager.getTransaction(def);


        assertThatThrownBy(() -> {
            userService.deleteAll();
            transactionManager.commit(status);
        }).isInstanceOf(TransientDataAccessResourceException.class);

    }
}
