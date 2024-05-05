package com.example.entity;

import java.util.List;

public class SignInData {
    private List<Student> students;
    private Check check;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Check getCheck() {
        return check;
    }

    public void setCheck(Check check) {
        this.check = check;
    }
}
