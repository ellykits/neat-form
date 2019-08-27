package com.nerdstone.neatformcore.domain.model;

import android.view.View;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class NFormViewDetails implements Serializable {

    private String name;
    private Object value;
    private View view;
    private Map<String, Object> metadata;
    private List<String> subjects;

    public NFormViewDetails() {
    }

    public NFormViewDetails(View view) {
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

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }
}
