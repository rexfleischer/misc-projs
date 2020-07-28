<?php
require_once '../Boydsnest.php';
require_once FCORE_FILE_DBFORUM;

function print_forum($posts, &$forum)
{
    foreach($posts as $post)
    {
        ?>

    <div>
        <?php for($i=0; $i<$post[DBForum::POSTINDENT]; $i++){ echo "&nbsp;&nbsp;&nbsp;"; } ?>
        <?php echo $post[DBForum::ORIGIN_TYPE].':'.$post[DBForum::ORIGIN_ID]." says: "; ?>
        <?php echo $forum->post_get_content($post[DBForum::POST_ID]); ?>
        
    </div>

    <?php
    }
}

DBForum::CreateForum("test", 123, DBForum::$valid_data_types[0]);

$forum = DBForum::GetForum("test", 123);

            $forum->post_create("users", 12, "hello");
$post_id =  $forum->post_create("users", 13, "hello back");
            $forum->post_create("users", 123, "he said hello", $post_id);
            $forum->post_create("users", 12, "how are you");
$post_id2 = $forum->post_create("users", 124, "i'm going to move my post by parent");
$post_id5 = $forum->post_create("users", 124, "i'm going to change my content");
            $forum->post_create("users", 13, "i'm fine, how are you");
            $forum->post_create("users", 12, "i'm doing good");
$post_id6 = $forum->post_create("users", 124, "i'm goign to be deleted");
$post_id3 = $forum->post_create("users", 124, "i'm going to move my post by order");
            $forum->post_create("users", 13, "thats nice");
$post_id =  $forum->post_create("users", 12, "eat shit");
$post_id4 = $forum->post_create("users", 12, "i'm sorry, i take it back", $post_id);
            $forum->post_create("users", 123, "he said sorry", $post_id4);
            $forum->post_create("users", 123, "he said sorry", $post_id);
            $forum->post_create("users", 13, "thats nice");

$forum->post_update($post_id2, false, $post_id, false);
$forum->post_update($post_id3, false, false, -1);
$forum->post_update($post_id5, "i changed my content", false, false);
$forum->post_delete($post_id6);

$hierarchy = $forum->build_hierarchy();
print_forum($hierarchy, $forum);

echo "<br /><br /><br />";

$hierarchy = $forum->build_hierarchy($post_id);
print_forum($hierarchy, $forum);

DBForum::DeleteForum("test", 123);

?>