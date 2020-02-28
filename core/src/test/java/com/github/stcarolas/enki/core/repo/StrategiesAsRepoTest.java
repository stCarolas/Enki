package com.github.stcarolas.enki.core.repo;

import java.io.File;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.TestRepo;
import static io.vavr.API.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class StrategiesAsRepoTest {
	StrategiesAsRepo repo = new StrategiesAsRepo();

	@Test
	public void test_null_name_on_missing_strategy(){
		assertNull(repo.name());
	}

	@Test
	public void test_null_name_if_strategy_provide_null_name(){
		assertNull(
			repo.setNameStrategy(() -> null).name()
		);
	}

	@Test
	public void test_null_name_if_strategy_falls(){
		assertNull(
			repo.setNameStrategy(() -> {throw new RuntimeException();}).name()
		);
	}

	@Test
	public void test_name_as_strategy_provides(){
		var value = "value";
		assertEquals(
			value, 
			repo.setNameStrategy(() -> value).name()
		);
	}

	@Test
	public void test_null_id_on_missing_strategy(){
		assertNull(repo.id());
	}

	@Test
	public void test_null_id_if_strategy_provide_null_name(){
		assertNull(
			repo.setIdentityStrategy(() -> null).id()
		);
	}

	@Test
	public void test_null_id_if_strategy_falls(){
		assertNull(
			repo.setIdentityStrategy(() -> {throw new RuntimeException();}).id()
		);
	}

	@Test
	public void test_same_id_as_strategy_provides(){
		var value = "value";
		assertEquals(
			value, 
			repo.setIdentityStrategy(() -> value).id()
		);
	}

	@Test
	public void test_null_directory_on_missing_strategy(){
		assertNull(repo.directory());
	}

	@Test
	public void test_null_directory_if_strategy_provide_null_name(){
		assertNull(
			repo.setDirectoryStrategy(() -> null).directory()
		);
	}

	@Test
	public void test_null_directory_if_strategy_falls(){
		assertNull(
			repo.setDirectoryStrategy(() -> {throw new RuntimeException();}).directory()
		);
	}

	@Test
	public void test_same_directory_as_strategy_provides(){
		var file = new File("/tmp/test");
		assertEquals(
			file,
			repo.setDirectoryStrategy(() -> file).directory()
		);
	}

	@Test
	public void test_return_null_on_missing_commit_strategy(){
		assertNull(repo.directory());
	}

	@Test
	public void test_return_null_if_commit_strategy_falls(){
		assertNull(
			repo.setCommitStrategy( arg -> {throw new RuntimeException();} ).commit("some arg")
		);
	}

	@Test
	public void test_return_null_if_commit_strategy_provide_null_repo(){
		assertNull(
			repo.setCommitStrategy( arg -> null ).commit("some arg")
		);
	}

	@Test
	public void test_calling_commit_strategy_with_right_args(){
		var testRepo = new TestRepo();
		var message = "some message";

		var strategy = mock(Function.class);
		when(strategy.apply(any())).thenReturn(testRepo);

		assertEquals(testRepo, repo.setCommitStrategy(strategy).commit(message));
		verify(strategy).apply(message);
	}

	@Test
	public void test_return_empty_seq_on_missing_providers_strategy(){
		assertTrue(repo.providers().isEmpty());
	}

	@Test
	public void test_return_empty_seq_if_providers_strategy_falls(){
		assertTrue(
			repo.setProvidersStrategy( () -> {throw new RuntimeException();})
				.providers().isEmpty()
		);
	}

	@Test
	public void test_return_empty_seq_if_provider_strategy_provide_null(){
		assertTrue(repo.setProvidersStrategy( () -> null ).providers().isEmpty());
	}

	@Test
	public void test_calling_provider_strategy(){
		var strategy = mock(Supplier.class);
		var providers = Seq();
		when(strategy.get()).thenReturn(providers);
		assertEquals(
			providers, 
			repo.setProvidersStrategy(strategy).providers()
		);
	}

}
