package com.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import com.redis.entities.Student;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedisApplicationTests {

	@LocalServerPort
	private int port;
	private String baseUrl = "http://localhost";

	private static RestTemplate restTemplate;

	@Autowired
	private TestH2Repository testH2Repository;

	@Test
	void contextLoads() {
	}

	@BeforeEach
	public void init() {
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setUp() {
		baseUrl = baseUrl.concat(":").concat(port + "").concat("/student/");
	}

	@Test
	@Sql(statements = "DELETE FROM student WHERE student_name = 'Gagan'",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testAddStudent() {

		Student student = new Student("Gagan", "8509707097", "itsgagakhatri@gmail.com");
		Student response = restTemplate.postForObject(baseUrl, student, Student.class);
		assertEquals("Gagan", response.getStudentName());
		assertEquals(1, testH2Repository.findAll().size());

	}

	@Test
	@Sql(statements = "DELETE FROM student",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "INSERT INTO student (student_name,student_contact, student_mail) VALUES ('harsh','8765869532', 'harsh.gupta@gmail.com')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM student WHERE student_name='harsh'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testGetStudents() {
		List<Student> students = restTemplate.getForObject(baseUrl, List.class);
		assertEquals(1, students.size());
		assertEquals(1, testH2Repository.findAll().size());
	}

	@Test
	@Sql(statements = "INSERT INTO student (student_name,student_contact, student_mail) VALUES ('Yukta','7503921723', 'yukta.khatri@gmail.com')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM student WHERE student_name='Yukta'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testFindStudentByRollNo() {
		Student getStudent = testH2Repository.findByStudentName("Yukta");
		int rollNo = getStudent.getStudentRollNo();
		Student student = restTemplate.getForObject(baseUrl + "/{rollNo}", Student.class, rollNo);
		assertAll(
				() -> assertNotNull(student),
				() -> assertEquals("Yukta", getStudent.getStudentName())
		);

	}

	@Test
	@Sql(statements = "DELETE FROM student",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "INSERT INTO student (student_name,student_contact, student_mail) VALUES ('Laveena','7503921724', 'Laveena.khatri@gmail.com')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM student WHERE student_name='Lavina'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testUpdateStudent(){
		Student getStudent = testH2Repository.findByStudentName("Laveena");
		int rollNo = getStudent.getStudentRollNo();
		Student student = new Student("Lavina", "7503921721", "lavina.khatri@gmail.com");
		restTemplate.put(baseUrl + "/{rollNo}", student, rollNo);
		Student studentFromDB = testH2Repository.findByStudentName("Lavina");
		assertAll(
				() -> assertNotNull(studentFromDB),
				() -> assertEquals("7503921721", studentFromDB.getStudentContact())
		);

	}

	@Test
	@Sql(statements = "DELETE FROM student",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "INSERT INTO student (student_name,student_contact, student_mail) VALUES ('Dipshi','99110066774', 'dipshi.singh@gmail.com')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void testDeleteStudent(){
		int recordCount = testH2Repository.findAll().size();
		Student getFirstStudent = testH2Repository.findAll().get(0);
		assertEquals(1, recordCount);
		restTemplate.delete(baseUrl + "/{rollNo}", getFirstStudent.getStudentRollNo());
		assertEquals(0, testH2Repository.findAll().size());
	}

}
