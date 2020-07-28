<?php

require_once FCORE_FILE_BBCONSUMER;

echoln("<h2>".(isset($title) ? $title : "No Title")."</h2>");

echo BBConsumer::consume($content);

?>