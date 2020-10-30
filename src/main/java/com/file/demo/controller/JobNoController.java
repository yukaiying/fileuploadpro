package com.file.demo.controller;

import com.file.demo.dao.JobNoDao;
import com.file.demo.dao.StudentDao;
import com.file.demo.entity.JobNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/jobNo")
public class JobNoController {
    @Autowired
    private JobNoDao jobNoDao;

    @Autowired
    private StudentDao studentDao;

    @PostMapping("/byJobId/{id}")
    @ResponseBody
    public List<JobNo> showStudentNoById(@PathVariable("id") Integer id){
        return jobNoDao.findAllByJobId(id);
    }
}
