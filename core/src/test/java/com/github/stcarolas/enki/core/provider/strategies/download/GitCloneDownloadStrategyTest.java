package com.github.stcarolas.enki.core.provider.strategies.download;

import java.io.File;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.TestRepo;
import com.github.stcarolas.enki.core.transport.DefaultTransportConfigCallback;

import org.eclipse.jgit.api.CloneCommand;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static io.vavr.API.*;

public class GitCloneDownloadStrategyTest {
		
	Repo repo = new TestRepo();
	String url = "url";
	GitCloneDownloadStrategy<Repo> workingStrategy = 
		(GitCloneDownloadStrategy<Repo>) GitCloneDownloadStrategy.GitSshClone(repo, url);

	@Test
	public void test_cloneFn() {
		var cloneCommandMock = mock(CloneCommand.class);
		when(cloneCommandMock.setURI(any())).thenReturn(cloneCommandMock);
		when(cloneCommandMock.setDirectory(any())).thenReturn(cloneCommandMock);
		var url = "url";
		var dir = mock(File.class);
		var transport = mock(DefaultTransportConfigCallback.class);
		var command = GitCloneDownloadStrategy.cloneFn
			.apply( ()-> transport, () -> cloneCommandMock, url, dir );
		verify(cloneCommandMock).setURI(url);
		verify(cloneCommandMock).setDirectory(dir);
		verify(cloneCommandMock).setTransportConfigCallback(transport);
	}

	@Test
	public void test() {
		var strategy = GitCloneDownloadStrategy.builder().build()
			.withSshUrl(None())
			.withRepository(Some(repo))
			.withCloneCommand( () -> mock(CloneCommand.class) )
			.withClone( ($1,$2,$3,$4) -> mock(CloneCommand.class) )
			.withTransport( () -> mock(DefaultTransportConfigCallback.class) );
	}

}
