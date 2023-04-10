package com.student.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Serializable>{

}
