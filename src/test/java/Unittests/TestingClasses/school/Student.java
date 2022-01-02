package Unittests.TestingClasses.school;

import ORM.Annotations.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Calendar;

@Data
@Table(name= "STUDENTS")
@InheritOneToOne
public class Student extends Person
{
    @ID
    protected Long id;
    /** Salary. */
    @Column
    protected int grade;
    
    /** Hire date. */
    @Column
    protected Calendar hireDate;

    @ManyToMany()//
    protected ArrayList<Teacher> teachers;
}
