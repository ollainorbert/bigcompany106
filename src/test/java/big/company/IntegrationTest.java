package big.company;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTest {
    private static final String EXPECTED_OUTPUT =
        """
        Managers who earn less than they should:
        Employee: 123, with 238200.0 amount.
        Employee: 307, with 8400.0 amount.
        Employee: 306, with 8200.0 amount.
        Employee: 305, with 8000.0 amount.
        
        Managers who earn more than they should:
        Employee: 124, with 375000.0 amount.
        
        Employees with too long reporting line:
        Employee: 308, with 5 number of manager.
        """;

    @Test
    public void intTest() {
        String[] arguments = new String[] { "src/test/resources/integration-test.csv" };

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(byteArrayOutputStream);
        System.setOut(out);

        Main.main(arguments);

        String consoleOutput = byteArrayOutputStream.toString(Charset.defaultCharset());
        assertTrue(consoleOutput.contains(EXPECTED_OUTPUT));

        out.close();
    }
}
