$(document).ready(function () {
    updateTable("/jobById/","jobForm");
    insertTable("jobForm");
    // delTable("/jobDel/");
    $(".btn-upload").click(function () {
        $("form[name='uploadForm'] input[name='id']").get(0).value = this.value;
    });
});