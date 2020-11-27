package ua.selftaught;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("String calculator tests. These tests are test expressions like '2+3/2*6' and so on..")
public class StringCalculatorTest {

    @DisplayName("Empty string should return 0.0")
    @Test
    void testEmptyStringShouldReturn0() {

        Double actual = StringCalculator.evaluate("");
        Assertions.assertEquals(0.0, actual, () -> "Should be 0.0");

    }

    @DisplayName("Expression with a number")
    @Test
    void testExpressionWithANumber() {
        Double actual = StringCalculator.evaluate("23.2");

        Assertions.assertEquals(23.2, actual, () -> "Should be equal 23.2");
    }

}

class StringCalculator {

    public static final Double ZERO = 0.0;

    public static Double evaluate(String expr) {
        if (expr.isEmpty()) return ZERO;
        return Double.parseDouble(expr);
    }
}

