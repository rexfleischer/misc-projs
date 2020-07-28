        <div id="<?php IsSetEcho($menuid); ?>" class="_mch1_m_container">
            <div class="_mch1_m_upper">
                <?php if(isset($title)){?>
                <span class="text"><?php echo $title; ?></span>
                <?php } ?>
            </div>
            <div class="_mch1_m_middle">
                <?php
                IsSetEcho($content);
                ?>
            </div>
            <div class="_mch1_m_lower">
            </div>
        </div>