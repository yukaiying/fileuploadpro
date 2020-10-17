package com.file.demo.dao;

import com.file.demo.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobDao extends JpaRepository<Job, Integer> {
}
