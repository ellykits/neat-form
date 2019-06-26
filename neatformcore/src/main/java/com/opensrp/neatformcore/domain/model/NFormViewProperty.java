package com.opensrp.neatformcore.domain.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class NFormViewProperty implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("properties")
    private Map<String, Object> viewAttributes;

    @SerializedName("meta_data")
    private Map<String, Object> viewMetadata;

    @SerializedName("rule")
    private List<NFormViewRule> viewRules;

    @SerializedName("options")
    private List<NFormSubViewProperty> options;

    @SerializedName("required_status")
    private String requiredStatus;

    public NFormViewProperty() {
    }

    public NFormViewProperty(String name) {
        this.name = name;
    }

    public NFormViewProperty(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getViewAttributes() {
        return viewAttributes;
    }

    public void setViewAttributes(Map<String, Object> viewAttributes) {
        this.viewAttributes = viewAttributes;
    }

    public Map<String, Object> getViewMetadata() {
        return viewMetadata;
    }

    public void setViewMetadata(Map<String, Object> viewMetadata) {
        this.viewMetadata = viewMetadata;
    }

    public List<NFormViewRule> getViewRules() {
        return viewRules;
    }

    public void setViewRules(List<NFormViewRule> viewRules) {
        this.viewRules = viewRules;
    }

    public List<NFormSubViewProperty> getOptions() {
        return options;
    }

    public void setOptions(List<NFormSubViewProperty> options) {
        this.options = options;
    }

    public String getRequiredStatus() {
        return requiredStatus;
    }

    public void setRequiredStatus(String requiredStatus) {
        this.requiredStatus = requiredStatus;
    }
}
