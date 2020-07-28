<?php
require_once '../Boydsnest.php';
require_once FCORE_FILE_DBCOUNTER;

DBCounter::Increment("something");
DBCounter::Increment("something");
DBCounter::Increment("something");
DBCounter::Increment("something");
DBCounter::Increment("something");
DBCounter::Increment("something");
DBCounter::Increment("something");
DBCounter::Increment("something");
DBCounter::Increment("something");
DBCounter::Increment("something");

$count = DBCounter::GetCount("something");
if ($count == 10)
{
    echo "passed basic count (count is 10)<br />";
}
else
{
    echo "failed basic count (count was $count, was supposted to be 10)<br />";
}

DBCounter::Delete("something");
if (DBCounter::GetCount("something") == null)
{
    echo "passed delete<br />";
}
else
{
    echo "failed delete<br />";
}

DBCounter::SetCount("something", 123);
$count = DBCounter::GetCount("something");
if ($count == 123)
{
    echo "passed setting count<br />";
}
else
{
    echo "failed setting count<br />";
}

DBCounter::Delete("something");

?>