package org.example;

import java.io.Serializable;

public class Doctor extends person implements Serializable {
    String speciality;
    double fee;

    public Doctor(String name, int age, String gender, String speciality, double fee) {
        super(name, age, gender);
        this.speciality = speciality;
        this.fee = fee;
    }

    public String getSpeciality() {
        return speciality;
    }

    public double getFee() {
        return fee;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

}

