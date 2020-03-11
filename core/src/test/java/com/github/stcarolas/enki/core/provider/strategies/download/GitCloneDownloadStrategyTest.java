package com.github.stcarolas.enki.core.provider.strategies.download;

import static io.vavr.API.Function;
import static io.vavr.API.None;
import static io.vavr.API.Some;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.File;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.TestRepo;
import com.github.stcarolas.enki.core.transport.DefaultTransportConfigCallback;
import com.github.stcarolas.enki.core.util.Spy4;

import org.eclipse.jgit.api.CloneCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

// todo test git clone on subfunctions errors
public class GitCloneDownloadStrategyTest {
		
	Repo repo = new TestRepo();
	String url = "url";
	File dirMock = mock(File.class);
	DefaultTransportConfigCallback transportMock = mock(DefaultTransportConfigCallback.class);
	CloneCommand cloneCommandMock = mock(CloneCommand.class);

	@Test
	public void test_cloneFn() {
		when(cloneCommandMock.setURI(any())).thenReturn(cloneCommandMock);
		when(cloneCommandMock.setDirectory(any())).thenReturn(cloneCommandMock);
		when(cloneCommandMock.setTransportConfigCallback(any())).thenReturn(cloneCommandMock);
		var command = GitCloneDownloadStrategy.cloneFn.apply( 
				()-> transportMock, () -> cloneCommandMock, url, dirMock 
		);
		Assertions.assertEquals(cloneCommandMock, command);
		verify(cloneCommandMock).setTransportConfigCallback(transportMock);
		verify(cloneCommandMock).setURI(url);
		verify(cloneCommandMock).setDirectory(dirMock);
	}

	@Test
	public void test_get_for_none_url() {
		var clonedRepo = GitCloneDownloadStrategy.builder().build()
			.withSshUrl(None())
			.withRepository(Some(repo))
			.withCloneCommand(() ->  cloneCommandMock)
			.withClone(($1,$2,$3,$4) -> cloneCommandMock)
			.withTransport(() ->  transportMock)
			.get();

		assertNull(clonedRepo);
		verifyNoInteractions(cloneCommandMock);
		verifyNoInteractions(transportMock);
	}

	@Test
	public void test_get_for_none_repo() {
		var clonedRepo = GitCloneDownloadStrategy.builder().build()
			.withSshUrl(Some(url))
			.withRepository(None())
			.withCloneCommand(() ->  cloneCommandMock)
			.withClone(($1,$2,$3,$4) -> cloneCommandMock)
			.withTransport(() ->  transportMock)
			.get();

		assertNull(clonedRepo);
		verifyNoInteractions(cloneCommandMock);
		verifyNoInteractions(transportMock);
	}

	@Test
	public void test_get_using_right_args() {
		var targetDir = mock(File.class);
		var repoMock = mock(Repo.class);
		when(repoMock.directory()).thenReturn(targetDir);

		var cloneFnSpy = Spy4.spy(($1,$2,$3,$4) -> cloneCommandMock);
		var cloneCommandSupplier = Function(() -> cloneCommandMock);
		var transportSupplier = Function(() ->  transportMock);

		var cloneRepo = GitCloneDownloadStrategy.builder().build()
			.withSshUrl(Some(url))
			.withRepository(Some(repoMock))
			.withCloneCommand(cloneCommandSupplier)
			.withClone(($1,$2,$3,$4)-> cloneFnSpy.fn().apply($1,$2,$3,$4))
			.withTransport(transportSupplier)
			.get();

		assertEquals(transportSupplier, cloneFnSpy.arg1());
		assertEquals(cloneCommandSupplier, cloneFnSpy.arg2());
		assertEquals(url, cloneFnSpy.arg3());
		assertEquals(targetDir, cloneFnSpy.arg4());
		assertEquals(targetDir, cloneRepo);
	}

}
