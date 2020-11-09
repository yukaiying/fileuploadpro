package com.file.demo.controller;

import com.file.demo.dao.*;
import com.file.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private JobCompleteDao jobCompleteDao;

    @Autowired
    private JobNoDao jobNoDao;

    @Autowired
    private StudentDao studentDao;

    @GetMapping("/job")
    public String jobPage(Model model, HttpServletRequest request) {
        jobList(model, request, null, null);
        return "joblist";
    }

    @PostMapping("/job")
    public String jobPage(Model model, @RequestParam("className") String className, @RequestParam("course") String course, HttpServletRequest request) {
        jobList(model, request, className, course);
        return "joblist";
    }

    private void jobList(Model model, HttpServletRequest request, String className, String course) {
        model.addAttribute("course", course);
        model.addAttribute("courseList", courseDao.findAll());
        model.addAttribute("studentClassList", studentClassDao.findAll());
        Student student = (Student) request.getSession().getAttribute("loginUser");
        if (student != null) {
            if (student.getType() != null && 2 == student.getType()) {
                model.addAttribute("list", updateIsPast(jobDao.findAllByOrderByIdDesc()));
                model.addAttribute("userType", true);
            } else {
                className = studentClassDao.findById(student.getClassId()).orElseGet(StudentClass::new).getClassName();
                model.addAttribute("list", updateIsPast(jobDao.findAllByClassNameOrderByIdDesc(className)));
                model.addAttribute("className", className);
            }
        } else {
            model.addAttribute("list", updateIsPast(jobDao.findAllByClassNameAndCourseOrderByIdDesc(className, course)));
            model.addAttribute("className", className);
        }
    }

    private List<Job> updateIsPast(List<Job> jobs) {
        return jobs.stream().peek(i -> i.setPast(i.getEndTime() != null && (new Date()).before(i.getEndTime()))).collect(Collectors.toList());
    }

    @PostMapping("/jobSave")
    @Transactional
    public String jobSave(Job job) {
        boolean flag = false;
        if (job.getId() == null) {
            job.setCreateTime(new Date());
            flag = true;
        }
        job.setClassName(studentClassDao.findById(job.getClassId()).orElseGet(StudentClass::new).getClassName());
        job = jobDao.save(job);
        if (flag) {
            int jobId = job.getId();
            jobNoDao.saveAll(studentDao.findAllByClassId(job.getClassId()).stream().map(i -> new JobNo(jobId, i.getId(), i.getName())).collect(Collectors.toList()));
        }
        return "redirect:/job";
    }

    @PostMapping("/jobById/{id}")
    @ResponseBody
    public Job jobById(@PathVariable("id") Integer id) {
        return jobDao.findById(id).orElseGet(Job::new);
    }

    @PostMapping("/fileUpload")
    @Transactional
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
            jobNoDao.deleteByJobIdAndStudentId(job.getId(), student.getId());
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "文件错误，请重新上传");
            return "error";
        }
    }
}
