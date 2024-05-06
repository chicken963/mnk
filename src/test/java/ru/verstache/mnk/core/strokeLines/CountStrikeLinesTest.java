package ru.verstache.mnk.core.strokeLines;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.verstache.mnk.core.Field;
import ru.verstache.mnk.core.StruckManagerTestUtils;
import ru.verstache.mnk.manager.StrokeLinesManager;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ru.verstache.mnk.core.StruckManagerTestUtils.getExpectedNumberOfStruckLines;

public class CountStrikeLinesTest {

    private StrokeLinesManager uut;

    @BeforeEach
    public void init() {
        uut = new StrokeLinesManager();
    }


    @ParameterizedTest
    @MethodSource("fields4x4")
    public void shouldCountStruckLines_4x4_partial(Field field, int expectedNumberOfLines) {
        assertEquals(expectedNumberOfLines, uut.countStruckLines(field));
    }

    private static Stream<Arguments> fields4x4() {
        File dir = new File("src/test/resources/field_states/4x4");
        File[] files = dir.listFiles();
        assert files != null;

        Map<Integer, Field> testFields = Arrays.stream(files)
                .collect(Collectors.toMap(
                        StruckManagerTestUtils::getExpectedNumberOfStruckLines,
                        StruckManagerTestUtils::mapToField));

        return testFields.entrySet()
                .stream()
                .map(entry -> Arguments.of(entry.getValue(), entry.getKey()));
    }

    @ParameterizedTest
    @MethodSource("fields5x5")
    public void shouldCountStruckLines_5x5_partial(Field field, int expectedNumberOfLines) {

        assertEquals(expectedNumberOfLines, uut.countStruckLines(field));
    }

    private static Stream<Arguments> fields5x5() {
        File dir = new File("src/test/resources/field_states/5x5");
        File[] files = dir.listFiles();
        assert files != null;

        Map<Integer, Field> testFields = Arrays.stream(files)
                .collect(Collectors.toMap(
                        StruckManagerTestUtils::getExpectedNumberOfStruckLines,
                        StruckManagerTestUtils::mapToField));

        return testFields.entrySet()
                .stream()
                .map(entry -> Arguments.of(entry.getValue(), entry.getKey()));
    }

    @ParameterizedTest
    @MethodSource("horizontal")
    public void shouldCountStruckLines_horizontal_partial(Field field,
                                                          int expectedTotalNumberOfLines) {

        assertEquals(expectedTotalNumberOfLines, uut.countStruckLines(field));
    }

    @ParameterizedTest
    @MethodSource("vertical")
    public void shouldCountStruckLines_vertical_partial(Field field,
                                                        int expectedTotalNumberOfLines) {

        assertEquals(expectedTotalNumberOfLines, uut.countStruckLines(field));
    }

    @ParameterizedTest
    @MethodSource("fieldsFull")
    public void shouldCountFullStruck(Field field) {
        assertEquals(field.getWidth() + field.getHeight() + field.countDiagonals(), uut.countStruckLines(field));
    }

    private static Stream<Arguments> horizontal() {
        return getArgumentsStreamFromDirectory("horizontal");
    }

    private static Stream<Arguments> vertical() {
        return getArgumentsStreamFromDirectory("vertical");
    }

    private static Stream<Arguments> getArgumentsStreamFromDirectory(String dir) {
        return StruckManagerTestUtils.getInnerFilesRecursively(dir).stream()
                .collect(Collectors.toMap(File::getPath, StruckManagerTestUtils::mapToField))
                .entrySet().stream()
                .map(entry -> Arguments.of(
                        entry.getValue(),
                        getExpectedNumberOfStruckLines(entry.getKey())
                ));
    }

    private static Stream<Arguments> fieldsFull() {
        File dir = new File("src/test/resources/field_states/full");
        File[] files = dir.listFiles();
        assert files != null;

        return Arrays.stream(files)
                .map(StruckManagerTestUtils::mapToField)
                .map(Arguments::of);
    }

}
