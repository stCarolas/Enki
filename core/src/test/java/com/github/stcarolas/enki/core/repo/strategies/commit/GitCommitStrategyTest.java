package com.github.stcarolas.enki.core.repo.strategies.commit;

import static com.github.stcarolas.enki.core.repo.strategies.commit.GitCommitStrategy.GitCommit;
import static com.github.stcarolas.enki.core.repo.strategies.commit.GitCommitStrategyTestHelpers.successfullCommit;
import static com.github.stcarolas.enki.core.repo.strategies.commit.GitCommitStrategyTestHelpers.successfullStage;
import static com.github.stcarolas.enki.core.repo.strategies.commit.GitCommitStrategyTestHelpers.tmpDir;
import static com.github.stcarolas.enki.core.util.Spy1.spy;
import static com.github.stcarolas.enki.core.util.Spy2.spy;
import static io.vavr.API.Failure;
import static io.vavr.API.Option;
import static io.vavr.API.Success;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.UUID;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Try;

public class GitCommitStrategyTest {

	@Nested
	@DisplayName("Tests for factory method GitCommit()")
	public class TestsForGitCommit{
		@Test
		public void test_Failure_on_empty_provider() {
			assertTrue(
				GitCommit(null).apply("message").isFailure()
			);
		}

		@Test
		public void test_Failure_on_empty_directory() {
			assertTrue(
				GitCommit(() -> null).apply("message").isFailure()
			);
		}

		@Test
		public void test_Failure_on_exception_while_accessing_directory() {
			assertTrue(
				GitCommit(() -> {throw new RuntimeException("some error");})
					.apply("message")
					.isFailure()
			);
		}
	}

	@Nested
	@DisplayName("Tests for main public method apply()")
	public class TestsForApply{
		@Test
		public void test_Failure_on_missing_commit_message() {
			assertTrue(GitCommit(tmpDir()).apply(null).isFailure());
			assertTrue(GitCommit(tmpDir()).apply("").isFailure());
			assertTrue(GitCommit(tmpDir()).apply("  ").isFailure());
		}

		@Test
		public void test_Failure_on_clean_status_without_staging_and_commiting() {
			var stageMock  = spy(successfullStage());
			var commitMock = spy(successfullCommit());

			var strategy = new GitCommitStrategy(Option(tmpDir())){{
				git     = ignore -> Success(mock(Git.class));
				isClean = ignore -> true;
				stage   = stageMock.fn();
				commit  = commitMock.fn();
			}};

			assertTrue(strategy.apply("some message").isFailure());
			assertFalse(stageMock.isCalled());
			assertFalse(commitMock.isCalled());
		}

		@Test
		public void test_Success_stage_and_commit_if_unclean() {
			var commit = mock(RevCommit.class);
			var stageMock  = spy(successfullStage());
			var commitMock = spy(successfullCommit(commit));

			var strategy = new GitCommitStrategy(Option(tmpDir())){{
				git     = ignore -> Success(mock(Git.class));
				isClean = ignore -> false;
				stage   = stageMock.fn();
				commit  = commitMock.fn();
			}};

			var result = strategy.apply("some message");
			assertTrue(result.isSuccess());
			assertEquals(commit, result.get());
			assertTrue(stageMock.isCalled());
			assertTrue(commitMock.isCalled());
		}

		@Test
		public void test_Failure_on_failed_stage() {
			var commitMock = spy(successfullCommit());

			var strategy = new GitCommitStrategy(Option(tmpDir())){{
				git     = ignore -> Success(mock(Git.class));
				isClean = ignore -> false;
				stage   = ignore -> Failure(new RuntimeException());
				commit  = commitMock.fn();
			}};

			assertTrue(strategy.apply("some message").isFailure());
			assertFalse(commitMock.isCalled());
		}

		@Test
		public void test_Failure_on_failed_commit() {
			var strategy = new GitCommitStrategy(Option(tmpDir())){{
				git     = ignore -> Success(mock(Git.class));
				isClean = ignore -> false;
				stage   = successfullStage();
				commit  = (arg1,arg2) -> Failure(new RuntimeException("error"));
			}};

			assertTrue(strategy.apply("some message").isFailure());
		}
	}

	@Nested
	@DisplayName("Tests for git() which should provide Git")
	public class TestsForGit{
		Function1<File, Try<Git>> git = 
			new GitCommitStrategy(Option(tmpDir())).git;

		@Test
		public void test_Failure_on_non_directory_without_git_repo() {
			var emptyDir = new File("/tmp/" + UUID.randomUUID().toString());
			emptyDir.mkdir();
			Assertions.assertTrue(
				git.apply(emptyDir).isFailure()
			);
		}

