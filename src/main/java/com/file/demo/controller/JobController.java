package com.file.demo.controller;

import com.file.demo.dao.JobDao;
import com.file.demo.entity.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class JobController {

    @Value("${file.upload.url}")
    private String filePath;

    @Autowired
    private JobDao jobDao;

    @GetMapping("/job")
    public String jobPage(Model model) {
        model.addAttribute("list", jobDao.findAll());
        return "joblist";
    }

    @PostMapping("/jobSave")
    public String jobSave(Job job) {
        if (job.getId() == null) {
            job.setCreateTime(new Date());
        }
        jobDao.save(job);
        return "redirect:/job";
    }

    @PostMapping("/jobById/{id}")
    @ResponseBody
    public Job jobById(@PathVariable("id") Integer id) {
        return jobDao.findById(id).orElseGet(Job::new);
    }

    @PostMapping("/fileUpload")
    public String fileUpload(Model model, @RequestParam("file_upload") MultipartFile file, @RequestParam("id") Integer id, @RequestParam("name") String name) {
        if (file.isEmpty()) {
            model.addAttribute("err", "文件为空，请重新上传");
            return "error";
        }
        Job job = jobDao.getOne(id);
        String newFilePath = filePath + "/" + job.getTeacher() + "/" + job.getCourse() + "/" + job.getClassName() + "/" + job.getNum() + "/";
        File newFile = new File(newFilePath);
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fileName = name + sdf.format(new Date()) + "_" + file.getOriginalFilename();
        File newFile1 = new File(newFilePath + fileName);
        try {
            file.transferTo(newFile1);
            job.setStudentNum(job.getStudentNum() == null ? 1 : job.getStudentNum() + 1);
            jobDao.save(job);
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/job";
    }
}
