<?php
require_once '../Boydsnest.php';
require_once FCORE_FILE_DBMESSAGE;

DBMessage::ForceDeleteMessageOfOriginType("test");


$message_id = DBMessage::CreateMessage(
        "hello there", "hello",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 123
        ), 
        array(234, 345, 456, 567));
if ($message_id)
{
    echo __LINE__.": passed create";
}
else
{
    echo __LINE__.": failed create";
}
htmlln();
$message = DBMessage::GetMessage($message_id);
$message = $message[0];
$content = DBMessage::GetMessageContent($message[DBMessage::MESSAGE_ID]);
if ($message[DBMessage::TITLE] == "hello" &&
        $message[DBMessage::ORIGIN_TYPE] == "test" &&
        $content == "hello there")
{
    echo __LINE__.": passed get";
}
else
{
    echo __LINE__.": failed get";
}
htmlln();
DBMessage::DeleteMessages($message_id,
        array(
            array(DBMessage::ORIGIN_ID => 234, DBMessage::ORIGIN_TYPE => "test")
        ));
$message = DBMessage::GetMessage($message_id);
//echo var_dump($message);
if ($message != null)
{
    echo __LINE__.": passed single delete";
}
else
{
    echo __LINE__.": failed single delete";
}
htmlln();
DBMessage::DeleteMessages($message_id,
        array(
            array(DBMessage::ORIGIN_ID => 345, DBMessage::ORIGIN_TYPE => "test"),
            array(DBMessage::ORIGIN_ID => 456, DBMessage::ORIGIN_TYPE => "test")
        ));
$message = DBMessage::GetMessage($message_id);
//echo var_dump($message);
if ($message != null)
{
    echo __LINE__.": passed multi delete";
}
else
{
    echo __LINE__.": failed multi delete";
}
htmlln();
DBMessage::DeleteMessages($message_id,
        array(
            array(DBMessage::ORIGIN_ID => 123, DBMessage::ORIGIN_TYPE => "test"),
            array(DBMessage::ORIGIN_ID => 567, DBMessage::ORIGIN_TYPE => "test")
        ));
$message = DBMessage::GetMessage($message_id);
//echo var_dump($message);
if ($message == null)
{
    echo __LINE__.": passed complete delete";
}
else
{
    echo __LINE__.": failed complete delete";
}
htmlln();


$message_id = DBMessage::CreateMessage(
        "hello there", "hello",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 123
        ),
        array(234, 345, 456, 567));
DBMessage::ForceDeleteMessage($message_id);
$message = DBMessage::GetMessage($message_id);
if ($message == null)
{
    echo __LINE__.": passed force delete";
}
else
{
    echo __LINE__.": failed force delete";
}
htmlln();


$message_id = array();
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 1
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 1
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 2
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 3
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 3
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 4
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 5
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 1
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 13
        ),
        array(3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 1
        ),
        array(3,4,5));

$messages = DBMessage::GetMessageLimitedByForOriginsFrom("test", 1);
if (sizeof($messages) == 4)
{
    echo __LINE__.": passed limited by for origins from call";
}
else
{
    echo __LINE__.": failed limited by for origins from call";
}
htmlln();

$messages = DBMessage::GetMessageLimitedByForOriginsTo("test", 2);
if (sizeof($messages) == 8)
{
    echo __LINE__.": passed limited by for origins to call";
}
else
{
    echo __LINE__.": failed limited by for origins to call";
}
htmlln();

DBMessage::ForceDeleteMessageOfOriginType("test");



$message_id = array();
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 1
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 1
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 2
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 3
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 3
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 4
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 5
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 1
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 13
        ),
        array(3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 1
        ),
        array(3,4,5));

DBMessage::DeleteMessagesForOrigin($message_id, "test", 1);

$messages = DBMessage::GetMessageLimitedByForOriginsFrom("test", 1);
if ($messages == null)
{
    echo __LINE__.": passed message delete for origin test";
}
else
{
    echo __LINE__.": failed message delete for origin test";
    echo "<br />";
    var_export($messages);
}
htmlln();

$messages = DBMessage::GetMessageLimitedByForOriginsTo("test", 2);
if (sizeof($messages) == 8)
{
    echo __LINE__.": passed message delete for origin test 2";
}
else
{
    echo __LINE__.": failed message delete for origin test 2";
    echo "<br />";
    var_export($messages);
}
htmlln();
DBMessage::ForceDeleteMessageOfOriginType("test");




$message_id = array();
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 1
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 1
        ),
        array(2,3,4,5));
$message_id[] = DBMessage::CreateMessage(
        "hello", "poo",
        array(
            DBMessage::ORIGIN_TYPE => "test",
            DBMessage::ORIGIN_ID => 2
        ),
        array(2,3,4));
DBMessage::DeleteMessagesForOrigin($message_id, "test", 1);
DBMessage::DeleteMessagesForOrigin($message_id, "test", 2);
DBMessage::DeleteMessagesForOrigin($message_id, "test", 3);
DBMessage::DeleteMessagesForOrigin($message_id, "test", 4);
$messages = DBMessage::GetMessage($message_id);
if (sizeof($messages) == 2)
{
    echo __LINE__.": passed message delete for origin test 3";
}
else
{
    echo __LINE__.": failed message delete for origin test 3";
    echo "<br />";
    var_export($messages);
}
htmlln();
DBMessage::DeleteMessagesForOrigin($message_id, "test", 5);
$messages = DBMessage::GetMessage($message_id);
if ($messages == null)
{
    echo __LINE__.": passed message delete for origin test 4";
}
else
{
    echo __LINE__.": failed message delete for origin test 4";
    echo "<br />";
    var_export($messages);
}
htmlln();

DBMessage::ForceDeleteMessageOfOriginType("test");

?>