		@Test
		public void test_Success() throws IllegalStateException, GitAPIException {
			var dirWithGit = new File("/tmp/" + UUID.randomUUID().toString());
			dirWithGit.mkdir();
			Git.init().setGitDir(dirWithGit).call();
			Assertions.assertTrue(git.apply(dirWithGit).isSuccess());
		}
	}

	@Nested
	@DisplayName("Tests for isClean() which should check if git repo has changes to commit")
	public class TestsForIsClean{
		Function1<Git, Boolean> isClean = 
			new GitCommitStrategy(Option(tmpDir())).isClean;

		@Test
		public void test_Return_same_as_git_status() throws NoWorkTreeException, GitAPIException {
			var gitMock = mock(Git.class);
			var statusCommandMock = mock(StatusCommand.class);
			var statusMock = mock(Status.class);
			Mockito.when(gitMock.status())
				.thenReturn(statusCommandMock);
			Mockito.when(statusCommandMock.call())
				.thenReturn(statusMock);

			Mockito.when(statusMock.isClean())
				.thenReturn(true);
			Assertions.assertTrue(isClean.apply(gitMock));

			Mockito.reset(statusMock);
			Mockito.when(statusMock.isClean())
				.thenReturn(false);
			Assertions.assertFalse(isClean.apply(gitMock));

		}

		@Test
		public void test_Return_true_on_git_error() throws NoWorkTreeException, GitAPIException {
			var gitMock = mock(Git.class);
			var statusCommandMock = mock(StatusCommand.class);
			Mockito.when(gitMock.status())
				.thenReturn(statusCommandMock);
			Mockito.when(statusCommandMock.call())
				.thenThrow(new RuntimeException("some error"));

			Assertions.assertTrue(isClean.apply(gitMock));
		}
	}

	@Nested
	@DisplayName("Tests for stage() which should add changes to git index")
	public class TestsForStage{
		Function1<Git, Try<Git>> stage = 
			new GitCommitStrategy(Option(tmpDir())).stage;

		@Test
		public void test_calling_git_add_with_all_files() throws NoWorkTreeException, GitAPIException {
			var gitMock = mock(Git.class);
			var addCommandMock = mock(AddCommand.class);
			when(gitMock.add()).thenReturn(addCommandMock);
			when(addCommandMock.addFilepattern(any()))
				.thenReturn(addCommandMock);
			when(addCommandMock.call())
				.thenReturn(mock(DirCache.class));

			var result = stage.apply(gitMock);
			Assertions.assertTrue(result.isSuccess());
			Assertions.assertEquals(gitMock, result.get());
			verify(addCommandMock).addFilepattern(".");
		}

		@Test
		public void test_Failure_on_git_error() throws NoWorkTreeException, GitAPIException {
			var gitMock = mock(Git.class);
			var addCommandMock = mock(AddCommand.class);
			when(gitMock.add()).thenReturn(addCommandMock);
			when(addCommandMock.addFilepattern(any()))
				.thenReturn(addCommandMock);
			when(addCommandMock.call())
				.thenThrow(new RuntimeException("some error"));

			Assertions.assertTrue(stage.apply(gitMock).isFailure());
		}
	}

	@Nested
	@DisplayName("Tests for commit() which should make git commit")
	public class TestsForCommit{
		Function2<String, Git, Try<RevCommit>> commit = 
			new GitCommitStrategy(Option(tmpDir())).commit;

		@Test
		public void test_calling_git_commit_with_provided_message() throws NoWorkTreeException, GitAPIException {
			var gitMock = mock(Git.class);
			var commitCommandMock = mock(CommitCommand.class);
			var commitMock = mock(RevCommit.class);
			when(gitMock.commit()).thenReturn(commitCommandMock);
			when(commitCommandMock.setMessage(any()))
				.thenReturn(commitCommandMock);
			when(commitCommandMock.call())
				.thenReturn(commitMock);

			var message = "some message";
			var result = commit.apply(message, gitMock);
			Assertions.assertTrue(result.isSuccess());
			Assertions.assertEquals(commitMock, result.get());
			verify(commitCommandMock).setMessage(message);
		}

		@Test
		public void test_Failure_on_empty_message() throws NoWorkTreeException, GitAPIException {
			var gitMock = mock(Git.class);
			Assertions.assertTrue(
				commit.apply("  ", gitMock).isFailure()
			);
			Assertions.assertTrue(
				commit.apply(null, gitMock).isFailure()
			);
		}

		@Test
		public void test_Failure_on_git_error() throws NoWorkTreeException, GitAPIException {
			var gitMock = mock(Git.class);
			var commitCommandMock = mock(CommitCommand.class);
			when(gitMock.commit()).thenReturn(commitCommandMock);
			when(commitCommandMock.setMessage(any()))
				.thenReturn(commitCommandMock);
			when(commitCommandMock.call())
				.thenThrow(new RuntimeException("some error"));

			Assertions.assertTrue(
				commit.apply("message",gitMock).isFailure()
			);
		}
	}

}
