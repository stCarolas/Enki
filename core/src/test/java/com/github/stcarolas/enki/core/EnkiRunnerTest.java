package com.github.stcarolas.enki.core;

import static io.vavr.API.Seq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnkiRunnerTest {

	@Test
	public void testEmptyProviders() {
		var run = EnkiRunner.enki().run();
		Assertions.assertTrue(run.isEmpty());
	}

	@Test
	public void testEmptyHandlers() {
		var run = EnkiRunner.enki()
			.withProvider(mock(RepoProvider.class))
			.run();
		Assertions.assertTrue(run.isEmpty());
	}

	@Test
	@SuppressWarnings({"unchecked"})
	public void test_ignore_null_provider() {
		var provider = mock(RepoProvider.class);
		var repo = new TestRepo();
		when(provider.repositories()).thenReturn(Seq(repo));

		var handler = mock(RepoHandler.class);
		when(handler.handle(repo)).thenReturn(repo);

		var run = EnkiRunner.enki()
			.withProvider(null)
			.withProvider(provider)
			.withProvider(null)
			.withHandler(handler)
			.run();
		verify(handler).handle(repo);
		Assertions.assertTrue(!run.isEmpty());
	}

	@Test
	@SuppressWarnings({"unchecked"})
	public void test_ignore_null_handler() {
		var provider = mock(RepoProvider.class);
		var repo = new TestRepo();
		when(provider.repositories()).thenReturn(Seq(repo));

		var handler = mock(RepoHandler.class);
		when(handler.handle(repo)).thenReturn(repo);

		var run = EnkiRunner.enki()
			.withProvider(provider)
			.withHandler(null)
			.withHandler(handler)
			.withHandler(null)
			.run();
		verify(handler).handle(repo);
		Assertions.assertTrue(!run.isEmpty());
	}

	@Test
	@SuppressWarnings({"unchecked"})
	public void test_ignore_error_in_provider() {
		var rightProvider = mock(RepoProvider.class);
		var repo = new TestRepo();
		when(rightProvider.repositories()).thenReturn(Seq(repo));

		var wrongProvider = mock(RepoProvider.class);
		when(wrongProvider.repositories()).thenThrow(new RuntimeException("some error"));

		var handler = mock(RepoHandler.class);
		when(handler.handle(repo)).thenReturn(repo);

		var run = EnkiRunner.enki()
			.withProvider(wrongProvider)
			.withProvider(rightProvider)
			.withHandler(handler)
			.run();
		Assertions.assertTrue(!run.isEmpty());
		verify(handler).handle(repo);
	}

	@Test
	@SuppressWarnings({"unchecked"})
	public void test_ignore_error_in_handler() {
		var repo = new TestRepo();
		var provider = mock(RepoProvider.class);
		when(provider.repositories()).thenReturn(Seq(repo));

		var rightHandler = mock(RepoHandler.class);
		when(rightHandler.handle(repo)).thenReturn(repo);
		var wrongHandler = mock(RepoHandler.class);
		when(wrongHandler.handle(repo)).thenThrow(new RuntimeException("some error"));

		var run = EnkiRunner.enki()
			.withProvider(provider)
			.withHandler(wrongHandler)
			.withHandler(rightHandler)
			.run();
		Assertions.assertTrue(!run.isEmpty());
		verify(rightHandler).handle(repo);
	}

	@Test
	@SuppressWarnings({"unchecked"})
	public void test_calling_handler_for_each_repo_provided_by_one_provider() {
		var provider = mock(RepoProvider.class);
		var repo1 = new TestRepo();
		var repo2 = new TestRepo();
		when(provider.repositories()).thenReturn(Seq(repo1,repo2));
		var handler = mock(RepoHandler.class);
		when(handler.handle(repo1)).thenReturn(repo1);
		when(handler.handle(repo2)).thenReturn(repo2);
		EnkiRunner.enki()
			.withProvider(provider)
			.withHandler(handler)
			.run();
		verify(handler).handle(repo1);
		verify(handler).handle(repo2);
	}

	@Test
	@SuppressWarnings({"unchecked"})
	public void test_calling_handler_for_each_repo_provided_by_multiple_providers() {
		var provider1 = mock(RepoProvider.class);
		var repo1 = new TestRepo();
		when(provider1.repositories()).thenReturn(Seq(repo1));

		var provider2 = mock(RepoProvider.class);
		var repo2 = new TestRepo();
		when(provider2.repositories()).thenReturn(Seq(repo2));

		var handler = mock(RepoHandler.class);
		when(handler.handle(repo1)).thenReturn(repo1);
		when(handler.handle(repo2)).thenReturn(repo2);
		EnkiRunner.enki()
			.withProvider(provider1)
			.withProvider(provider2)
			.withHandler(handler)
			.run();
		verify(handler).handle(repo1);
		verify(handler).handle(repo2);
	}
}
