package com.redis.service;

import com.redis.entities.Student;
import com.redis.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("studentService")
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    @Autowired
    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student) {
        logger.info("Creating student with name - {} ",student.getStudentName());
        return studentRepository.save(student);
    }

    @Cacheable(cacheNames = "allStudents")
    public List<Student> getStudents() {
        logger.info("Fetching data from DB");
        return studentRepository.findAll();
    }

    @Cacheable(cacheNames = "students",key = "#rollNo")
    public Student getStudentByRollNo(int rollNo) {
        logger.info("Fetching student from DB using Roll No. - {}",rollNo);
        return studentRepository.findByStudentRollNo(rollNo).orElse(null);
    }


    @CacheEvict(cacheNames = "students",key = "#rollNo")
    public Student deleteStudent(int rollNo) {
        Student student = null;
        Optional optional = studentRepository.findById(rollNo);
        if(optional.isPresent()){
            student = studentRepository.findById(rollNo).get();
            studentRepository.deleteById(rollNo);
        }
        return student;
    }

    @CachePut(cacheNames = "students",key = "#rollNo")
    public Student updateStudent(int rollNo,Student student) {
        Student existingStudent = studentRepository.findByStudentRollNo(rollNo).orElse(null);
        existingStudent.setStudentName(student.getStudentName());
        existingStudent.setStudentContact(student.getStudentContact());
        existingStudent.setStudentMail(student.getStudentMail());
        logger.info("Student with roll no. = {} is updated successfully",rollNo);
        return studentRepository.save(existingStudent);
    }


    @Scheduled(initialDelay = 60000, fixedRate = 60000)
    @CacheEvict(value = "allStudents", allEntries = true)
    public void clearCache() {
        logger.info("old cache deleted , new cache is created");
        getStudents();
    }
}
