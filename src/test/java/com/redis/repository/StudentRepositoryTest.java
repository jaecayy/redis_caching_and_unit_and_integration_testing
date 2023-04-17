package com.redis.repository;

import com.redis.TestH2Repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import com.redis.entities.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentRepositoryTest {
    private static Logger logger = LoggerFactory.getLogger(StudentRepositoryTest.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestH2Repository testH2Repository;

    @AfterEach
    public void tearDown(){
        logger.info("Tearing down is called.");
        testH2Repository.deleteAll();
    }

    @Test
//    @Sql(statements = "delete from student",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByStudentRollNo() {

        Student student = new Student("Jitesh Khatri","7503921723","jiteshkhatri2000@gmail.com");
        Student saveStudent = testH2Repository.save(student);

        Student actualStudent = studentRepository.findByStudentRollNo(saveStudent.getStudentRollNo()).get();
        assertEquals("Jitesh Khatri",actualStudent.getStudentName());
        assertEquals("7503921723",actualStudent.getStudentContact());
        logger.info("saved user in DB roll no is - {} ",actualStudent.getStudentRollNo());
        assertEquals(1,testH2Repository.findAll().size());
    }


}