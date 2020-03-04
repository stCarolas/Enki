package com.github.stcarolas.enki.core.provider.strategies.download;

import java.io.File;

import org.eclipse.jgit.api.CloneCommand;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class GitCloneDownloadStrategyTest {

	@Test
	public void test() {
		var cloneCommandMock = mock(CloneCommand.class);
		when(cloneCommandMock.setURI(any())).thenReturn(cloneCommandMock);
		when(cloneCommandMock.setDirectory(any())).thenReturn(cloneCommandMock);
		var url = "url";
		var dir = mock(File.class);
		var command = GitCloneDownloadStrategy.cloneFn
			.apply( () -> cloneCommandMock, url, dir );
		verify(cloneCommandMock).setURI(url);
		verify(cloneCommandMock).setDirectory(dir);
	}

}
