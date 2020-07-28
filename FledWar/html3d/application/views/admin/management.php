<?php include VIEWPATH . "header.php"; ?>

<?php include VIEWPATH . "admin/adminnav.php"; ?>


<!-- start management nav -->

<div class="navbar">
  <!--<div class="navbar-inner">-->
  <div>
    <div class="container">
      <div class="nav-collapse collapse">
        <ul class="nav">
          <li id="nav-home"><a href="<?php echo $this->config->item("base_url"); ?>admin/management_refresh">refresh</a></li>
          <li id="nav-home"><a href="<?php echo $this->config->item("base_url"); ?>admin/management_reset">reset</a></li>
        </ul>
      </div><!--/.nav-collapse -->
    </div>
  </div>
</div>

<!-- end management nav -->

<pre>
result:

<?php print_r($result); ?>
</pre>

<?php include VIEWPATH . "footer.php" ?>