package Unittests.TestingClasses.school;


import ORM.Annotations.*;
import lombok.Data;

import java.util.ArrayList;

@Data
@Table(name = "COURSES")
public class Course 
{
    /** ID. */
    @ID
    protected Long id;
    
    /** Name. */
    @Column
    protected String name;
    
    /** Active flag. */
    @Column
    protected boolean active;
    
    /** Teacher. */
    @ManyToOne()
    protected Person teacher;
    
    /** Students. */
    @OneToMany()
    protected ArrayList<Person> students;

}
