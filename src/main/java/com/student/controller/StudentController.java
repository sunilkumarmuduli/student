package com.student.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.entity.Student;
import com.student.repository.StudentRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class StudentController {

	@Autowired
	private StudentRepository studentRepository;

	@PostMapping("/students")
	public ResponseEntity<?> createStudent(@Valid @RequestBody Student student) {
		// Check that the firstName and lastName fields have a minimum length of 3
		// characters
		if (student.getFirstName().length() < 3 || student.getLastName().length() < 3) {
			return ResponseEntity.badRequest()
					.body("First name and last name must have a minimum length of 3 characters");
		}

		// Check that the dob field is mandatory and that the student's age is greater
		// than 15 years and less than or equal to 20 years
		LocalDate dob = student.getDob();
		if (dob == null) {
			return ResponseEntity.badRequest().body("DOB is mandatory");
		}
		LocalDate now = LocalDate.now();
		int age = Period.between(dob, now).getYears();
		if (age <= 15 || age > 20) {
			return ResponseEntity.badRequest()
					.body("Age must be greater than 15 years and less than or equal to 20 years");
		}

		// Check that the section field has a valid value of A, B, or C
		String section = student.getSection();
		if (!Arrays.asList("A", "B", "C").contains(section)) {
			return ResponseEntity.badRequest().body("Section must have a valid value of A, B, or C");
		}

		// Check that the gender field has a valid value of M or F
		String gender = student.getGender();
		if (!Arrays.asList("M", "F").contains(gender)) {
			return ResponseEntity.badRequest().body("Gender must have a valid value of M or F");
		}

		// Check that the marks1, marks2, and marks3 fields are within the range of 0 to
		// 100
		Integer marks1 = student.getMarks1();
		Integer marks2 = student.getMarks2();
		Integer marks3 = student.getMarks3();
		if (marks1 == null || marks1 < 0 || marks1 > 100 || marks2 == null || marks2 < 0 || marks2 > 100
				|| marks3 == null || marks3 < 0 || marks3 > 100) {
			return ResponseEntity.badRequest().body("Marks must be within the range of 0 to 100");
		}

		// Calculate total, average, and result
		int totalMarks = (student.getMarks1() != 0 ? student.getMarks1() : 0)
				+ (student.getMarks2() != 0 ? student.getMarks2() : 0)
				+ (student.getMarks3() != 0 ? student.getMarks3() : 0);
		double averageMarks = totalMarks / 3.0;
		boolean result = (student.getMarks1() != 0 && student.getMarks1() >= 35)
				&& (student.getMarks2() != 0 && student.getMarks2() >= 35)
				&& (student.getMarks3() != 0 && student.getMarks3() >= 35);

		// Create a new student object
		Student newStudent = new Student();
		newStudent.setFirstName(student.getFirstName());
		newStudent.setLastName(student.getLastName());
		newStudent.setDob(student.getDob());
		newStudent.setSection(student.getSection());
		newStudent.setGender(student.getGender());
		newStudent.setMarks1(student.getMarks1());
		newStudent.setMarks2(student.getMarks2());
		newStudent.setMarks3(student.getMarks3());
		newStudent.setTotal(totalMarks);
		newStudent.setAverage(averageMarks);
		if (result) {
			newStudent.setResult("PASS");
		}else {
			newStudent.setResult("FAIL");
		}

		// Save to database
		newStudent = studentRepository.save(newStudent);

		return ResponseEntity.ok(newStudent);
	}

	@PutMapping("/students/{id}")
	public ResponseEntity<Student> updateStudentMarks(@PathVariable Long id, @RequestBody Student student) {
		// Find student by ID
		Optional<Student> optionalStudent = studentRepository.findById(id);
		if (!optionalStudent.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		// Validate inputs
		if (student.getMarks1() == 0 || student.getMarks2() == 0 || student.getMarks3() == 0) {
			return ResponseEntity.badRequest().build();
		}
		if (student.getMarks1() < 0 || student.getMarks1() > 100 || student.getMarks2() < 0 || student.getMarks2() > 100
				|| student.getMarks3() < 0 || student.getMarks3() > 100) {
			return ResponseEntity.badRequest().build();
		}

		// Calculate total, average, and result
		int totalMarks = student.getMarks1() + student.getMarks2() + student.getMarks3();
		double averageMarks = totalMarks / 3.0;
		boolean result = student.getMarks1() >= 35 && student.getMarks2() >= 35 && student.getMarks3() >= 35;

		// Update student object
		Student updatedStudent = optionalStudent.get();
		updatedStudent.setMarks1(student.getMarks1());
		updatedStudent.setMarks2(student.getMarks2());
		updatedStudent.setMarks3(student.getMarks3());
		updatedStudent.setTotal(totalMarks);
		updatedStudent.setAverage(averageMarks);
		if (result) {
			updatedStudent.setResult("PASS");
		}else {
			updatedStudent.setResult("FAIL");
		}
		

		// Save to database
		updatedStudent = studentRepository.save(updatedStudent);

		return ResponseEntity.ok(updatedStudent);
	}
}
