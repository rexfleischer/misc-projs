<?php

class ThreadedForumViewer {
    public static final function BuildViewForAdminPageManage(_FORUM &$forum, $canUpdate){
        $count = $forum->getNodeCount();
        for($i=0; $i<$count; $i++){
            $thisNode = $forum->getNode($i);
            //$thisNode = new DB_PAGE();
            ?>

<div id="_listcontainer" class="<?php if($i%2==0){ echo "on"; }else{ echo "off"; } ?>">
    <?php
    echo _HTML::I_2."<div style='"._CSS::FloatLeftWidth(40)."'>";
    echo _HTML::I_3.$thisNode->getPageID();
    echo _HTML::I_2."</div>";
    ?>
    <a href="<?php echo URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::PAGES_WRITE."&".PAGES_PAGEID."=".$thisNode->getPageID(); ?>">
        <div id="_listcontainer_width300px">
            <?php for($s=0; $s<$forum->getIndent($i); $s++){ echo "&nbsp;"; } ?>
            <?php echo $thisNode->getTitle(); ?>
        </div>
    </a>
    <?php if($canUpdate){ ?>
    <div id="_listcontainer_something">
        <?php
        echo _HTML::I_1._FORM::_FormBegin("updaterank", URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::PAGES_MANAGE);
        echo _HTML::I_2._FORM::Hidden(ACTION_GO, ACTION_UPDATE);
        echo _HTML::I_2._FORM::Hidden(PAGES_USERID, $thisNode->getUserID());
        echo _HTML::I_2._FORM::Hidden(PAGES_PAGEID, $thisNode->getPageID());
        echo _HTML::I_2."<div style='"._CSS::FloatLeftWidth(110)."'>";
        echo _HTML::I_3."Rank: "._FORM::Text(PAGES_RANK, $thisNode->getRank(), 3, 3);
        echo _HTML::I_2."</div>";
        echo _HTML::I_2."<div style='"._CSS::FloatLeftWidth(110)."'>";
        echo _HTML::I_3._FORM::Submit("Update Rank");
        echo _HTML::I_2."</div>";
        echo _HTML::I_1._FORM::_FormEnd();
        ?>
    </div>
    <div id="_listcontainer_something"><?php
        echo _HTML::I_2._FORM::_FormBegin("movefolder", URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::PAGES_MANAGE);
        echo _HTML::I_3._FORM::Hidden(ACTION_GO, ACTION_PASS);
        echo _HTML::I_3._FORM::Hidden(PAGES_USERID, $thisNode->getUserID());
        echo _HTML::I_3._FORM::Hidden(PAGES_PAGEID, $thisNode->getPageID());
        echo _HTML::I_3."<div style='"._CSS::FloatLeftWidth(110)."'>";
        echo _HTML::I_4."Parent: "._FORM::Text(PAGES_CHILDOF, $thisNode->getChildOf(), 3, 7);
        echo _HTML::I_3."</div>";
        echo _HTML::I_3."<div style='"._CSS::FloatLeftWidth(90)."'>";
        echo _HTML::I_4._FORM::Submit("Move Page");
        echo _HTML::I_3."</div>";
        echo _HTML::I_2._FORM::_FormEnd();
        ?>
    </div>
    <div style="<?php echo _CSS::FloatLeftWidth(100); ?>">
        <div id="_listcontainer_tomato"><?php
            echo _HTML::I_3._FORM::_FormBegin("privacyoffolder", URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::PAGES_MANAGE);
            echo _HTML::I_4._FORM::Hidden(ACTION_GO, ACTION_VIEW);
            echo _HTML::I_4._FORM::Hidden(PAGES_USERID, $thisNode->getUserID());
            echo _HTML::I_4._FORM::Hidden(PAGES_PAGEID, $thisNode->getPageID());
            echo _HTML::I_4._FORM::Submit($thisNode->getIsPrivate() ? "Make Public" : "Make Private");
            echo _HTML::I_3._FORM::_FormEnd();
            ?>
        </div>
    </div>
    <div style="<?php echo _CSS::FloatLeftWidth(100); ?>">
        <div id="_listcontainer_tomato"><?php
            echo _HTML::I_3._FORM::_FormBegin("deletefolder", URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::PAGES_MANAGE);
            echo _HTML::I_4._FORM::Hidden(ACTION_GO, ACTION_DELETE);
            echo _HTML::I_4._FORM::Hidden(PAGES_USERID, $thisNode->getUserID());
            echo _HTML::I_4._FORM::Hidden(PAGES_PAGEID, $thisNode->getPageID());
            echo _HTML::I_4._FORM::Submit("Delete Page");
            echo _HTML::I_3._FORM::_FormEnd();
            ?>
        </div>
    </div><?php } ?>
</div>
<?php
        }
    }
}

?>
