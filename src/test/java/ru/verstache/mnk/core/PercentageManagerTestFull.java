package ru.verstache.mnk.core;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.verstache.mnk.manager.PercentageManager;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

public class PercentageManagerTestFull {

    private PercentageManager uut;

    @BeforeEach
    public void init() {
        uut = new PercentageManager();;
    }

    @ParameterizedTest
    @MethodSource("percents_4x4")
    public void shouldComputeWinningPercentage_when4x4_and_3lineStreak(Field field, int strokeCells) {
        Assertions.assertEquals(String.format("%.2f", strokeCells / 16.0 * 100), uut.countWinningPercentage(field));
    }


    public static Stream<Arguments> percents_4x4() {
        File dir = new File("src/test/resources/field_states/3_lines_percentage/4x4");
        File[] files = dir.listFiles();
        assert files != null;

        return Arrays.stream(files)
                .map(file -> Arguments.of(StruckManagerTestUtils.mapToField(file), StruckManagerTestUtils.countStrokeCells(file)));
    }

    @ParameterizedTest
    @MethodSource("percents_for_3_line_streak_5x5")
    public void shouldComputeWinningPercentage_when5x5_and_3lineStreak(Double expectedPercentage, Field field) {
        Assertions.assertEquals(String.format("%.2f", expectedPercentage * 100), uut.countWinningPercentage(field, 3));
    }


    public static Stream<Arguments> percents_for_3_line_streak_5x5() {
        File dir = new File("src/test/resources/field_states/3_lines_percentage/5x5");
        File[] files = dir.listFiles();
        assert files != null;

        return Arrays.stream(files)
                .map(file -> Arguments.of(extractExpectedPercentageFromNameByParts(file), StruckManagerTestUtils.mapToField(file)));
    }

    @ParameterizedTest
    @MethodSource("percents_for_3_line_streak_nonSquare")
    public void shouldComputeWinningPercentage_whenNonSquare_and_3lineStreak(Double expectedPercentage, Field field) {
        Assertions.assertEquals(String.format("%.2f", expectedPercentage * 100), uut.countWinningPercentage(field, 3));
    }


    public static Stream<Arguments> percents_for_3_line_streak_nonSquare() {
        File dir = new File("src/test/resources/field_states/3_lines_percentage/horizontal");
        File[] files = dir.listFiles();
        assert files != null;

        return Arrays.stream(files)
                .map(file -> Arguments.of(extractExpectedPercentageFromNameByParts(file), StruckManagerTestUtils.mapToField(file)));
    }

    private static Double extractExpectedPercentageFromNameByParts(File file) {
        String[] parts = file.getName().split("_of_");
        int denominator = parts[1].contains("_")
                ? Integer.parseInt(StringUtils.substringBefore(parts[1], "_"))
                : Integer.parseInt(StringUtils.substringBefore(parts[1], ".csv"));
        return Double.parseDouble(parts[0]) / denominator;
    }
}
