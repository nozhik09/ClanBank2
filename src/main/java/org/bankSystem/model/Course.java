package model;

import java.time.LocalDate;

public class Course {

    //    private Currency currency;
    private double course;
    LocalDate localDate;

    public Course(double course) {
        this.course = course;
        this.localDate = LocalDate.now();
    }

    public double getCourse() {
        return course;
    }

    public void setCourse(double course) {
        this.course = course;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    @Override
    public String toString() {

        return"Курс=" + course +
                ", Дата изменения курса =" + localDate +
                '}';
    }
}
