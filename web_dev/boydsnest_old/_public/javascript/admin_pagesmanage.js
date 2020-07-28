/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function getTargetFromEvent(event){
    if (!event) event = window.event;
    return (event.target) ? event.target : event.srcElement;
}

function getUserSelectParent(){
    return $("#_userselecttarget");
}

function triggerUserAjax(event){
    var target = getTargetFromEvent(event);
    $.ajax({
        type: "POST",
        //url: serverloc + "/ajax/_ajaxentry.php?page=admin_pagesmanage",
        url: "ajax/_ajaxentry.php?page=admin_pagesmanage",
        data: "userID="+target.value,
        success: function(data){
            $("#pagehierarchytarget").html(data);
        },
        error: function(data){
            alert(data);
        }
    });
}

function buildUserOption(name, value, selected, parent){
    var newoption = document.createElement('option');
    newoption.id = "_userselectoption";
    newoption.value = value;
    newoption.innerHTML = name;
    if (selected){
        newoption.selected = "selected";
    }
    newoption.onclick = function(event){
        var target = getTargetFromEvent(event);
        $.ajax({
            type: "POST",
            //url: serverloc + "/ajax/_ajaxentry.php?page=admin_pagesmanage",
            url: "ajax/_ajaxentry.php?page=admin_pagesmanage",
            data: "userID="+target.value,
            success: function(data){
                $("#pagehierarchytarget").html(data);
            },
            error: function(data){
                alert(data);
            }
        });
    }

    parent.append(newoption);

    //return newoption;
}

