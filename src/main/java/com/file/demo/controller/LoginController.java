package com.file.demo.controller;

import com.file.demo.dao.StudentDao;
import com.file.demo.entity.Student;
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

    @GetMapping("/login")
    public String showLoginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, @RequestParam("userName") String userName, @RequestParam("passWord") String passWord, HttpServletRequest request){
        Student student = studentDao.findByUserNameAndPassWord(userName, DigestUtils.md5DigestAsHex(passWord.getBytes()));
        if(student != null && student.getId() != null){
            request.getSession().setAttribute("loginUser", student);
            return "redirect:/job";
        }
        model.addAttribute("error", "登录失败:用户名或密码错误");
        return  "login";
    }
}
