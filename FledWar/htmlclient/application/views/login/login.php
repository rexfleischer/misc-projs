<?php include VIEWPATH . "header.php"; ?>


<form method='post'
      accept-charset='UTF-8' 
      action="<?php echo $this->config->item("base_url"); ?>login/attempt/"
      class="form-inline">
    <fieldset>
        <input type='text' name="username" class="input-large" id='username' placeholder="Username" />

        <input type='password' name="password" class="input-large" id='password' placeholder="Password" />

        <button type="submit" class="btn">Login</button>
    </fieldset>
</form>

<?php if(array_key_exists("error", $_REQUEST)): ?>
<i class="icon-warning-sign"></i><p class="text-error">there was an error while logging in</p>
<?php endif; ?>


<script>
    $(function(){
        $("#username").focus();
    });
</script>


<?php include VIEWPATH . "footer.php" ?>