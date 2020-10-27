package com.file.demo.dao;

import com.file.demo.entity.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentClassDao extends JpaRepository<StudentClass, Integer> {

}
