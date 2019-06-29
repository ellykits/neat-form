package com.nerdstone.neatformcore.domain.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class NFormContent implements Serializable {

  @SerializedName("title")
  private String stepName;

  @SerializedName("step_number")
  private int stepNumber;

  @SerializedName("fields")
  private List<NFormViewProperty> fields;

  public NFormContent() {
  }

  public NFormContent(String stepName) {
    this.stepName = stepName;
  }


  public NFormContent(String stepName, int stepNumber, List<NFormViewProperty> fields) {
    this.stepName = stepName;
    this.stepNumber = stepNumber;
    this.fields = fields;
  }

  public String getStepName() {
    return stepName;
  }

  public void setStepName(String stepName) {
    this.stepName = stepName;
  }

  public int getStepNumber() {
    return stepNumber;
  }

  public void setStepNumber(int stepNumber) {
    this.stepNumber = stepNumber;
  }

  public List<NFormViewProperty> getFields() {
    return fields;
  }

  public void setFields(List<NFormViewProperty> fields) {
    this.fields = fields;
  }
}
