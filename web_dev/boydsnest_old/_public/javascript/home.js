/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//var serverloc = "http://localhost/boydsnest";

function getPage(pageID){
    return $('div[id="_sn_foldercontainer"] > div[id="page_'+pageID+'"]');
}

function getUserSection(userID){
    return $('div[id="_sn_usercontainer"][title="'+userID+'"]');
}

function getUserFolder(userID){
    return $('div[id="_sn_usercontainer"][title="'+userID+'"] > div[id="_sn_foldercontainer"]');
}

function getTargetFromEvent(event){
    if (!event) event = window.event;
    return (event.target) ? event.target : event.srcElement;
}

function insertAfter(newElement,targetElement) {
    var parent = targetElement.parentNode;
    if(parent.lastchild == targetElement) {
        parent.appendChild(newElement);
    } else {
        parent.insertBefore(newElement, targetElement.nextSibling);
    }
}

function buildMenuButton(pageID, title, indent, folded){
    var newdiv = document.createElement('div');
    newdiv.id = "page_" + pageID;
    newdiv.title = indent;
    for(var i=0; i<indent; i++){newdiv.innerHTML += "&nbsp;";}

    var expandbutton = document.createElement('span');
    expandbutton.id = "_sn_expandbutton";
    expandbutton.onclick = togglePageExpand;
    if (folded == null){
        expandbutton.innerHTML += "[x]";
    } else {
        if (folded){
            expandbutton.innerHTML += "[+]";
        } else {
            expandbutton.innerHTML += "[-]";
        }
    }
    newdiv.appendChild(expandbutton);

    var pagetitle = document.createElement('span');
    pagetitle.id = "_sn_pagetitle";
    pagetitle.innerHTML = "&nbsp;" + title;
    pagetitle.onclick = toggleAjaxPage;
    newdiv.appendChild(pagetitle);

    return newdiv;
}

function buildPage(pageid, title, content){
    var newdiv = document.createElement('div');
    newdiv.title = pageid;

    var header = document.createElement('h2');
    header.innerHTML = title;

    var pagecontent = document.createElement('div');
    pagecontent.innerHTML = content;

    newdiv.appendChild(header);
    newdiv.appendChild(pagecontent);

    return newdiv;
}

function toggleAjaxPage(event){
    var target = getTargetFromEvent(event);
    var pageid = target.parentNode.id;
    pageid = pageid.substring(5);
    ajaxInPage(pageid);
    if (target.previousSibling.innerHTML == "[+]"){
        openExpandButton(pageid, target);
        target.previousSibling.innerHTML = "[-]";
    }
    ajaxInBaseComments(pageid);
}

function togglePageExpand(event){
    if (!event) event = window.event;
    var target = (event.target) ? event.target : event.srcElement;
    if (target.innerHTML == "[-]"){
        target.innerHTML = "[+]";
        ridChildren(target.parentNode);
    } else if (target.innerHTML == "[+]") {
        var pageid = target.parentNode.id;
        pageid = pageid.substring(5);
        openExpandButton(pageid, target);
        target.innerHTML = "[-]";
    }
}

function openExpandButton(pageid, target){
    $.ajax({
        type: "POST",
        //url: serverloc + "/ajax/_homeajax.php",
        url: "ajax/_homeajax.php",
        data: "action=getchild&pageID=" + pageid,
        success: function(data){
            if (data == ""){return;}
            var pages = data.split("::::");
            for(var i=0; i<pages.length; i++){
                try{
                    var pageInfo = pages[i].split("::");
                    var newDiv = buildMenuButton(
                            pageInfo[0],
                            pageInfo[1],
                            parseInt(target.parentNode.title) + 3,
                            (pageInfo[2]==1 ? false : null)
                        );
                    insertAfter(newDiv, target.parentNode);
                } catch(e){
                }
            }
        }
    });
}

