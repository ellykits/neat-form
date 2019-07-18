package com.nerdstone.neatformcore.domain.model.form;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NForm implements Serializable {

    @SerializedName("form")
    private String formName;

    @SerializedName("is_multi_step")
    private Boolean isMultiStep;

    @SerializedName("rules_file")
    private String rulesFile;

    @SerializedName("count")
    private int count;

    @SerializedName("steps")
    private List<NFormContent> steps;

    public NForm() {
    }

    public NForm(String formName) {
        this.formName = formName;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public Boolean isMultiStep() {
        return isMultiStep;
    }

    public void setMultiStep(Boolean multiStep) {
        isMultiStep = multiStep;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<NFormContent> getSteps() {
        return steps;
    }

    public void setSteps(List<NFormContent> steps) {
        this.steps = steps;
    }

    public String getRulesFile() {
        return rulesFile;
    }

    public void setRulesFile(String rulesFile) {
        this.rulesFile = rulesFile;
    }
}
