package ru.verstache.mnk.core.strokeSet;

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

public class WinningSetTest {

    private StrokeSetManager uut;

    @BeforeEach
    public void init() {
        uut = new StrokeSetManager();;
    }

    @ParameterizedTest
    @MethodSource("fields4x4_stroke_set")
    public void shouldProvideStrokeSet_4x4(Field field, Set<Cell> strokeCells) {

        assertEqualCellSet(strokeCells, uut.getStrokeSet(field));
    }

    private static Stream<Arguments> fields4x4_stroke_set() {
        File dir = new File("src/test/resources/field_states/4x4");
        File[] files = dir.listFiles();
        assert files != null;

        List<Field> fields = Arrays.stream(files)
                .map(StruckManagerTestUtils::mapToField)
                .toList();

        List<Set<Integer>> cellIndexes = Stream.of(
                        IntStream.of(),
                        IntStream.of(3, 6, 9, 12),
                        IntStream.of(1, 3, 5, 6, 9, 12, 13),
                        IntStream.of(1, 3, 5, 6, 9, 12, 13, 14, 15),
                        IntStream.of(1, 3, 5, 6, 7, 9, 11, 12, 13, 14, 15),
                        IntStream.of(0, 1, 2, 3, 5, 6, 7, 9, 11, 12, 13, 14, 15),
                        IntStream.of(0, 1, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15),
                        IntStream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
                )
                .map(intStream -> intStream
                        .boxed()
                        .collect(Collectors.toSet()))
                .toList();

        return IntStream.range(0, cellIndexes.size())
                .mapToObj(index -> Arguments.of(fields.get(index), toCells(fields.get(index), cellIndexes.get(index))));
    }
}
