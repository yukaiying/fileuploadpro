$(document).ready(function () {
    updateTable("/jobById/","jobForm");
    insertTable("jobForm");
    // delTable("/jobDel/");
    $(".btn-upload").click(function () {
        $("#job_id")[0].value = this.value;
    });
    $(".btn-no").click(function () {
        let jobId = this.value;
        $.post('/jobNo/byJobId/' + jobId,null,function (data) {
            let tbody = $("#no_student_show_tbody")[0];
            let result = Array.of(...data);
            tbody.innerHTML = result.map(i => `<tr><td>${i.studentName}</td></tr>`).join("");
            $("#no_job_num")[0].innerHTML ="未交作业学生数目：" + result.length;
        });
    });
});