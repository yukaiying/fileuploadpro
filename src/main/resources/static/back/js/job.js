$(document).ready(function () {
    updateTable("/jobById/","jobForm");
    insertTable("jobForm");
    // delTable("/jobDel/");
    $(".btn-upload").click(function () {
        $("#job_id")[0].value = this.value;
    });
});