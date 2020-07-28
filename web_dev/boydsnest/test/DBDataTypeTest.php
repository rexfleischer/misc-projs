<?php
require_once '../Boydsnest.php';
require_once FCORE_FILE_DBDATATYPE;

$inserts = array();
$inserts[] = DBDataType::CreateData("test", 123, DBDataType::DATATYPE_TEXT);
$inserts[] = DBDataType::CreateData("test", 124, DBDataType::DATATYPE_TEXT);
$inserts[] = DBDataType::CreateData("test", 125, DBDataType::DATATYPE_TEXT);
$inserts[] = DBDataType::CreateData("test", 126, DBDataType::DATATYPE_TEXT);
$inserts[] = DBDataType::CreateData("test", 127, DBDataType::DATATYPE_TEXT);

echo var_dump($inserts);

$data = DBDataType::GetPrimaryData("test", 123);
if ($data[DBDataType::DATATYPE_ID] == $inserts[0])
{
    echo "passed insert and get test<br />";
}
else
{
    echo "failed insert and get test<br />";
}
$data = DBDataType::GetPrimaryData("test", 124);
if ($data[DBDataType::DATATYPE_ID] == $inserts[1])
{
    echo "passed insert and get test<br />";
}
else
{
    echo "failed insert and get test<br />";
}
$data = DBDataType::GetPrimaryData("test", 125);
if ($data[DBDataType::DATATYPE_ID] == $inserts[2])
{
    echo "passed insert and get test<br />";
}
else
{
    echo "failed insert and get test<br />";
}
$data = DBDataType::GetPrimaryData("test", 126);
if ($data[DBDataType::DATATYPE_ID] == $inserts[3])
{
    echo "passed insert and get test<br />";
}
else
{
    echo "failed insert and get test<br />";
}
$data = DBDataType::GetPrimaryData("test", 127);
if ($data[DBDataType::DATATYPE_ID] == $inserts[4])
{
    echo "passed insert and get test<br />";
}
else
{
    echo "failed insert and get test<br />";
}


foreach($inserts as $insert_id)
{
    DBDataType::DeleteDataFromID($insert_id);
}

$data_left = DBDataType::DeleteAllFromOriginType("test");
if ($data_left == null)
{
    echo "passed delete test<br />";
}
else
{
    echo var_dump($data_left);
}


$inserts = array();
$inserts[] = DBDataType::CreateData("test", 123, DBDataType::DATATYPE_TEXT);
$inserts[] = DBDataType::CreateData("test", 124, DBDataType::DATATYPE_TEXT);
$inserts[] = DBDataType::CreateData("test", 125, DBDataType::DATATYPE_TEXT);
$inserts[] = DBDataType::CreateData("test", 126, DBDataType::DATATYPE_TEXT);
$inserts[] = DBDataType::CreateData("test", 127, DBDataType::DATATYPE_TEXT);

$data_left = DBDataType::DeleteAllFromOriginType("test");
echo var_dump($data_left);
if (sizeof($data_left) == 5)
{
    echo "passed mass delete test<br />";
}
else
{
    echo "failed mass delete test<br />";
}


$inserts = array();
$inserts[] = DBDataType::CreateData("test", 123, DBDataType::DATATYPE_TEXT, "content 1");
$inserts[] = DBDataType::CreateData("test", 124, DBDataType::DATATYPE_TEXT, "content 2");
$inserts[] = DBDataType::CreateData("test", 125, DBDataType::DATATYPE_TEXT, "content 3");
$inserts[] = DBDataType::CreateData("test", 126, DBDataType::DATATYPE_TEXT, "content 4");
$inserts[] = DBDataType::CreateData("test", 127, DBDataType::DATATYPE_TEXT, "content 15");

echo var_dump($inserts);

$data = DBDataType::GetDataContentFromOrigin("test", 123);
if ($data == "content 1")
{
    echo "passed insert and get test<br />";
}
else
{
    echo "failed insert and get test<br />";
}
$data = DBDataType::GetDataContentFromOrigin("test", 124);
if ($data == "content 2")
{
    echo "passed insert and get test<br />";
}
else
{
    echo "failed insert and get test<br />";
}
$data = DBDataType::GetDataContentFromOrigin("test", 125);
if ($data == "content 3")
{
    echo "passed insert and get test<br />";
}
else
{
    echo "failed insert and get test<br />";
}
$data = DBDataType::GetDataContentFromOrigin("test", 126);
if ($data == "content 4")
{
    echo "passed insert and get test<br />";
}
else
{
    echo "failed insert and get test<br />";
}
$data = DBDataType::GetDataContentFromOrigin("test", 127);
if ($data == "content 15")
{
    echo "passed insert and get test<br />";
}
else
{
    echo "failed insert and get test<br />";
}


$data_left = DBDataType::DeleteAllFromOriginType("test");
echo var_dump($data_left);
if (sizeof($data_left) == 5)
{
    echo "passed mass delete test with content<br />";
}
else
{
    echo "failed mass delete test with content<br />";
}



$inserts = array();
$inserts[] = DBDataType::CreateData("test", 123, DBDataType::DATATYPE_TEXT, "content 1", "test:test1");
$inserts[] = DBDataType::CreateData("test", 124, DBDataType::DATATYPE_TEXT, "content 2", "test:test2");
$inserts[] = DBDataType::CreateData("test", 125, DBDataType::DATATYPE_TEXT, "content 3", "test:test3");
echo var_dump($inserts);


$data = DBDataType::GetPrimaryData("test", 123);
if ($data[DBDataType::METADATA] == "test:test1")
{
    echo "passed insert and get test with meta<br />";
}
else
{
    echo "failed insert and get test with meta<br />";
}
DBDataType::UpdateContentFromID("content 1 extended",
        $data[DBDataType::DATATYPE_ID]);
$data = DBDataType::GetDataContentFromOrigin("test", 123);
if ($data == "content 1 extended")
{
    echo "passed change content and get content<br />";
}
else
{
    echo "failed change content and get content<br />";
}


$data = DBDataType::GetPrimaryData("test", 124);
if ($data[DBDataType::METADATA] == "test:test2")
{
    echo "passed insert and get test with meta<br />";
}
else
{
    echo "failed insert and get test with meta<br />";
}
DBDataType::UpdateContentFromID("content 2 extended",
        $data[DBDataType::DATATYPE_ID], $data[DBDataType::DATATYPE]);
$data = DBDataType::GetDataContentFromOrigin("test", 124);
if ($data == "content 2 extended")
{
    echo "passed change content and get content<br />";
}
else
{
    echo "failed change content and get content<br />";
}


$data = DBDataType::GetPrimaryData("test", 125);
if ($data[DBDataType::METADATA] == "test:test3")
{
    echo "passed insert and get test with meta<br />";
}
else
{
    echo "failed insert and get test with meta<br />";
}
DBDataType::UpdateContentFromOrigin("content 3 extended",
        $data[DBDataType::ORIGIN_TYPE], $data[DBDataType::ORIGIN_ID]);
$data = DBDataType::GetDataContentFromOrigin("test", 125);
if ($data == "content 3 extended")
{
    echo "passed change content and get content<br />";
}
else
{
    echo "failed change content and get content<br />";
}


DBDataType::DeleteAllFromOriginType("test");

?>