function ridChildren(element){
    try {
        var indent = parseInt(element.title);
        var parent = element.parentNode;
        while(element.nextSibling != null){
            var checking = element.nextSibling;
            if (parseInt(checking.title) > indent){
                parent.removeChild(checking);
            } else {
                return;
            }
        }
    } catch(e) {
    }
}

function ajaxInPage(pageid){
    $.ajax({
        type: "POST",
//        url: serverloc + "/ajax/_homeajax.php",
        url: "ajax/_homeajax.php",
        data: "action=getpage&pageID=" + pageid,
        success: function(data){
            if (data == "") return;
            var pageInfo = data.split("::::");
            $('div[id="_pagecontenttarget"]').html(
                buildPage(pageInfo[0], pageInfo[1], pageInfo[2])
            );
        }
    });
}

function getUserRoots(userid){
    $.ajax({
        type: "POST",
//        url: serverloc + "/ajax/_homeajax.php",
        url: "ajax/_homeajax.php",
        data: "action=getroots&userID=" + userid,
        success: function(data){
            if (data == ""){return;}
            var parent = $('div[title="'+userid+'"] > div[id="_sn_foldercontainer"]');
            var pages = data.split("::::");
            for(var i=0; i<pages.length; i++){
                try{
                    var pageInfo = pages[i].split("::");
                    var newDiv = buildMenuButton(
                            pageInfo[0],
                            pageInfo[1],
                            3,
                            (pageInfo[2]==1 ? true : null)
                        );
                    parent.append(newDiv);
                } catch(e){
                }
            }
        }
    });
}

$(function(){
    $('div[id="_sn_username"]').click(function(event){
        target = getTargetFromEvent(event);
        $('div[id="_sn_foldercontainer"]').html("");
        getUserRoots(target.parentNode.title);
    });
});

function buildCommentTitle(commentid, title, indent, cancomment){
    var newdiv = document.createElement('div');
    newdiv.id = "comment_" + commentid;
    for(var i=0; i<indent; i++){newdiv.innerHTML += "&nbsp;";}

    var newtitle = document.createElement('span');
    newtitle.id = "_comment_title";
    newtitle.innerHTML = title;
    newtitle.onclick = toggleCommentBody;
    newdiv.appendChild(newtitle);

    if (cancomment){
        var newcomment = document.createElement('span');
        newcomment.id = "_comment_make";

        var commentformtarget = document.createElement('div');
        commentformtarget.id = "_comment_formtarget";
        var commentbutton = document.createElement('button');
        commentbutton.value = "Make Comment";

        newcomment.appendChild(commentformtarget);
        newcomment.appendChild(commentbutton);

        newdiv.appendChild(newcomment);
    }
}

function buildCommentOnForm(childof, pageid){
    var newform = document.createElement('form');
    newform.id = "_comment_form";
    newform.name = "createcomment";

    var newhidden = document.createElement('input');
    newhidden.type = "hidden";
    newhidden.name = "childof";
    newhidden.value = childof;
    newform.appendChild(newhidden);

    var newhidden2 = document.createElement('input');
    newhidden2.type = "hidden";
    newhidden2.name = "pageID";
    newhidden2.value = pageid;
    newform.appendChild(newhidden2);

    var newtextarea = document.createElement('textarea');
    newtextarea.name = "comment";
    newtextarea.cols = 50;
    newtextarea.rows = 5;
    newform.appendChild(newtextarea);

    var button = document.createElement('button');
    button.onclick = submitComment;
    button.value = "Comment";
    newform.appendChild(button);

    return newform;
}

function submitComment(event){
    var target = getTargetFromEvent(event);
    target = target.parentNode;
    
}

function ajaxInBaseComments(pageid){
    $.ajax({
        type: "POST",
//        url: serverloc + "/ajax/_homeajax.php",
        url: "ajax/_homeajax.php",
        data: "action=basecomments&pageID=" + pageid,
        success: function(data){
            if (data == "") return;
            var pageInfo = data.split("::::");
            for(var i=0; i<pageInfo.length; i++){
                
            }
        }
    });
}

function openCommentBody(){
    
}

function toggleCommentBody(event){
    
}