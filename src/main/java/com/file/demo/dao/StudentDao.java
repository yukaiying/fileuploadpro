package com.file.demo.dao;

import com.file.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDao extends JpaRepository<Student, Integer> {

    public Student findByUserNameAndPassWord(String userName, String passWord);

    public Student findByNumOrUserName(String num,String userName);
}
