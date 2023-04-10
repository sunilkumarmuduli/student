package com.student.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name = "students")
@Data
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "First name is mandatory")
	@Pattern(regexp = "[a-zA-Z]{3,}", message = "First name must have at least 3 alphabetic characters")
	private String firstName;

	@NotBlank(message = "Last name is mandatory")
	@Pattern(regexp = "[a-zA-Z]{3,}", message = "Last name must have at least 3 alphabetic characters")
	private String lastName;

	@NotNull(message = "DOB is mandatory")
	private LocalDate dob;

	@Pattern(regexp = "[ABC]", message = "Section must be A, B or C")
	private String section;

	@Pattern(regexp = "[MF]", message = "Gender must be M or F")
	private String gender;

	@Min(value = 0, message = "Marks range is 0 to 100")
	@Max(value = 100, message = "Marks range is 0 to 100")
	private Integer marks1;

	@Min(value = 0, message = "Marks range is 0 to 100")
	@Max(value = 100, message = "Marks range is 0 to 100")
	private Integer marks2;

	@Min(value = 0, message = "Marks range is 0 to 100")
	@Max(value = 100, message = "Marks range is 0 to 100")
	private Integer marks3;

	private Integer total;

	private double average;

	private String result;
}
