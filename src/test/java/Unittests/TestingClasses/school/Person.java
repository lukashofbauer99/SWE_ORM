package Unittests.TestingClasses.school;

import ORM.Annotations.Column;
import ORM.Annotations.ID;
import ORM.Annotations.Table;
import lombok.Data;

import java.util.Calendar;

/** This is a person implementation (from School example). */
@Data
@Table(name="People")
public class Person 
{
     /** Instance number counter. */
    protected static int _N = 1;

    /** ID. */
    @ID
    protected Long ids;
    
    /** Name. */
    @Column
    protected String name;
    
    /** First name. */
    @Column
    protected String firstName;
    
    /** Birth date. */
    @Column
    protected Calendar birthDate;
    
    /** Gender. */
    @Column
    protected Gender gender;
    
    /** Instance number. */
    //protected int instanceNumber = _N++;
}
