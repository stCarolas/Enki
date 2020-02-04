package com.github.stcarolas.enki;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MavenPluginExecution {
  private List<String> goals;
  private String id;
  private String phase;
  private boolean inherited;
  private int priority;
}
