package org.example;

import java.io.Serializable;

public class Patient extends person implements Serializable {
    String disease;

    public Patient(String name, int age, String gender, String disease) {
        super(name, age, gender);
        this.disease = disease;
    }


    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }


}
