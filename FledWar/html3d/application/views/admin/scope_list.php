<?php include VIEWPATH . "header.php"; ?>

<?php include VIEWPATH . "admin/adminnav.php"; ?>

<table border="3">
  <tr>
    <th>_id: </th>
    <th>name: </th>
    <th>type: </th>
    <th>&nbsp;</th>
  </tr>
  <?php foreach($scopes as $scope): ?>
  <tr>
    <th>&nbsp;<?php echo $scope["_id"]; ?>&nbsp;</th>
    <th>&nbsp;<?php echo $scope["name"]; ?>&nbsp;</th>
    <th>&nbsp;<?php echo $scope["scope_type"]; ?>&nbsp;</th>
    <th>&nbsp;<a href="<?php echo $this->config->item("base_url"); ?>admin/scopeview/<?php echo $scope["_id"]; ?>">view</a>&nbsp;</th>
  </tr>
  <?php endforeach; ?>
</table>



<?php include VIEWPATH . "footer.php" ?>