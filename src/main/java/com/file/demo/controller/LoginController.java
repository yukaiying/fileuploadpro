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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private StudentClassDao studentClassDao;


    @GetMapping({"/login","/"})
    public String showLoginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, @RequestParam("userName") String userName, @RequestParam("passWord") String passWord, HttpServletRequest request){
        Student student = studentDao.findByUserNameAndPassWord(userName, DigestUtils.md5DigestAsHex(passWord.getBytes()));
        if(student != null && student.getId() != null){
            student.setStudentClassName(studentClassDao.findById(student.getClassId()).orElseGet(StudentClass::new).getClassName());
            request.getSession().setAttribute("loginUser", student);
            return "redirect:/job";
        }
        model.addAttribute("error", "登录失败:用户名或密码错误");
        return  "login";
    }

    @GetMapping("/register")
    public String showStudentInfoPage(Model model){
        model.addAttribute("studentClassList", studentClassDao.findAll());
        return "register";
    }

    @PostMapping("/register")
    public String saveStudent(Model model,Student student){
        Student oldStudent = studentDao.findByNumOrUserName(student.getNum(), student.getUserName());
        if(oldStudent == null){
            student.setType(1);
            student.setPassWord(DigestUtils.md5DigestAsHex(student.getPassWord().getBytes()));
            studentDao.save(student);
            return "redirect:/login";
        }else {
            model.addAttribute("error","已经存在相同的学号或者是用户名，不允许重复注册。请登录试试");
            return "error";
        }
    }
    @GetMapping("/left")
    public String left(){
        return "left";
    }

    @GetMapping("/forgetPassWord")
    public String forgetPassWord(){
        return "forgetpassword";
    }

    @PostMapping("/backPassWord")
    public String backPassWord(Student student, Model model){
        Student oldStudent = studentDao.findByNumOrUserName(student.getNum(), student.getUserName());
        if(oldStudent != null && oldStudent.getId() != null){
            oldStudent.setPassWord(DigestUtils.md5DigestAsHex(student.getPassWord().getBytes()));
            studentDao.save(oldStudent);
        }else{
            model.addAttribute("error", "没匹配到用户名和考号，注册试试");
            return "error";
        }
        return "redirect:/";
    }
}
