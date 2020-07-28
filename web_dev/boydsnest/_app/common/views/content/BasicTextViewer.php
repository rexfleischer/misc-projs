
        <link href="<?php echo BN_URL_CSS; ?>/content/BasicTextViewer.css" rel="stylesheet" type="text/css" />
        <fieldset class="basictextviewer">
            <legend><?php if (isset($title)){ echo "<h3>$title</h3>"; } ?></legend>
            <fieldset class="basictextviewer_inner">
                <?php IsSetEcho($content); ?>
            </fieldset>
        </fieldset>
        