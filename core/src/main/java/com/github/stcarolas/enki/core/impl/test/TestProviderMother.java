package com.github.stcarolas.enki.core.impl.test;

import java.util.Arrays;

public class TestProviderMother {

    public static TestProvider testProvider(){
        return TestProvider.builder()
            .testRepos(Arrays.asList(new TestRepo("first")))
            .build();
    }
}
