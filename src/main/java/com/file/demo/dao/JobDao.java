package com.file.demo.dao;

import com.file.demo.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobDao extends JpaRepository<Job, Integer> {

    List<Job> findAllByClassNameAndCourseOrderByIdDesc(String className, String course);
    List<Job> findAllByClassNameOrderByIdDesc(String className);
    List<Job> findAllByOrderByIdDesc();
}
