package Unittests;

import ORM.Cache.BaseCache;
import ORM.DBUtils.Packages.MariaDBUtilPackage;
import ORM.ORM;
import Unittests.TestingClasses.school.Gender;
import Unittests.TestingClasses.school.SClass;
import Unittests.TestingClasses.school.Student;
import Unittests.TestingClasses.school.Teacher;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Unittests {

    ORM orm;

    {
        try {
            orm= new ORM("Unittests.TestingClasses.school",Unittests.class.getClassLoader(),"jdbc:mariadb://172.17.0.2:3306/SWE_ORM", "root", "root",
                    new MariaDBUtilPackage(),new BaseCache());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    public void testCreationOfDatabase()
    {
        orm.dropDatabase();
        Assert.assertTrue(orm.createDatabase());
    }

    @Test
    @Order(2)
    public void saveSimpleEntity()
    {
        Teacher t = new Teacher();
        t.setName("fename");
        t.setFirstName("fefirstname");
        t.setBirthDate(Calendar.getInstance());
        t.setGender(Gender.FEMALE);
        t.setCourses(new ArrayList<>());

        orm.save(t);

        Assert.assertNotNull(t.getId());
    }

    @Test
    @Order(3)
    public void saveComplexEntityWithRelationships()
    {

        Teacher p2 = new Teacher();
        p2.setName("fename");
        p2.setFirstName("fefirstname");
        p2.setBirthDate(Calendar.getInstance());
        p2.setGender(Gender.FEMALE);
        p2.setCourses(new ArrayList<>());

        List<SClass> classes= new ArrayList<>();
        SClass cl1= new SClass();
        SClass cl2= new SClass();
        cl1.setName("lol");
        cl1.setTeacher(p2);
        cl2.setTeacher(p2);
        cl2.setName("lol2");
        classes.add(cl1);
        classes.add(cl2);


        Teacher p = new Teacher();
        p.setName("name");
        p.setFirstName("firstname");
        p.setBirthDate(Calendar.getInstance());
        p.setGender(Gender.MALE);
        p.setClasses(classes);
        p.setCourses(new ArrayList<>());

        Student student= new Student();
        student.setFirstName("stud");
        student.setGrade(1);
        ArrayList<Teacher> teachers = new ArrayList<>();
        teachers.add(p);
        teachers.add(p2);
        student.setTeachers(teachers);

        orm.save(student);

        Assert.assertNotNull(student.getId());
        Assert.assertNotNull(p.getId());
        Assert.assertNotNull(p2.getId());
        Assert.assertNotNull(cl1.getId());
        Assert.assertNotNull(cl2.getId());
    }

    @Test
    @Order(4)
    public void getSimpleEntityWithCache()
    {
        Teacher t = new Teacher();
        t.setName("fename");
        t.setFirstName("fefirstname");
        t.setBirthDate(Calendar.getInstance());
        t.setGender(Gender.FEMALE);
        t.setCourses(new ArrayList<>());

        orm.save(t);

        Assert.assertNotNull(t.getId());

        Teacher teacherStudent= orm.get(Teacher.class,t.getId());
        Assert.assertEquals("fefirstname",teacherStudent.getFirstName());
    }

    @Test
    @Order(5)
    public void getSimpleEntityWithoutCache()
    {
        Teacher t = new Teacher();
        t.setName("fename");
        t.setFirstName("fefirstname");
        t.setBirthDate(Calendar.getInstance());
        t.setGender(Gender.FEMALE);
        t.setCourses(new ArrayList<>());

        orm.save(t);

        Assert.assertNotNull(t.getId());

        Teacher teacherStudent= orm.get(Teacher.class,t.getId());
        Assert.assertEquals("fefirstname",teacherStudent.getFirstName());
    }

    @Test
    @Order(6)
    public void getComplexEntityWithCache()
    {
        Teacher p2 = new Teacher();
        p2.setName("fename");
        p2.setFirstName("fefirstname");
        p2.setBirthDate(Calendar.getInstance());
        p2.setGender(Gender.FEMALE);
        p2.setCourses(new ArrayList<>());

        List<SClass> classes= new ArrayList<>();
        SClass cl1= new SClass();
        SClass cl2= new SClass();
        cl1.setName("lol");
        cl1.setTeacher(p2);
        cl2.setTeacher(p2);
        cl2.setName("lol2");
        classes.add(cl1);
        classes.add(cl2);


        Teacher p = new Teacher();
        p.setName("name");
        p.setFirstName("firstname");
        p.setBirthDate(Calendar.getInstance());
        p.setGender(Gender.MALE);
        p.setClasses(classes);
        p.setCourses(new ArrayList<>());

        Student student= new Student();
        student.setFirstName("stud");
        student.setGrade(1);
        ArrayList<Teacher> teachers = new ArrayList<>();
        teachers.add(p);
        teachers.add(p2);
        student.setTeachers(teachers);

        orm.save(student);

        Assert.assertNotNull(student.getId());
        Assert.assertNotNull(p.getId());
        Assert.assertNotNull(p2.getId());
        Assert.assertNotNull(cl1.getId());
        Assert.assertNotNull(cl2.getId());

        Student loadedStudent= orm.get(Student.class,student.getId());
        Assert.assertEquals("stud",loadedStudent.getFirstName());
        Assert.assertEquals(2,loadedStudent.getTeachers().size());
    }

    @Test
    @Order(7)
    public void getComplexEntityWithoutCache()
    {
        Teacher p2 = new Teacher();
        p2.setName("fename");
        p2.setFirstName("fefirstname");
        p2.setBirthDate(Calendar.getInstance());
        p2.setGender(Gender.FEMALE);
        p2.setCourses(new ArrayList<>());

        List<SClass> classes= new ArrayList<>();
        SClass cl1= new SClass();
        SClass cl2= new SClass();
        cl1.setName("lol");
        cl1.setTeacher(p2);
        cl2.setTeacher(p2);
        cl2.setName("lol2");
        classes.add(cl1);
        classes.add(cl2);


        Teacher p = new Teacher();
        p.setName("name");
        p.setFirstName("firstname");
        p.setBirthDate(Calendar.getInstance());
        p.setGender(Gender.MALE);
        p.setClasses(classes);
        p.setCourses(new ArrayList<>());

        Student student= new Student();
        student.setFirstName("stud");
        student.setGrade(1);
        ArrayList<Teacher> teachers = new ArrayList<>();
        teachers.add(p);
        teachers.add(p2);
        student.setTeachers(teachers);

        orm.save(student);

        Assert.assertNotNull(student.getId());
        Assert.assertNotNull(p.getId());
        Assert.assertNotNull(p2.getId());
        Assert.assertNotNull(cl1.getId());
        Assert.assertNotNull(cl2.getId());

        Student loadedStudent= orm.get(Student.class,student.getId());
        Assert.assertEquals("stud",loadedStudent.getFirstName());
        Assert.assertEquals(2,loadedStudent.getTeachers().size());
    }
    @Test
    @Order(8)
    public void queryComplexEntityWithoutCache()
    {
        Teacher p2 = new Teacher();
        p2.setName("fename");
        p2.setFirstName("fefirstname");
        p2.setBirthDate(Calendar.getInstance());
        p2.setGender(Gender.FEMALE);
        p2.setCourses(new ArrayList<>());

        List<SClass> classes= new ArrayList<>();
        SClass cl1= new SClass();
        SClass cl2= new SClass();
        cl1.setName("lol");
        cl1.setTeacher(p2);
        cl2.setTeacher(p2);
        cl2.setName("lol2");
        classes.add(cl1);
        classes.add(cl2);


        Teacher p = new Teacher();
        p.setName("name");
        p.setFirstName("firstname");
        p.setBirthDate(Calendar.getInstance());
        p.setGender(Gender.MALE);
        p.setClasses(classes);
        p.setCourses(new ArrayList<>());

        Student student= new Student();
        student.setFirstName("stud");
        student.setGrade(1);
        ArrayList<Teacher> teachers = new ArrayList<>();
        teachers.add(p);
        teachers.add(p2);
        student.setTeachers(teachers);

        orm.save(student);

        Assert.assertNotNull(student.getId());
        Assert.assertNotNull(p.getId());
        Assert.assertNotNull(p2.getId());
        Assert.assertNotNull(cl1.getId());
        Assert.assertNotNull(cl2.getId());

        List<Student> students = orm.from(Student.class).where("grade").comparer("=").constVal(1).execute().stream().toList();
        Assert.assertTrue(1<=students.size());
    }
    @Test
    @Order(9)
    public void deleteEntity()
    {
        Teacher p2 = new Teacher();
        p2.setName("fename");
        p2.setFirstName("fefirstname");
        p2.setBirthDate(Calendar.getInstance());
        p2.setGender(Gender.FEMALE);
        p2.setCourses(new ArrayList<>());

        List<SClass> classes= new ArrayList<>();
        SClass cl1= new SClass();
        SClass cl2= new SClass();
        cl1.setName("lol");
        cl1.setTeacher(p2);
        cl2.setTeacher(p2);
        cl2.setName("lol2");
        classes.add(cl1);
        classes.add(cl2);


        Teacher p = new Teacher();
        p.setName("name");
        p.setFirstName("firstname");
        p.setBirthDate(Calendar.getInstance());
        p.setGender(Gender.MALE);
        p.setClasses(classes);
        p.setCourses(new ArrayList<>());

        Student student= new Student();
        student.setFirstName("stud");
        student.setGrade(1);
        ArrayList<Teacher> teachers = new ArrayList<>();
        teachers.add(p);
        teachers.add(p2);
        student.setTeachers(teachers);

        orm.save(student);

        Assert.assertNotNull(student.getId());
        Assert.assertNotNull(p.getId());
        Assert.assertNotNull(p2.getId());
        Assert.assertNotNull(cl1.getId());
        Assert.assertNotNull(cl2.getId());

        orm.delete(student);
        Student loadedStudent= orm.get(Student.class,student.getId());
        Assert.assertEquals(null,loadedStudent);
    }
}
