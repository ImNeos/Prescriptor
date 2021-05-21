package com.number.app.myapplication.MedScreen;

public class list_model {

    String name;
    String first_name;
    String reg;
    String id;
    String medoc_name;

    public list_model(String name, String first_name, String reg, String id, String medoc_name) {
        this.name = name;
        this.first_name = first_name;
        this.reg = reg;
        this.id = id;
        this.medoc_name = medoc_name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedoc_name() {
        return medoc_name;
    }

    public void setMedoc_name(String medoc_name) {
        this.medoc_name = medoc_name;
    }
}
