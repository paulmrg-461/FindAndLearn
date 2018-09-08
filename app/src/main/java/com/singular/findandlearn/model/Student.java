package com.singular.findandlearn.model;

/**
 * Created by Paul Realpe on 24/03/2018.
 */

public class Student {
    private int id_student;
    private String img_student;
    private String firstname_student;
    private String lastname_student;
    private String nickname_student;
    private String password_student;
    private int age_student;
    private String gender_student;
    private String program_student;
    private int semester_student;
    private float latitude_student;
    private float length_student;
    private int points_student;

    public Student(String img_student, String nickname_student, float latitude_student, String program_student, int points_student){
        this.img_student = img_student;
        this.nickname_student = nickname_student;
        this.latitude_student = latitude_student;
        this.program_student = program_student;
        this.points_student = points_student;
    }
    public int getId_student() {
        return id_student;
    }

    public void setId_student(int id_student) {
        this.id_student = id_student;
    }

    public String getImg_student() {
        return img_student;
    }

    public void setImg_student(String img_student) {
        this.img_student = img_student;
    }

    public String getFirstname_student() {
        return firstname_student;
    }

    public void setFirstname_student(String firstname_student) {
        this.firstname_student = firstname_student;
    }

    public String getLastname_student() {
        return lastname_student;
    }

    public void setLastname_student(String lastname_student) {
        this.lastname_student = lastname_student;
    }

    public String getNickname_student() {
        return nickname_student;
    }

    public void setNickname_student(String nickname_student) {
        this.nickname_student = nickname_student;
    }

    public String getPassword_student() {
        return password_student;
    }

    public void setPassword_student(String password_student) {
        this.password_student = password_student;
    }

    public int getAge_student() {
        return age_student;
    }

    public void setAge_student(int age_student) {
        this.age_student = age_student;
    }

    public String getGender_student() {
        return gender_student;
    }

    public void setGender_student(String gender_student) {
        this.gender_student = gender_student;
    }

    public String getProgram_student() {
        return program_student;
    }

    public void setProgram_student(String program_student) {
        this.program_student = program_student;
    }

    public int getSemester_student() {
        return semester_student;
    }

    public void setSemester_student(int semester_student) {
        this.semester_student = semester_student;
    }

    public float getLatitude_student() {
        return latitude_student;
    }

    public void setLatitude_student(float latitude_student) {
        this.latitude_student = latitude_student;
    }

    public float getLength_student() {
        return length_student;
    }

    public void setLength_student(float length_student) {
        this.length_student = length_student;
    }

    public int getPoints_student() {
        return points_student;
    }

    public void setPoints_student(int points_student) {
        this.points_student = points_student;
    }

    /**
     * Compara los atributos de dos estudiantes
     * @param student Estudiante externo
     * @return true si son iguales, false si hay cambios
     */
    public boolean compararCon(Student student) {
        return this.img_student.compareTo(student.img_student) == 0 &&
                this.firstname_student.compareTo(student.firstname_student) == 0 &&
                this.lastname_student.compareTo(student.lastname_student) == 0 &&
                this.nickname_student.compareTo(student.nickname_student) == 0 &&
                this.password_student.compareTo(student.password_student) == 0 &&
                this.age_student == student.age_student &&
                this.gender_student.compareTo(student.gender_student) == 0 &&
                this.program_student.compareTo(student.program_student) == 0 &&
                this.semester_student == student.semester_student &&
                this.latitude_student == student.latitude_student &&
                this.length_student == student.length_student &&
                this.points_student == student.points_student;
    }
}
