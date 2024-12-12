package big.company.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmployeeFileNameArgumentExtractorTest {
    private static final String NO_FILE_ADDED_AS_ARG = "No file was added as arg!";
    private static final String ONLY_ONE_FILE_CAN_BE_ADDED_AS_ARG = "Only one file can be added as arg!";

    private EmployeeFileNameArgumentExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new EmployeeFileNameArgumentExtractor();
    }

    @Test
    void extractWithNoArgTest() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> extractor.extractFileName(new String[] {})
        );

        assertEquals(NO_FILE_ADDED_AS_ARG, thrown.getMessage());
    }

    @Test
    void extractWithMoreThanOneArgTest() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> extractor.extractFileName(new String[] {"fileName1", "fileName2"})
        );

        assertEquals(ONLY_ONE_FILE_CAN_BE_ADDED_AS_ARG, thrown.getMessage());
    }

    @Test
    void extractOneArgTest() {
        String fileName = "fileName";
        assertEquals(fileName, extractor.extractFileName(new String[] {fileName}));
    }
}
