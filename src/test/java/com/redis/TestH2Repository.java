package com.redis;

import com.redis.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestH2Repository extends JpaRepository<Student,Integer> {
    public Student findByStudentName(String studentName);
}
