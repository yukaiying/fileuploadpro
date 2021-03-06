package com.file.demo.controller;

import com.file.demo.dao.StudentClassDao;
import com.file.demo.dao.StudentDao;
import com.file.demo.entity.Student;
import com.file.demo.entity.StudentClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentDao studentDao;

    @Autowired
    private StudentClassDao studentClassDao;



    @PostMapping("/update")
    public String updateStudent(Student student,HttpServletRequest request){
        if(student.getPassChange() != null && student.getPassChange() == 1){
            student.setPassWord(DigestUtils.md5DigestAsHex(student.getPassWord().getBytes()));
        }
        student = studentDao.save(student);
        student.setStudentClassName(studentClassDao.findById(student.getClassId()).orElseGet(StudentClass::new).getClassName());
        request.getSession().setAttribute("loginUser", student);
        return "redirect:/job";
    }

    @GetMapping("/info")
    public String studentUpdate(Model model, HttpServletRequest request){
        Student student = (Student) request.getSession().getAttribute("loginUser");
        model.addAttribute("student", student);
        model.addAttribute("studentClassList", studentClassDao.findAll());
        return "studentinfo";
    }
}
