package ru.verstache.mnk.core;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StruckManagerTestUtils {

    private static final String TOTAL_GROUP_NAME = "total";
    private static final String ROWS_GROUP_NAME = "rows";
    private static final String COLUMNS_GROUP_NAME = "columns";
    private static final String MAIN_DIAGONALS_GROUP_NAME = "mainDiagonals";
    private static final String SECONDARY_DIAGONALS_GROUP_NAME = "secondaryDiagonals";
    private static final String testFilenamePattern = String.format(
            "^(?<%s>\\d+)_r(?<%s>\\d+)c(?<%s>\\d+)md(?<%s>\\d+)sd(?<%s>\\d+).*",
            TOTAL_GROUP_NAME,
            ROWS_GROUP_NAME,
            COLUMNS_GROUP_NAME,
            MAIN_DIAGONALS_GROUP_NAME,
            SECONDARY_DIAGONALS_GROUP_NAME);

    private static final Pattern testFilePattern = Pattern.compile(testFilenamePattern, Pattern.CASE_INSENSITIVE);

    public static Field mapToField(File file) {
        List<List<String>> tableFromFile = new ArrayList<>();
        try {
            tableFromFile = Files.lines(file.toPath())
                    .map(line -> Arrays.asList(line.split(",")))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (tableFromFile.isEmpty()) {
            throw new RuntimeException("Failed to read file " + file.getName());
        }

        List<List<String>> finalTableFromFile = tableFromFile;
        return new Field() {
            @Override
            public int getHeight() {
                return finalTableFromFile.size();
            }

            @Override
            public int getWidth() {
                return finalTableFromFile.get(0).size();
            }

            @Override
            public List<Cell> getCells() {
                return toListOfCells(finalTableFromFile);
            }

            public String toString() {
                return getRows().stream()
                        .map(row -> row.getCells().stream()
                                .map(cell -> cell.isStruck() ? "X" : "O")
                                .collect(Collectors.joining(", ")))
                        .collect(Collectors.joining("    \n"));
            }
        };
    }

    public static int countStrokeCells(File file) {
        try {
            return (int) Files.lines(file.toPath())
                    .map(line -> Arrays.asList(line.split(",")))
                    .flatMap(Collection::stream)
                    .filter(symbol -> "x".equalsIgnoreCase(symbol))
                    .count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Cell> toListOfCells(List<List<String>> finalTableFromFile) {
        List<Cell> cells = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < finalTableFromFile.size(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < finalTableFromFile.get(rowIndex).size(); columnIndex++) {
                int finalRowIndex = rowIndex;
                int finalColumnIndex = columnIndex;
                cells.add(new Cell() {
                    @Override
                    public int getRowIndex() {
                        return finalRowIndex;
                    }

                    @Override
                    public int getColumnIndex() {
                        return finalColumnIndex;
                    }

                    @Override
                    public boolean isStruck() {
                        return finalTableFromFile.get(finalRowIndex).get(finalColumnIndex).equalsIgnoreCase("X");
                    }
                });
            }
        }
        return cells;
    }


    public static Integer getExpectedNumberOfStruckLines(File file) {
        return getExpectedNumberOfStruckLines(file.getName());
    }
    public static Integer getExpectedNumberOfStruckLines(String fileName) {
        boolean containsPath = fileName.contains("\\");
        return extractByLineType(containsPath
                ? StringUtils.substringAfterLast(fileName, "\\")
                : fileName, TOTAL_GROUP_NAME);
    }

    public static int extractByLineType(String fileName, String groupName) {
        Matcher matcher = testFilePattern.matcher(fileName);
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group(groupName));
        }
        return Integer.parseInt(StringUtils.substringBefore(fileName, "_"));
    }

    public static List<File> getInnerFilesRecursively(String folder) {
        Path parentFolder = Paths.get(String.format("src/test/resources/field_states/%s", folder));
        try (Stream<Path> stream = Files.walk(parentFolder)) {
            return stream.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<Cell> toCells(Field field, Set<Integer> cellIndexes) {
        return cellIndexes.stream()
                .map(index -> field.getCell(index / field.getWidth(), index % field.getHeight()))
                .collect(Collectors.toSet());
    }

    public static void assertEqualCellSet(Set<Cell> expected, Set<Cell> actual) {
        assertEquals(expected.size(), actual.size());
        expected.forEach(expectedCell -> assertTrue(actual.stream().anyMatch(actualCell -> areEqual(expectedCell, actualCell))));
    }

    public static void assertEqualCellList(List<Cell> expected, List<Cell> actual) {
        assertEquals(expected.size(), actual.size());
        IntStream.range(0, expected.size())
                        .forEach(i -> assertEqual(expected.get(i), actual.get(i)));
    }

    static void assertEqual(Cell expectedCell, Cell actualCell) {
        assertEquals(actualCell.getRowIndex(), expectedCell.getRowIndex());
        assertEquals(actualCell.getColumnIndex(), expectedCell.getColumnIndex());
    }

    static boolean areEqual(Cell expectedCell, Cell actualCell) {
        return actualCell.getRowIndex() == expectedCell.getRowIndex() && actualCell.getColumnIndex() == expectedCell.getColumnIndex();
    }
}
