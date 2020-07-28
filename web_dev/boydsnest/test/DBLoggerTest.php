<?php
require_once '../Boydsnest.php';
require_once FCORE_FILE_DBLOGGER;

$inserts = array();
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");

echo var_dump($inserts);
if(sizeof($inserts) == 10)
{
    echo "passed inserts<br />";
}
else
{
    echo "failed inserts<br />";
}

foreach($inserts as $id)
{
    DBLogger::delete($id);
}
if (DBLogger::GetLimitedBy("hell") == null)
{
    echo "passed single deletes<br />";
}
else
{
    echo "failed single deletes<br />";
}


$inserts = array();
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");
$inserts[] = DBLogger::log("hell", "o world");

DBLogger::delete($inserts);
if (DBLogger::GetLimitedBy("hell") == null)
{
    echo "passed mulitple deletes<br />";
}
else
{
    echo "failed mulitple deletes<br />";
}

$inserts = array();
$inserts[] = DBLogger::log("hell", "o world0");
$inserts[] = DBLogger::log("hell", "o world1");
sleep(1);
$inserts[] = DBLogger::log("hell", "o world2");
$inserts[] = DBLogger::log("hell", "o world3");
sleep(1);
$inserts[] = DBLogger::log("hell", "o world4");
$inserts[] = DBLogger::log("hell", "o world5");
sleep(1);
$inserts[] = DBLogger::log("hell", "o world6");
$inserts[] = DBLogger::log("hell", "o world7");
sleep(1);
$inserts[] = DBLogger::log("hell", "o world8");
$inserts[] = DBLogger::log("hell", "o world9");
sleep(1);
$inserts[] = DBLogger::log("hell", "o world10");
$inserts[] = DBLogger::log("hell", "o world11");
sleep(1);
$inserts[] = DBLogger::log("hell", "o world12");
$inserts[] = DBLogger::log("hell", "o world13");
sleep(1);
$inserts[] = DBLogger::log("hell", "o world14");
$inserts[] = DBLogger::log("hell", "o world15");
sleep(1);
$inserts[] = DBLogger::log("hell", "o world16");
$inserts[] = DBLogger::log("hell", "o world17");
sleep(1);
$inserts[] = DBLogger::log("hell", "o world18");
$inserts[] = DBLogger::log("hell", "o world19");
echo var_dump($inserts);
$logs1 = DBLogger::GetLimitedBy("hell", 1, 10);
echo var_dump($logs1);
$logs2 = DBLogger::GetLimitedBy("hell", 11, 20);
echo var_dump($logs2);
if (sizeof($logs1) == 10 && sizeof($logs2) == 9)
{
    echo "passed pageation<br />";
}
else
{
    echo "failed pageation<br />";
}

DBLogger::delete($inserts);

?>