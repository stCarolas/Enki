package com.github.stcarolas.enki.core.provider.strategies.upload;

import static io.vavr.API.List;
import static io.vavr.API.Option;
import static io.vavr.API.None;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.util.Spy1;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.transport.PushResult;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;


@SuppressWarnings({"rawtypes"})
public class GitPushUploadStrategyTest {
	@Nested
	public class TestDefaultGitPushFn {
		Git git = mock(Git.class);
		PushCommand pushCommand = mock(PushCommand.class);
		List<PushResult> pushResults = List(mock(PushResult.class));

		@Test
		public void successfull() throws Exception {
			when(git.push()).thenReturn(pushCommand);
			when(pushCommand.call()).thenReturn(pushResults);

			var results = GitPushUploadStrategy.defaultGitPushFn.apply(git);

			verify(pushCommand).call();
			assertFalse(results.isEmpty());
			assertEquals(pushResults, results.get());
		}

		@Test
		public void error_on_calling_command() throws Exception {
			when(git.push()).thenReturn(pushCommand);
			when(pushCommand.call()).thenThrow(new RuntimeException("error"));

			var result = GitPushUploadStrategy.defaultGitPushFn.apply(git);

			assertTrue(result.isEmpty());
		}

		@Test
		public void error_on_getting_command() throws Exception {
			when(git.push()).thenThrow(new RuntimeException("error"));

			var result = GitPushUploadStrategy.defaultGitPushFn.apply(git);

			assertTrue(result.isEmpty());
		}
	}

	@Nested
	public class TestDefaultRepoDirectoryFn {
		Repo repoMock = mock(Repo.class);
		File dirMock = mock(File.class);

		@Test
		public void successfull() throws Exception {
			when(repoMock.directory()).thenReturn(dirMock);

			var dir = GitPushUploadStrategy.defaultRepoDirectoryFn.apply(repoMock);

			assertFalse(dir.isEmpty());
			assertEquals(dirMock, dir.get());
		}
		@Test
		public void error() throws Exception {
			when(repoMock.directory()).thenThrow(new RuntimeException("error"));

			var dir = GitPushUploadStrategy.defaultRepoDirectoryFn.apply(repoMock);

			assertTrue(dir.isEmpty());
		}
	}

	@Nested
	public class TestGitPushUploadStrategy {
		@Test
		public void use_default_functions() throws Exception {
			var strategy = (GitPushUploadStrategy)GitPushUploadStrategy.GitPush(null);
			assertEquals(
					GitPushUploadStrategy.defaultGitOpenFn, 
					strategy.getGitOpenFn()
			);
			assertEquals(
					GitPushUploadStrategy.defaultGitPushFn, 
					strategy.getGitPushFn()
			);
			assertEquals(
					GitPushUploadStrategy.defaultRepoDirectoryFn, 
					strategy.getRepoDirectoryFn()
			);
		}

		@Test
		public void setting_repo() throws Exception {
			var strategy = (GitPushUploadStrategy)GitPushUploadStrategy.GitPush(null);
			assertTrue(strategy.getRepository().isEmpty());

			var repo = mock(Repo.class);
			strategy = (GitPushUploadStrategy)GitPushUploadStrategy.GitPush(repo);
			assertEquals(repo, strategy.getRepository().get());
		}
	}

	@Nested
	public class TestGet {
		// todo how to factory functions for test
		Repo repoMock = mock(Repo.class);
		Git gitMock = mock(Git.class);
		File fileMock = mock(File.class);
		List<PushResult> expectedResult = List(mock(PushResult.class));
		Spy1<File, Option<Git>> gitOpenFnMock = Spy1.spy( file -> Option(gitMock) );
		Spy1<Git, Option<Iterable<PushResult>>> gitPushFnMock = 
			Spy1.spy( git -> Option(expectedResult) );
		Spy1<Repo, Option<File>> repoDirectoryFnMock = Spy1.spy(repo -> Option(fileMock));
		GitPushUploadStrategy.GitPushUploadStrategyBuilder strategy = 
			GitPushUploadStrategy.builder()
				.repository( Option(repoMock) )
				.gitOpenFn( gitOpenFnMock.fn() )
				.gitPushFn( gitPushFnMock.fn() )
				.repoDirectoryFn( repoDirectoryFnMock.fn() );

		@Test
		public void test_none_repo() throws Exception {
			var result = strategy.repository(None()).build().get();

			assertFalse(result.iterator().hasNext());
			assertFalse(repoDirectoryFnMock.isCalled());
			assertFalse(gitOpenFnMock.isCalled());
			assertFalse(gitPushFnMock.isCalled());
		}

		@Test
		public void test_using_repo_directory_fn() throws Exception {
			strategy.repoDirectoryFn(repoDirectoryFnMock.fn()).build().get();

			assertTrue(repoDirectoryFnMock.isCalled());
			assertEquals(repoMock, repoDirectoryFnMock.arg1());
		}

		@Test
		public void test_none_from_repo_directory_fn() throws Exception {
			repoDirectoryFnMock = Spy1.spy(repo -> None());

			var result = strategy
				.repoDirectoryFn(repoDirectoryFnMock.fn())
				.build()
				.get();

			assertTrue(repoDirectoryFnMock.isCalled());
			assertFalse(gitOpenFnMock.isCalled());
			assertFalse(gitPushFnMock.isCalled());
			assertFalse(result.iterator().hasNext());
		}

		@Test
		public void test_using_git_open_fn() throws Exception {
			strategy
				.gitOpenFn(gitOpenFnMock.fn())
				.build()
				.get();

			assertTrue(gitOpenFnMock.isCalled());
			assertEquals(fileMock, gitOpenFnMock.arg1());
		}

		@Test
		public void test_none_from_git_open_fn() throws Exception {
			gitOpenFnMock = Spy1.spy( file -> None() );

			var result = strategy
				.gitOpenFn(gitOpenFnMock.fn())
				.build()
				.get();

			assertTrue(gitOpenFnMock.isCalled());
			assertFalse(gitPushFnMock.isCalled());
			assertFalse(result.iterator().hasNext());
		}

		@Test
		public void test_using_git_push_fn() throws Exception {
			var result = strategy
				.gitPushFn(gitPushFnMock.fn())
				.build()
				.get();

			assertTrue(gitPushFnMock.isCalled());
			assertEquals(gitPushFnMock.arg1(), gitMock);
			assertEquals(expectedResult, result);
		}

		@Test
		public void test_none_from_git_push_fn() throws Exception {
			gitPushFnMock = Spy1.spy( git -> None() );

			var result = strategy
				.gitPushFn(gitPushFnMock.fn())
				.build()
				.get();

			assertTrue(gitPushFnMock.isCalled());
			assertFalse(result.iterator().hasNext());
		}
	}
}
