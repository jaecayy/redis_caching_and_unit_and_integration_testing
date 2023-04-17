package com.redis.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "student")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentRollNo;
    private String studentName;
    private String studentContact;
    private String studentMail;

    public Student(String studentName, String studentContact, String studentMail) {
        this.studentName = studentName;
        this.studentContact = studentContact;
        this.studentMail = studentMail;
    }
}
