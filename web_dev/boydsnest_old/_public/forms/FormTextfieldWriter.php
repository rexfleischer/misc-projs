<?php

class FormTextfieldWriter {

    public static final function JavaScriptInclude(){
        
    }

    public static final function EchoCSS(){
        ?>

<style type="text/css">
#_tfw_container
{
    width: 600px;
    height: 450px;
    background-color: silver;
    border-style: solid;
    border-color: darkgreen;
    border-width: 4px;
    margin: 4px;
    padding: 2px;
}
#_tfw_tools
{
    overflow: auto;
    min-width: 100px;
    min-height: 20px;
    background-color: yellow;
}
#_tfw_textarea
{
    border-style: solid;
    border-color: darkgreen;
    border-width: 4px;
    margin: 4px;
    padding: 2px;
}
#_tfw_title
{
    margin: 2px;
    padding: 2px;
}
</style>

<?php
    }
    
    public static final function EchoFormLayoutTemplate(
            $formURL,
            $action,
            $hiddenContent,
            $titleContent,
            $textareaName,
            $textareaCols,
            $textareaRows,
            $textareaContent,
            $submitText){
        ?>

<div id="_tfw_container"><?php
    echo _HTML::I_1._FORM::_FormBegin("_textareaform", $formURL);
    echo _HTML::I_2._FORM::Hidden(ACTION_GO, $action);
    echo $hiddenContent;
    echo _HTML::I_2._HTML::DIV_B("_tfw_title");
    echo $titleContent;
    echo _HTML::I_2._HTML::DIV_E;
    echo _HTML::I_2._HTML::DIV_B("_tfw_tools");
    echo _HTML::I_2._HTML::DIV_E;
    echo _HTML::I_2._HTML::DIV_B("_tfw_textarea");
    echo _HTML::I_3._FORM::TextArea($textareaName, $textareaCols, $textareaRows, $textareaContent);
    echo _HTML::I_2._HTML::DIV_E;
    echo _HTML::I_2._HTML::DIV_B($id);
    echo _HTML::I_3._FORM::Submit($submitText);
    echo _HTML::I_2._HTML::DIV_E;
    echo _HTML::I_1._FORM::_FormEnd();
    ?>
    
</div>

<?php
    }

}

?>
