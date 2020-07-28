/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//var serverloc = "http://localhost/boydsnest";
$(function(){
    $("select[title=manualstate_write] > option").click(function(event){
        if (!event) event = window.event;
        var target = (event.target) ? event.target : event.srcElement;
        $.ajax({
            type: "POST",
            //url: serverloc + "/ajax/_ajaxentry.php?page=usermanual",
            url: "ajax/_ajaxentry.php?page=usermanual",
            data: "pageID="+$(target).val(),
            success: function(data){
                $("#manualstatewritetarget").html(data);
            },
            error: function(data, data2, data3){
                alert(data + data2 + data3);
            }
        });
    });
});