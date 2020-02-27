package com.github.stcarolas.enki.core.repo;

import java.io.File;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.TestRepo;
import static io.vavr.API.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class StrategiesAsRepoTest {

	@Test
	public void test_null_name_on_missing_strategy(){
		var strategies = new StrategiesAsRepo();
		Assertions.assertNull(strategies.name());
	}

	@Test
	public void test_null_name_if_strategy_provide_null_name(){
		var strategies = new StrategiesAsRepo().setNameStrategy(() -> null);
		Assertions.assertNull(strategies.name());
	}

	@Test
	public void test_null_name_if_strategy_falls(){
		var strategies = new StrategiesAsRepo()
			.setNameStrategy(() -> {throw new RuntimeException();});
		Assertions.assertNull(strategies.name());
	}

	@Test
	public void test_name_as_strategy_provides(){
		var value = "value";
		var strategies = new StrategiesAsRepo().setNameStrategy(() -> value);
		Assertions.assertEquals(value, strategies.name());
	}

	@Test
	public void test_null_id_on_missing_strategy(){
		var strategies = new StrategiesAsRepo();
		Assertions.assertNull(strategies.id());
	}

	@Test
	public void test_null_id_if_strategy_provide_null_name(){
		var strategies = new StrategiesAsRepo().setIdentityStrategy(() -> null);
		Assertions.assertNull(strategies.id());
	}

	@Test
	public void test_null_id_if_strategy_falls(){
		var strategies = new StrategiesAsRepo()
			.setIdentityStrategy(() -> {throw new RuntimeException();});
		Assertions.assertNull(strategies.id());
	}

	@Test
	public void test_same_id_as_strategy_provides(){
		var value = "value";
		var strategies = new StrategiesAsRepo().setIdentityStrategy(() -> value);
		Assertions.assertEquals(value, strategies.id());
	}

	@Test
	public void test_null_directory_on_missing_strategy(){
		var strategies = new StrategiesAsRepo();
		Assertions.assertNull(strategies.directory());
	}

	@Test
	public void test_null_directory_if_strategy_provide_null_name(){
		var strategies = new StrategiesAsRepo().setDirectoryStrategy(() -> null);
		Assertions.assertNull(strategies.directory());
	}

	@Test
	public void test_null_directory_if_strategy_falls(){
		var strategies = new StrategiesAsRepo()
			.setDirectoryStrategy(() -> {throw new RuntimeException();});
		Assertions.assertNull(strategies.directory());
	}

	@Test
	public void test_same_directory_as_strategy_provides(){
		var file = new File("/tmp/test");
		var strategies = new StrategiesAsRepo()
			.setDirectoryStrategy(() -> file);
		Assertions.assertEquals(file,strategies.directory());
	}

	@Test
	public void test_return_null_on_missing_commit_strategy(){
		var strategies = new StrategiesAsRepo();
		Assertions.assertNull(strategies.directory());
	}

	@Test
	public void test_return_null_if_commit_strategy_falls(){
		var strategies = new StrategiesAsRepo()
			.setCommitStrategy( arg -> {throw new RuntimeException();} );
		Assertions.assertNull(strategies.commit("some arg"));
	}

	@Test
	public void test_return_null_if_commit_strategy_provide_null_repo(){
		var strategies = new StrategiesAsRepo()
			.setCommitStrategy( arg -> null );
		Assertions.assertNull(strategies.commit("some arg"));
	}

	@Test
	public void test_calling_commit_strategy_with_right_args(){
		var repo = new TestRepo();
		var message = "some message";

		var strategy = mock(Function.class);
		when(strategy.apply(any())).thenReturn(repo);

		var strategies = new StrategiesAsRepo().setCommitStrategy(strategy);
		Assertions.assertEquals(repo, strategies.commit(message));
		verify(strategy).apply(message);
	}

	@Test
	public void test_return_empty_seq_on_missing_providers_strategy(){
		var strategies = new StrategiesAsRepo();
		Assertions.assertTrue(strategies.providers().isEmpty());
	}

	@Test
	public void test_return_empty_seq_if_providers_strategy_falls(){
		var strategies = new StrategiesAsRepo()
			.setProvidersStrategy( () -> {throw new RuntimeException();} );
		Assertions.assertTrue(strategies.providers().isEmpty());
	}

	@Test
	public void test_return_empty_seq_if_provider_strategy_provide_null(){
		var strategies = new StrategiesAsRepo().setProvidersStrategy( () -> null );
		Assertions.assertTrue(strategies.providers().isEmpty());
	}

	@Test
	public void test_calling_provider_strategy(){
		var strategy = mock(Supplier.class);
		var providers = Seq();
		when(strategy.get()).thenReturn(providers);
		var strategies = new StrategiesAsRepo().setProvidersStrategy(strategy);
		Assertions.assertEquals(providers, strategies.providers());
	}

}
