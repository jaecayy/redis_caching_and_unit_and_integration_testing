package com.redis.controller;

import com.redis.service.StudentService;
import com.redis.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping(value="/")
    public Student createStudent(@RequestBody Student student){
        return studentService.saveStudent(student);
    }

    @GetMapping(value = "/")
    public List<Student> getAllStudents(){
        return studentService.getStudents();
    }

    @GetMapping(value = "/{rollNo}")
    public Student getStudentByRollNo(@PathVariable Integer rollNo){
        return studentService.getStudentByRollNo(rollNo);
    }

    @PutMapping(value = "/{rollNo}")
    public Student updateStudentByRollNo(@PathVariable Integer rollNo , @RequestBody Student student){
        return studentService.updateStudent(rollNo,student);
    }

    @DeleteMapping(value="/{rollNo}")
    public Student deleteStudentByRollNo(@PathVariable Integer rollNo){
        return studentService.deleteStudent(rollNo);
    }
}
