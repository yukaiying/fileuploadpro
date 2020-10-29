package com.file.demo.controller;

import com.file.demo.dao.CourseDao;
import com.file.demo.dao.JobDao;
import com.file.demo.dao.StudentClassDao;
import com.file.demo.entity.Job;
import com.file.demo.entity.Student;
import com.file.demo.entity.StudentClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class JobController {

    @Value("${file.upload.url}")
    private String filePath;

    @Autowired
    private JobDao jobDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private StudentClassDao studentClassDao;

    @GetMapping("/job")
    public String jobPage(Model model, HttpServletRequest request) {
        jobList(model,request, null, null);
        return "joblist";
    }

    @PostMapping("/job")
    public String jobPage(Model model, @RequestParam("className") String className, @RequestParam("course") String course, HttpServletRequest request) {
        jobList(model,request,className,course);
        return "joblist";
    }

    private void jobList(Model model, HttpServletRequest request, String className, String course){
        model.addAttribute("course", course);
        model.addAttribute("courseList", courseDao.findAll());
        model.addAttribute("studentClassList", studentClassDao.findAll());
        Student student = (Student) request.getSession().getAttribute("loginUser");
        if (student != null) {
            if(2 == student.getType()){
                model.addAttribute("list", jobDao.findAllByOrderByIdDesc());
                model.addAttribute("userType", true);
            }else{
                className = studentClassDao.findById(student.getClassId()).orElseGet(StudentClass::new).getClassName();
                model.addAttribute("list", jobDao.findAllByClassNameOrderByIdDesc(className));
                model.addAttribute("className", className);
            }
        } else {
            model.addAttribute("list", jobDao.findAllByClassNameAndCourseOrderByIdDesc(className, course));
            model.addAttribute("className", className);
        }
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
    public String fileUpload(Model model, @RequestParam("file_upload") MultipartFile file, @RequestParam("id") Integer id, HttpServletRequest request) {
        if (file.isEmpty()) {
            model.addAttribute("error", "文件为空，请重新上传");
            return "error";
        }
        Job job = jobDao.getOne(id);
        String newFilePath = filePath + job.getTeacher() + "/" + job.getCourse() + "/" + job.getClassName() + "/" + job.getNum() + "/";
        File newFile = new File(newFilePath);
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String oldFileName = file.getOriginalFilename().contains("\\") ? file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("\\") + 1) : file.getOriginalFilename();
        Student student = (Student) request.getSession().getAttribute("loginUser");
        String fileName = student.getNum() + "_" + student.getName() + "_" + sdf.format(new Date()) + "_" + oldFileName;
        File newFile1 = new File(newFilePath + fileName);
        try {
            file.transferTo(newFile1);
            job.setStudentNum(job.getStudentNum() == null ? 1 : job.getStudentNum() + 1);
            jobDao.save(job);
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "文件错误，请重新上传");
            return "error";
        }
    }
}
