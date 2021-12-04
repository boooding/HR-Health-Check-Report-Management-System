package top.chenzhimeng.hr_health_check.model.po;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author M
 * @date 2021/06/09
 **/
class QuestionnaireQuestionTest {

    @ParameterizedTest
    @MethodSource("options2MapStringSource")
    void options2MapString(List<String> options, String mapString) {
        assertEquals(QuestionnaireQuestion.options2MapString(options), mapString);
    }

    private static List<Arguments> options2MapStringSource() {
        return List.of(
                Arguments.of(List.of(), "{}"),
                Arguments.of(List.of("test1_option0"), "{0:\"test1_option0\"}"),
                Arguments.of(List.of("test2_option0", "test2_option1"), "{0:\"test2_option0\",1:\"test2_option1\"}"),
                Arguments.of(null, null)
        );
    }
}