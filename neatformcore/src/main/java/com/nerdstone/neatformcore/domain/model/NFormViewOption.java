package com.nerdstone.neatformcore.domain.model;

import android.view.View;
import java.io.Serializable;
import java.util.Map;

public class NFormViewOption implements Serializable {

  private String name;
  private Object value;
  private View view;
  private Map<String, Object> metadata;

  public NFormViewOption() {
  }

  public NFormViewOption(View view) {
    this.view = view;
  }

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

  public View getView() {
    return view;
  }

  public void setView(View view) {
    this.view = view;
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }
}
