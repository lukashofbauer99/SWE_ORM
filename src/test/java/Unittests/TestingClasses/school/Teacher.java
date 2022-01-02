package Unittests.TestingClasses.school;

import ORM.Annotations.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Data
@Table(name = "TEACHERS")
@InheritOneToOne
public class Teacher extends Person
{
    @ID
    protected Long id;
    /** Salary. */
    @Column
    protected int salary;
    
    /** Hire date. */
    @Column
    protected Calendar hireDate;
    
    /** Classes. */
    @OneToMany()//
    protected List<SClass> classes;
    
    /** Courses. */
    @OneToMany()//
    protected List<Course> courses;

    @ManyToMany()//
    protected ArrayList<Student> students;

}
