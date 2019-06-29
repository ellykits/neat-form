package com.nerdstone.neatformcore.domain.model;

import java.io.Serializable;
import java.util.Map;

public class NFormViewData implements Serializable {

  private String name;
  private Object value;
  private String label;
  private Map<String, Object> metadata;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }
}
