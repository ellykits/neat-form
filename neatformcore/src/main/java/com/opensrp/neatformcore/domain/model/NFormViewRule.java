package com.opensrp.neatformcore.domain.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NFormViewRule implements Serializable {

    @SerializedName("action")
    private String action;

    @SerializedName("condition")
    private String condition;

    public NFormViewRule() {
    }

    public NFormViewRule(String action, String condition) {
        this.action = action;
        this.condition = condition;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
