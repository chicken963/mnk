package ru.verstache.mnk.core.subline;

import org.junit.jupiter.api.Test;
import ru.verstache.mnk.core.Field;
import ru.verstache.mnk.core.Line;
import ru.verstache.mnk.core.SubLine;
import ru.verstache.mnk.utils.CombinationUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.verstache.mnk.core.StruckManagerTestUtils.assertEqualCellList;
import static ru.verstache.mnk.core.StruckManagerTestUtils.mapToField;

public class SublineExtractorTest {

    @Test
    public void shouldExtractSubLines() {
        File file = new File("src/test/resources/field_states/horizontal/3x6/3_r1c0md1sd1.csv");
        Field field = mapToField(file);
        Line firstRow = field.getRows().get(0);
        List<SubLine> subLines = firstRow.getSubLines(3);
        assertEquals(4, subLines.size());
        assertEqualCellList(firstRow.getCells().subList(3, 6), subLines.get(3).getCells());

    }

    @Test
    public void shouldExtractSubLinesKSubsets() {
        File file = new File("src/test/resources/field_states/horizontal/3x6/3_r1c0md1sd1.csv");
        Field field = mapToField(file);
        List<List<SubLine>> sublines = CombinationUtils.generateCombinations(field.getLinesStream()
                .map(line -> line.getSubLines(3)).collect(Collectors.toList()), 3);
        List<Integer> lengths = field.getLinesStream().map(Line::length).toList();
        assertEquals(2192, sublines.size());
    }
}
