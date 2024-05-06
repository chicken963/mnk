package ru.verstache.mnk.core.strokeLines;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.verstache.mnk.core.Cell;
import ru.verstache.mnk.core.Field;
import ru.verstache.mnk.core.StruckManagerTestUtils;
import ru.verstache.mnk.manager.StrokeLinesManager;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountStrikeLinesWithNextTest {

    private StrokeLinesManager uut;

    @BeforeEach
    public void init() {
        uut = new StrokeLinesManager();;
    }

    @ParameterizedTest
    @MethodSource("fields4x4_withNext")
    public void shouldCountStruckLines_withNext_4x4_partial(Field field, Pair<Integer, Integer> cellIndexes, int expectedNumberOfLines) {

        assertEquals(expectedNumberOfLines, uut.countStruckLines(field, field.getCell(cellIndexes.getKey(), cellIndexes.getValue())));
    }

    private static Stream<Arguments> fields4x4_withNext() {
        File dir = new File("src/test/resources/field_states/4x4");
        File[] files = dir.listFiles();
        assert files != null;
        List<Pair<Integer, Integer>> additionalCells = List.of(
                Pair.of(0, 2),
                Pair.of(2, 2),
                Pair.of(1, 0),
                Pair.of(2, 2),
                Pair.of(1, 0),
                Pair.of(2, 2),
                Pair.of(0, 2),
                Pair.of(3, 3)
        );
        List<Integer> expectedNumberOfStruckLines = List.of(1, 2, 3, 5, 6, 8, 8, 10);

        List<Field> fields = Arrays.stream(files)
                .map(StruckManagerTestUtils::mapToField)
                .toList();

        return IntStream.range(0, additionalCells.size())
                .mapToObj(index -> Arguments.of(fields.get(index), additionalCells.get(index), expectedNumberOfStruckLines.get(index)));
    }

    @ParameterizedTest
    @MethodSource("fields5x5_withMultipleNext")
    public void shouldCountStruckLines_withMultipleNext_5x5_partial(Field field, List<Pair<Integer, Integer>> cellIndexes, int expectedNumberOfLines) {
        Assertions.assertEquals(expectedNumberOfLines, uut.countStruckLines(field, cellIndexes.stream()
                .map(cellIndex -> field.getCell(cellIndex.getKey(), cellIndex.getValue()))
                .toArray(Cell[]::new)));
    }

    private static Stream<Arguments> fields5x5_withMultipleNext() {
        File dir = new File("src/test/resources/field_states/5x5");
        File[] files = dir.listFiles();
        assert files != null;
        List<List<Pair<Integer, Integer>>> additionalCells = List.of(
                List.of(Pair.of(2, 1), Pair.of(2, 4)),
                List.of(Pair.of(2, 2), Pair.of(3, 4)),
                List.of(Pair.of(1, 2)),
                List.of(Pair.of(0, 0), Pair.of(0, 1), Pair.of(0, 2)),
                List.of(Pair.of(2, 1), Pair.of(2, 4)),
                List.of(Pair.of(0, 3), Pair.of(3, 0)),
                List.of(Pair.of(0, 3)),
                List.of(Pair.of(2, 1), Pair.of(0, 3))
        );
        List<Integer> expectedNumberOfStruckLines = List.of(1, 5, 5, 4, 8, 8, 9, 12);

        List<Field> fields = Arrays.stream(files)
                .skip(1)
                .map(StruckManagerTestUtils::mapToField)
                .toList();

        return IntStream.range(0, additionalCells.size())
                .mapToObj(index -> Arguments.of(fields.get(index), additionalCells.get(index), expectedNumberOfStruckLines.get(index)));
    }
}
