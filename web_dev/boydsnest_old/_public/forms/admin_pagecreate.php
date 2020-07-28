<?php

function MakeCreateUserChildOfSelect($info, $selected = 0){
    echo _FORM::SelectBegin(PAGES_CHILDOF);
    echo _FORM::Option("root", 0, $selected==0);
    $count = sizeof($info);
    for($i=0; $i<$count; $i++){
        echo _FORM::Option(
                $info[$i]->getTitle(),
                $info[$i]->getPageID(),
                $selected==$info[$i]->getPageID());
    }
    echo _FORM::SelectEnd();
}

?>
