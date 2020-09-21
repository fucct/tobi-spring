package com.fucct.tobispring.learning;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalcSumTest {
    Calculator calculator;
    String path;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
        path = getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    void name() throws IOException {
        int sum = calculator.calcSum(path);
        assertThat(sum).isEqualTo(10);
    }

    @Test
    void multi() throws IOException {
        int sum = calculator.calcMultiply(path);
        assertThat(sum).isEqualTo(24);
    }

    @Test
    void conCat() throws IOException {
        String sum = calculator.conCat(path);
        assertThat(sum).isEqualTo("1234");
    }
}
