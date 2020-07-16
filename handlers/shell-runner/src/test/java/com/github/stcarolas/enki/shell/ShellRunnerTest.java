package  com.github.stcarolas.enki.shell;

import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ShellRunnerTest {
	@Test
	public void testExecuteShellCommand() {
		assertTrue(new ShellRunner("echo test").apply(new File("/tmp/")).isSuccess());
	}
	
}
