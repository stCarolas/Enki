package com.github.stcarolas.enki.discordbot.commands.fill;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.Wither;

@Builder(toBuilder = true)
@Getter
public class FillData {
    @Wither
    private final String repoUrl;
    @Wither
    private final String templateUrl;
    @Singular
    private final Map<String,String> values;
    @Singular
    private final Map<String,String> mappins;
}
