package com.github.stcarolas.enki.core.repo.strategies.commit;

import static com.github.stcarolas.enki.core.repo.strategies.commit.GitCommitStrategy.GitCommit;
import static io.vavr.API.Failure;
import static io.vavr.API.Function;
import static io.vavr.API.Option;
import static io.vavr.API.Success;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.Test;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Try;

public class GitCommitStrategyTest {

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

	@Test
	public void test_Failure_on_missing_commit_message() {
		assertTrue(GitCommit(this::tmpDir).apply(null).isFailure());
		assertTrue(GitCommit(this::tmpDir).apply("").isFailure());
		assertTrue(GitCommit(this::tmpDir).apply("  ").isFailure());
	}

	@Test
	public void test_Failure_on_clean_status_without_staging_and_commiting() {
		var gitMock    = mock(Git.class);
		var dir        = tmpDir();
		var stageMock  = Spy1.spy(successfullStage());
		var commitMock = Spy2.spy(successfullCommit());

		var strategy = new GitCommitStrategy(Option(() -> dir)){{
			git     = ignore -> Success(gitMock);
			isClean = ignore -> true;
			stage   = stageMock.fn();
			commit  = commitMock.fn();
		}};

		assertTrue(strategy.apply("some message").isFailure());
	}

	@Test
	public void test_stage_and_commit_on_unclean_status() {
		var gitMock    = mock(Git.class);
		var dir        = tmpDir();
		var stageMock  = Spy1.spy(successfullStage());
		var commitMock = Spy2.spy(successfullCommit());

		var strategy = new GitCommitStrategy(Option(() -> dir)){{
			git     = ignore -> Success(gitMock);
			isClean = ignore -> false;
			stage   = stageMock.fn();
			commit  = commitMock.fn();
		}};

		assertTrue(strategy.apply("some message").isSuccess());
		assertTrue(commitMock.isCalled());
		assertTrue(stageMock.isCalled());
	}

	@Test
	public void test_dont_commit_after_failed_stage() {
		var gitMock    = mock(Git.class);
		var dir        = tmpDir();
		var commitMock = Spy2.spy(successfullCommit());

		var strategy = new GitCommitStrategy(Option(() -> dir)){{
			git     = ignore -> Success(gitMock);
			isClean = ignore -> false;
			stage   = ignore -> Failure(new RuntimeException());
			commit  = commitMock.fn();
		}};

		assertTrue(strategy.apply("some message").isFailure());
		assertFalse(commitMock.isCalled());
	}

	@Test
	public void test_Failure_on_failed_commit() {
		var gitMock    = mock(Git.class);
		var dir        = tmpDir();
		var commitMock = Spy2.spy(successfullCommit());

		var strategy = new GitCommitStrategy(Option(() -> dir)){{
			git     = ignore -> Success(gitMock);
			isClean = ignore -> false;
			stage   = successfullStage();
			commit  = (arg1,arg2) -> Failure(new RuntimeException("error"));
		}};

		assertTrue(strategy.apply("some message").isFailure());
	}

	private Function1<Git, Try<Git>> successfullStage(){
		return Function( git -> Success(git) );
	}

	private Function2<String, Git, Try<RevCommit>> successfullCommit(){
		return Function((message, git) -> Success(mock(RevCommit.class)));
	}

	private File tmpDir(){
		return new File("/tmp/test");
	}

	public static class Spy1<T1,T2> {
		private Function1<T1,T2> fn;
		private Boolean isCalled = false;

		public static <T1,T2>Spy1<T1,T2> spy(Function1<T1,T2> fn){
			var spy = new Spy1<T1,T2>();
			spy.fn = fn;
			return spy;
		}

		public Function1<T1,T2>  fn(){
			return Function( arg1 -> {
				isCalled = true;
				return fn.apply(arg1);
			});
		}

		public Boolean isCalled(){
			return isCalled;
		}

	}

	public static class Spy2<T1,T2,T3> {
		private Function2<T1,T2,T3> fn;
		private Boolean isCalled = false;

		public static <T1,T2,T3>Spy2<T1,T2,T3> spy(Function2<T1,T2,T3> fn){
			var spy = new Spy2<T1,T2,T3>();
			spy.fn = fn;
			return spy;
		}

		public Function2<T1,T2,T3>  fn(){
			return Function((arg1, arg2) -> {
				isCalled = true;
				return fn.apply(arg1,arg2);
			});
		}

		public Boolean isCalled(){
			return isCalled;
		}

	}

}
