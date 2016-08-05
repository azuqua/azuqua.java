package com.azuqua.java.models;

/**
 * Created by SASi on 06-Jan-16.
 */
public class Blob {

    private String id;
    private String name;
    private String operation;
    private String module;
    private String description;
    private String type;
    private Boolean isFlojet;
    private String configurationscheme;
    private Object inputs;
    private Object outputs;
    private Object triggerParams;
    private Object uiParams;
    private Object floapp;
    private int step;

    public Blob() {
    }

    public Blob(String id, String name, String operation, String module, String description, String type, Boolean isFlojet, String configurationscheme, Object inputs, Object outputs, Object triggerParams, Object uiParams, Object floapp, int step) {
        this.id = id;
        this.name = name;
        this.operation = operation;
        this.module = module;
        this.description = description;
        this.type = type;
        this.isFlojet = isFlojet;
        this.configurationscheme = configurationscheme;
        this.inputs = inputs;
        this.outputs = outputs;
        this.triggerParams = triggerParams;
        this.uiParams = uiParams;
        this.floapp = floapp;
        this.step = step;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsFlojet() {
        return isFlojet;
    }

    public void setIsFlojet(Boolean isFlojet) {
        this.isFlojet = isFlojet;
    }

    public String getConfigurationscheme() {
        return configurationscheme;
    }

    public void setConfigurationscheme(String configurationscheme) {
        this.configurationscheme = configurationscheme;
    }

    public Object getInputs() {
        return inputs;
    }

    public void setInputs(Object inputs) {
        this.inputs = inputs;
    }

    public Object getOutputs() {
        return outputs;
    }

    public void setOutputs(Object outputs) {
        this.outputs = outputs;
    }

    public Object getTriggerParams() {
        return triggerParams;
    }

    public void setTriggerParams(Object triggerParams) {
        this.triggerParams = triggerParams;
    }

    public Object getUiParams() {
        return uiParams;
    }

    public void setUiParams(Object uiParams) {
        this.uiParams = uiParams;
    }

    public Object getFloapp() {
        return floapp;
    }

    public void setFloapp(Object floapp) {
        this.floapp = floapp;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
