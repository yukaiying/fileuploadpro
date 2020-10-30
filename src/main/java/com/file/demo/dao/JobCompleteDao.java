package com.file.demo.dao;

import com.file.demo.entity.JobComplete;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobCompleteDao extends JpaRepository<JobComplete, Integer> {

    public void deleteByJobIdAndStudentId(Integer jobId, Integer studentId);
}
