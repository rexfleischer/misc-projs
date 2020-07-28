<?php
foreach($pagelist as $value){
?>
        <div class="clear">
            <a href="<?php echo buildurl(BN_URL_PAGE_USERMANUAL, array(USERMANUAL_PAGEID => $value[USERMANUAL_PAGEID])); ?>">
                 <?php echo $value[USERMANUAL_TITLE]; ?>
            </a>
        </div>

<?php } ?>
