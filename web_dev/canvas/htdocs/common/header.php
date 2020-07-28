
<fieldset>
    <legend>list-o-examples:</legend>
    <table>
    <?php 
    $prev = '';
    $examples = get_files_from_dir(APPDIR . "/htdocs/examples");
    echoln("    <tr>");
    foreach($examples as $value) 
    {
        preg_match("/(.*?)_/", $value, $curr);
        if ($prev == '')
        {
            $prev = $curr[1];
        }
        else if ($prev != $curr[1])
        {
            echoln("    </tr><tr>");
            $prev = $curr[1];
        }
        preg_match("/(.*)\./", $value, $matches);
        echoln("        <td><a href='example.php?example=$matches[1]'>$matches[1]</a></td>");
    }
    echoln("    </tr>");
    ?>
    </table>
</fieldset>
