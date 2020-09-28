package com.fucct.tobispring.user;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {
    User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void upgradeLevel() {
        final Level[] levels = Level.values();

        for (Level level : levels) {
            if (level.nextLevel() == null) {
                continue;
            }
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(level.nextLevel());
        }
    }

    @Test
    void cannotUpgradeLevel() {
        final Level[] levels = Level.values();

        for (Level level : levels) {
            if (level.nextLevel() == null) {
                user.setLevel(level);
                assertThatThrownBy(() -> user.upgradeLevel())
                    .isInstanceOf(IllegalStateException.class);
            }
        }
    }
}
