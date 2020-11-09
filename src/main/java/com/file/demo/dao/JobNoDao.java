package com.file.demo.dao;

import com.file.demo.entity.JobNo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobNoDao extends JpaRepository<JobNo, Integer> {

    public List<JobNo> findAllByJobId(Integer id);

    public void deleteByJobIdAndStudentId(Integer jobId, Integer studentId);

    public void deleteByJobId(Integer id);

}
