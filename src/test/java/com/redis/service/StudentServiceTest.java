package com.redis.service;

import com.redis.TestH2Repository;
import com.redis.entities.Student;
import com.redis.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class StudentServiceTest {

    @Autowired
    private TestH2Repository testH2Repository;

    @Mock
    private StudentRepository studentRepository;

    private StudentService studentService;
    @BeforeEach
    void setUp() {
        this.studentService = new StudentService(this.studentRepository);
    }

    @Test
    void saveStudent() {
        Student student = new Student("Harsh","8769870983","harsh.gupta@gmail.com");

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student savedStudent  = studentService.saveStudent(student);
        assertThat(savedStudent.getStudentName()).isNotNull();

    }

    @Test
    void getStudents() {
        //Method 1
//        studentService.getStudents();
//        verify(studentRepository).findAll();

        //Method 2
        Student student = new Student("Harsh","8769870983","harsh.gupta@gmail.com");
        List<Student> studentList = new ArrayList<>();
        studentList.add(student);

        when(studentRepository.findAll()).thenReturn(studentList);

        List<Student> fetchedListOfStudent = studentService.getStudents();
        assertThat(fetchedListOfStudent.size()).isGreaterThan(0);
    }

    @Test
    void getStudentByRollNo() {
        Student student = new Student(1,"Harsh","8769870983","harsh.gupta@gmail.com");

        when(studentRepository.findByStudentRollNo(1)).thenReturn(Optional.of(student));

        Student getStudent = studentRepository.findByStudentRollNo(1).get();
        assertEquals(1,getStudent.getStudentRollNo());

    }

    @Test
    void deleteStudent() {
        Student student = new Student(1,"Harsh","8769870983","harsh.gupta@gmail.com");

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1);
        verify(studentRepository,times(1)).deleteById(1);


    }

    @Test
    void updateStudent() {
        Student student = new Student(1,"Harsh","8769870983","harsh.gupta@gmail.com");
        when(studentRepository.findByStudentRollNo(1)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        Student check = studentRepository.findByStudentRollNo(1).get();
        student.setStudentMail("updated@gmail.com");
        Student updatedStudent = studentService.updateStudent(student.getStudentRollNo(),student);
        System.out.println(updatedStudent);
        assertThat(updatedStudent.getStudentMail()).isEqualTo("updated@gmail.com");
    }
}