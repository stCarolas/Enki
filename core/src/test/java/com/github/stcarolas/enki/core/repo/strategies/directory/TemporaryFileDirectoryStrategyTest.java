package com.github.stcarolas.enki.core.repo.strategies.directory;

import static io.vavr.API.None;
import static io.vavr.API.Some;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.github.stcarolas.enki.core.util.Spy1.spy;

import java.io.File;

import com.github.stcarolas.enki.core.TestRepo;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.vavr.Function1;

public class TemporaryFileDirectoryStrategyTest {

	TemporaryFileDirectoryStrategy strategy = ((TemporaryFileDirectoryStrategy)TemporaryFileDirectoryStrategy.TemporaryDirectory(null));

	@Nested
	public class TestForConstructDirectoryPath{
		@Test
		public void test_Success(){
			var fn = strategy.getConstructDirectoryPath();
			var result = fn.apply("name");
			assertNotNull(result);
			assertEquals(TemporaryFileDirectoryStrategy.TEMPORARY_LOCATION + "name", result.getPath());
		}
	}

	@Nested
	public class TestForCreateDirIfMissing{
		@Test
		public void test_Success_if_file_doesnt_exist(){
			var fn = TemporaryFileDirectoryStrategy.builder()
				.repo(None())
				.build()
				.getCreateDirIfMissing();
			var file = mock(File.class);
			when(file.exists()).thenReturn(false);
			assertEquals(file, fn.apply(file));
			verify(file, times(1)).mkdir();
		}

		@Test
		public void test_Success_if_file_exists(){
			var fn = TemporaryFileDirectoryStrategy.builder()
				.repo(None())
				.build()
				.getCreateDirIfMissing();
			var file = mock(File.class);
			when(file.exists()).thenReturn(true);
			assertEquals(file, fn.apply(file));
			verify(file, times(0)).mkdir();
		}

		@Test
		public void test_Null_if_cant_create_dir(){
			var fn = TemporaryFileDirectoryStrategy.builder()
				.repo(None())
				.build()
				.getCreateDirIfMissing();
			var file = mock(File.class);
			when(file.exists()).thenReturn(false);
			when(file.mkdir()).thenThrow(new RuntimeException("some error"));
			assertNull(fn.apply(file));
			verify(file, times(1)).mkdir();
		}
	}

	@Nested
	public class TestForGet {

		@Test
		public void test_Call_CreateDirIfMissing_with_result_of_ConstructDirectoryPath() {
			var expectedFile = mock(File.class);
			var createDirMock = spy( (Function1<File,File>) file -> file );
			var strategy = TemporaryFileDirectoryStrategy.builder()
				.repo(Some(new TestRepo()))
				.constructDirectoryPath(ignore -> expectedFile)
				.createDirIfMissing(createDirMock.fn())
				.build();
			assertNotNull(strategy.get());
			assertEquals(expectedFile, createDirMock.arg1());
		}

		@Test
		public void test_Success_return_file_as_ConstructDirectoryPath() {
			var expectedFile = mock(File.class);
			var strategy = TemporaryFileDirectoryStrategy.builder()
				.repo(Some(new TestRepo()))
				.constructDirectoryPath(ignore -> mock(File.class))
				.createDirIfMissing(file -> file)
				.build();
			assertEquals(expectedFile, strategy.get());
		}

		@Test
		public void test_Null_if_repo_missing() {
			var strategy = TemporaryFileDirectoryStrategy.builder()
				.repo(None())
				.constructDirectoryPath(ignore -> mock(File.class))
				.createDirIfMissing(file -> file)
				.build();
			assertNull(strategy.get());
		}

		@Test
		public void test_Null_if_repo_without_id() {
			var repo = new TestRepo(){
				@Override
				public String id() {
					return null;
				}
			};
			var strategy = TemporaryFileDirectoryStrategy.builder()
				.repo(Some(repo))
				.constructDirectoryPath(ignore -> mock(File.class))
				.createDirIfMissing(file -> file)
				.build();
			assertNull(strategy.get());
		}

		@Test
		public void test_Null_if_repo_throw_exception_insteadof_id() {
			var repo = new TestRepo(){
				@Override
				public String id() {
					throw new RuntimeException("some error");
				}
			};
			var strategy = TemporaryFileDirectoryStrategy.builder()
				.repo(Some(repo))
				.constructDirectoryPath(ignore -> mock(File.class))
				.createDirIfMissing(file -> file)
				.build();
			assertNull(strategy.get());
		}

	}

}
