package com.file.demo.controller;

import com.file.demo.dao.StudentClassDao;
import com.file.demo.dao.StudentDao;
import com.file.demo.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StudentController {
    @Autowired
    private StudentDao studentDao;

    @Autowired
    private StudentClassDao studentClassDao;

    @GetMapping("/student")
    public String showStudentInfoPage(Model model){
        model.addAttribute("studentClassList", studentClassDao.findAll());
        return "studentinfo";
    }

    @PostMapping("/student/insert")
    public String saveStudent(Model model,Student student){
        Student oldStudent = studentDao.findByNum(student.getNum());
        if(oldStudent == null){
            student.setType(1);
            student.setPassWord(DigestUtils.md5DigestAsHex(student.getPassWord().getBytes()));
            studentDao.save(student);
            return "success";
        }else {
            model.addAttribute("error","已经存在相同的学号，不允许重复注册。请登录试试");
            return "error";
        }
    }
}
