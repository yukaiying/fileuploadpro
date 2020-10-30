package com.file.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "tab_job_no")
public class JobNo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "student_name")
    private String studentName;

    public JobNo() {
    }

    public JobNo(Integer jobId, Integer studentId, String studentName){
        this.jobId = jobId;
        this.studentId = studentId;
        this.studentName = studentName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
