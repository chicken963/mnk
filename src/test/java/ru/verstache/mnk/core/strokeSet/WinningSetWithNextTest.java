package ru.verstache.mnk.core.strokeSet;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.verstache.mnk.core.Cell;
import ru.verstache.mnk.core.Field;
import ru.verstache.mnk.core.StruckManagerTestUtils;
import ru.verstache.mnk.manager.StrokeLinesManager;
import ru.verstache.mnk.manager.StrokeSetManager;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ru.verstache.mnk.core.StruckManagerTestUtils.assertEqualCellSet;
import static ru.verstache.mnk.core.StruckManagerTestUtils.toCells;

public class WinningSetWithNextTest {

    private StrokeSetManager uut;

    @BeforeEach
    public void init() {
        uut = new StrokeSetManager();;
    }

    @ParameterizedTest
    @MethodSource("fields5x5_stroke_set_with_next")
    public void shouldProvideStrokeSetWithNext_5x5(Field field, Cell[] additionalCells, Set<Cell> strokeCells) {

        assertEqualCellSet(strokeCells, uut.getStrokeSet(field, additionalCells));
    }

    private static Stream<Arguments> fields5x5_stroke_set_with_next() {
        File dir = new File("src/test/resources/field_states/5x5");
        File[] files = dir.listFiles();
        assert files != null;

        List<Field> fields = Arrays.stream(files)
                .skip(1)
                .map(StruckManagerTestUtils::mapToField)
                .toList();

        List<List<Pair<Integer, Integer>>> additionalCells = List.of(
                List.of(Pair.of(2, 2), Pair.of(2, 4)),
                List.of(Pair.of(2, 4)),
                List.of(Pair.of(3, 3)),
                List.of(Pair.of(0, 3), Pair.of(2, 1)),
                List.of(Pair.of(2, 4)),
                List.of(Pair.of(1, 0))
        );

        List<Set<Integer>> cellIndexes = Stream.of(
                        IntStream.of(0, 4, 5, 6, 8, 10, 12, 15, 16, 18, 20, 24),
                        IntStream.of(0, 5, 10, 15, 20, 21, 22, 23, 24),
                        IntStream.of(0, 4, 5, 6, 8, 10, 12, 15, 16, 18, 20, 21, 22, 23, 24),
                        IntStream.of(0, 1, 2, 3, 4, 5, 6, 8, 10, 12, 13, 15, 16, 17, 18, 19, 20, 23, 24),
                        IntStream.of(0, 4, 5, 6, 8, 9, 10, 12, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24),
                        IntStream.of(0, 2, 4, 5, 6, 7, 8, 9, 10, 12, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24)
                )
                .map(intStream -> intStream
                        .boxed()
                        .collect(Collectors.toSet()))
                .toList();

        return IntStream.range(0, cellIndexes.size())
                .mapToObj(index ->
                        Arguments.of(fields.get(index),
                                additionalCells.stream()
                                        .map(indexesSet -> indexesSet.stream()
                                        .map(cellIndex -> fields.get(index).getCell(cellIndex.getKey(), cellIndex.getValue()))
                                        .toArray(Cell[]::new))
                                        .toList()
                                        .get(index),
                                toCells(fields.get(index), cellIndexes.get(index))));
    }

}
