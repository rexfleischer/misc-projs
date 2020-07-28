/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//var serverloc = "http://localhost/boydsnest";

$(function(){
    $("select[name=userID] > option").click(function(event){
        if (!event) event = window.event;
        var target = (event.target) ? event.target : event.srcElement;
        $.ajax({
            type: "POST",
            //url: serverloc + "/ajax/_ajaxentry.php?page=admin_pagecreate",
            url: "ajax/_ajaxentry.php?page=admin_pagecreate",
            data: "userID="+$(target).val(),
            success: function(data){
                $("#childofselecttarget").html(data);
            }
        });
    });
});