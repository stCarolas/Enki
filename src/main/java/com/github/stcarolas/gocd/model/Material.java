package com.github.stcarolas.gocd.model;

import java.util.Map;

import lombok.Data;

@Data
public class Material {
  private String type;
  private Map<String, Object> attributes;
  
}
