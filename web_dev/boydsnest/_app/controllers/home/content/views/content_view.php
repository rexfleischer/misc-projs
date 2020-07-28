<?php

require_once FCORE_FILE_BBCONSUMER;

$viewer = array();
$viewer['title']    = isset($title) && is_string($title) ?
                            $title :
                            "No Title";
$viewer['content']  = isset($content) && is_string($content) ?
                            BBConsumer::consume($content) :
                            "No Content";

echoln();
echoln();

echo FCore::LoadViewPHP("content/BasicTextViewer", $viewer);

echoln();
echoln();

echo $responses;

echoln();
echoln();

?>