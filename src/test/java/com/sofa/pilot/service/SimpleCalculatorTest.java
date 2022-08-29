package com.sofa.pilot.service;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCalculatorTest {


    @Test
    void givenSimpleCalculatorWhenInitializedThenThrowUnsupportedOperationException(){
        final String expectedErrorMessage = "Class cannot be initialized";
        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            Constructor<SimpleCalculator> constructor = SimpleCalculator.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof UnsupportedOperationException);
        assertSame(expectedErrorMessage, cause.getMessage());
    }


    @Test
    void givenAddendsWhenSimpleCalculatorAddIsExecutedThenSumIsReturned(){
        final int firstAddend = 15;
        final int secondAddend = 23;
        final int expectedResult = 38;

        int sum = SimpleCalculator.add(firstAddend, secondAddend);

        assertEquals(expectedResult, sum);
    }
}
