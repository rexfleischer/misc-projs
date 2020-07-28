
        <!-- beginning footer.php -->
    </div>
    
    <script>
        // this little thing is for setting the active
        // class for the nav bar... i hope there is a
        // better way for this.
        var path = window.location.pathname;
        var split_path = path.split('/');
        var active = false;
        for(var i = split_path.length - 1; i >= 0; i--) {
            if ($.trim(split_path[i]) !== '') {
                active = $.trim(split_path[i]);
                break;
            }
        }
        if (active) {
            var element = $("#nav-"+active);
            if (element !== null) {
                element.addClass("active");
            }
        }
    </script>

  </body>
</html>