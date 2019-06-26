package com.opensrp.neatformcore.domain.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

public class NFormSubViewProperty implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("label")
    private String label;

    @SerializedName("is_exclusive")
    private Boolean isExclusive;

    @SerializedName("metadata")
    private Map<String, Object> viewMetadata;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean isExclusive() {
        return isExclusive;
    }

    public void setExclusive(Boolean exclusive) {
        isExclusive = exclusive;
    }

    public Map<String, Object> getViewMetadata() {
        return viewMetadata;
    }

    public void setViewMetadata(Map<String, Object> viewMetadata) {
        this.viewMetadata = viewMetadata;
    }
}
