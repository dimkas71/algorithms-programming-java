package ua.selftaught;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@DisplayName("String calculator tests. These tests are test expressions like '2+3/2*6' and so on..")
public class StringCalculatorTest {

    @DisplayName("Empty string should return 0.0")
    @Test
    void testEmptyStringShouldReturn0() {

        Double actual = StringCalculator.evaluate("");
        Assertions.assertEquals(0.0, actual, () -> "Should be 0.0");

    }

    @DisplayName("Expression with a number '23.2' should return double value 23.2")
    @Test
    void testExpressionWithANumber() {
        Double actual = StringCalculator.evaluate("23.2");

        Assertions.assertEquals(23.2, actual, () -> "Should be equal 23.2");
    }

    @DisplayName("If chars are not supported StringCalculatorException should be thrown")
    @Test
    void testSupportedCharactersInExpression() {
        StringCalculatorException exception = Assertions.assertThrows(StringCalculatorException.class, () -> StringCalculator.evaluate("23.2 +$"));

        Assertions.assertEquals("An expression contains unsupported characters", exception.getMessage());

    }

    @DisplayName("Evaluate expression '23.2+2.5' should be equal to 25.7")
    @Test
    void testEvaluationAddOperation() {
        Double actual = StringCalculator.evaluate("23.2+2.5");
        Assertions.assertEquals(25.7, actual, () -> "23.2 + 2.5 => 25.7");
    }

    @DisplayName("Evaluate expression '23.2-2.2' should be equal to 21.0")
    @Test
    void testEvaluationSubOperation() {
        Double actual = StringCalculator.evaluate("23.2-2.2");
        Assertions.assertEquals(21.0, actual, () -> "23.2-2.2 => 21.0");
    }

    @DisplayName("Evaluate expression '23.2*2' should be equal to 46.4")
    @Test
    void testEvaluationMulOperation() {
        Double actual = StringCalculator.evaluate("23.2*2");
        Assertions.assertEquals(46.4, actual, () -> "23.2*2 => 46.4");
    }

    @DisplayName("Evaluate expression '24.2/2' should be equal to 12.1")
    @Test
    void testEvaluationDivOperation() {
        Double actual = StringCalculator.evaluate("24.2/2");
        Assertions.assertEquals(12.1, actual, () -> "24.2/2 => 12.1");
    }

    @DisplayName("Test for multiple expressions")
    @ParameterizedTest(name = "Test #{index} = Expr {0} => {1}")
    @MethodSource("stringDoubleProvider")
    void testWithMultiArgMethodSource(String expr, Double expected) {

    }

    static Stream<Arguments> stringDoubleProvider() {
        return Stream.of(
                Arguments.arguments("22.2/2*2-0.2", 22.0),
                Arguments.arguments("22.2+2-4/2", 22.0),
                Arguments.arguments("1.5*2-2:2+2", 4.0),
                Arguments.arguments("0.2", 0.2),
                Arguments.arguments("2+2+2-3-4:2*4", -5.0)
        );


    }



}

class StringCalculatorException extends RuntimeException {
    public StringCalculatorException(String message) {
        super(message);
    }
}

class StringCalculator {

    private static final Pattern DOUBLE_NUMBER_PATTERN = Pattern.compile("(?<number>[0-9]{0,}\\.{0,1}[0-9]{0,})");

    private static final Pattern BINARY_OPERATION_ADD_PATTERN = Pattern.compile(
                "(?<operand1>[0-9]{0,}\\.{0,1}[0-9]{0,})(?<operation>\\+{1})(?<operand2>[0-9]{0,}\\.{0,1}[0-9]{0,})"
            );

    private static final Pattern BINARY_OPERATION_SUB_PATTERN = Pattern.compile(
                    "(?<operand1>[0-9]{0,}\\.{0,1}[0-9]{0,})(?<operation>\\-{1})(?<operand2>[0-9]{0,}\\.{0,1}[0-9]{0,})"
                );

    private static final Pattern BINARY_OPERATION_MUL_PATTERN = Pattern.compile(
                    "(?<operand1>[0-9]{0,}\\.{0,1}[0-9]{0,})(?<operation>\\*{1})(?<operand2>[0-9]{0,}\\.{0,1}[0-9]{0,})"
                );

    private static final Pattern BINARY_OPERATION_DIV_PATTERN = Pattern.compile(
                        "(?<operand1>[0-9]{0,}\\.{0,1}[0-9]{0,})(?<operation>\\/{1})(?<operand2>[0-9]{0,}\\.{0,1}[0-9]{0,})"
                    );


    private static final Double ZERO = 0.0;

    public static Double evaluate(String expr) {
        checkExpression(expr);
        if (expr.isEmpty()) return ZERO;


        //1. Match the div operation
        Matcher m = BINARY_OPERATION_DIV_PATTERN.matcher(expr);
        if (m.find()) {

            double left = Double.parseDouble(m.group("operand1"));
            double right = Double.parseDouble(m.group("operand2"));

            String replacement = expr.substring(m.start(), m.end());
            return evaluate(expr.replace(replacement, String.valueOf(left / right)));
        }

        //2. Match the mull operation
        m = BINARY_OPERATION_MUL_PATTERN.matcher(expr);
        if (m.find()) {

            double left = Double.parseDouble(m.group("operand1"));
            double right = Double.parseDouble(m.group("operand2"));

            String replacement = expr.substring(m.start(), m.end());
            return evaluate(expr.replace(replacement, String.valueOf(left * right)));
        }

        //3. Match the add operation
        m = BINARY_OPERATION_ADD_PATTERN.matcher(expr);
        if (m.find()) {

            double left = Double.parseDouble(m.group("operand1"));
            double right = Double.parseDouble(m.group("operand2"));

            String replacement = expr.substring(m.start(), m.end());
            return evaluate(expr.replace(replacement, String.valueOf(left + right)));
        }
        //4. Match the sub operation
        m = BINARY_OPERATION_SUB_PATTERN.matcher(expr);
        if (m.find()) {

            double left = Double.parseDouble(m.group("operand1"));
            double right = Double.parseDouble(m.group("operand2"));

            String replacement = expr.substring(m.start(), m.end());
            return evaluate(expr.replace(replacement, String.valueOf(left - right)));
        }



        //5. Match the number
        m = DOUBLE_NUMBER_PATTERN.matcher(expr);
        if (m.find()) {
            return Double.parseDouble(m.group("number"));
        }
        return Double.parseDouble(expr);
    }

    private static void checkExpression(String expr) {
        final String supportedCharacters = "0123456789.+-*/";

        long count = expr.chars()
                    .mapToObj(c -> (char)c)
                    .filter(c -> !supportedCharacters.contains(String.valueOf(c)))
                    .count();

        if (count > 0L) {
            throw new StringCalculatorException("An expression contains unsupported characters");
        }

    }
}

