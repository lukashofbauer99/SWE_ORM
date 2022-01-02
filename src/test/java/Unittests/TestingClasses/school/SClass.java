package Unittests.TestingClasses.school;

import ORM.Annotations.Column;
import ORM.Annotations.ID;
import ORM.Annotations.ManyToOne;
import ORM.Annotations.Table;
import lombok.Data;

@Data
@Table(name = "CLASSES")
public class SClass 
{
    /** ID. */
    @ID
    protected Long id;
    
    /** Name. */
    @Column
    protected String name;
    
    /** Teacher. */
    @ManyToOne
    protected Person teacher;

}
