
        <link href="<?php echo BN_URL_CSS; ?>/form/BasicTextEditor.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="<?php echo BN_URL_JAVASCRIPT; ?>/form/BasicTextEditor.js" ></script>
        <fieldset id="BasicTextEditor">
            <legend id="BasicTextEditor"><?php IsSetEcho($title); ?></legend>
            <button onclick="wrapText('b'); return false;" id="BasicTextEditor">
                <img alt="bold" src="<?php echo BN_URL_ASSETS; ?>/form/BasicTextEditor/bold.png" />
            </button>
            <button onclick="wrapText('i'); return false;" value="italic" id="BasicTextEditor">
                <img alt="italic" src="<?php echo BN_URL_ASSETS; ?>/form/BasicTextEditor/italic.png" />
            </button>
            <button onclick="wrapText('u'); return false;" value="underline" id="BasicTextEditor">
                <img alt="underline" src="<?php echo BN_URL_ASSETS; ?>/form/BasicTextEditor/underline.png" />
            </button>
            <button onclick="insertText('br'); return false;" value="linebreak" id="BasicTextEditor">
                <img alt="linebreak" src="<?php echo BN_URL_ASSETS; ?>/form/BasicTextEditor/linebreak.png" />
            </button>
            <textarea id="BasicTextEditor_target" name="<?php IsSetEcho($name) ?>" cols="80" rows="20"><?php IsSetEcho($content); ?></textarea>
            <?php if (isset($submit)){ ?>
            <input type="submit" value="<?php echo $submit; ?>" />
            <?php } ?>
        </fieldset>
