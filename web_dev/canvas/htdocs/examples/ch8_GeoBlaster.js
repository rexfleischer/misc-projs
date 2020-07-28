
function canvasApp()
{
    var canvas = document.getElementById("canvas");
    var context = canvas.getContext("2d");
    
    const GAME_STATE_TITLE = 0;
    const GAME_STATE_NEW_GAME = 1;
    const GAME_STATE_NEW_LEVEL = 2;
    const GAME_STATE_PLAYER_START = 3;
    const GAME_STATE_PLAY_LEVEL = 4;
    const GAME_STATE_PLAYER_DIE = 5;
    const GAME_STATE_GAME_OVER = 6;
    var current_game_state = 0;
    var current_game_state_function = null;
    
    var title_started = false;
    var game_over_started = false;
    
    var score = 0;
    var level = 0;
    var extra_ship_at_each = 10000;
    var extra_ships_earned = 0;
    var player_ships = 3;
    
    var xMin = 0;
    var xMax = 400;
    var yMin = 0;
    var yMax = 400;
    
    var big_rock_score = 50;
    var med_rock_score = 75;
    var sml_rock_score = 100;
    var saucer_score = 300;
    
    const ROCK_SCALE_LARGE = 1;
    const ROCK_SCALE_MEDIUM = 2;
    const ROCK_SCALE_SMALL = 3;
    
    var player = {};
    var rocks = [];
    var saucers = [];
    var player_missiles = [];
    var particles = [];
    var saucer_missiles = [];
    
    var level_rock_max_speed_adjust = 1;
    var level_saucer_max = 1;
    var level_saucer_occurrence_rate = 25;
    var level_saucer_speed = 1;
    var level_saucer_fire_delay = 300;
    var level_saucer_fire_rate = 30;
    var level_saucer_missile_speed = 1;
    
    var key_pressed_list = [];
    
    function run_game()
    {
        current_game_state_function();
    }
    
    function switch_game_state(new_state)
    {
        current_game_state = new_state;
        switch(current_game_state)
        {
            case GAME_STATE_TITLE:
                current_game_state_function = game_state_title;
                break;
            case GAME_STATE_NEW_GAME:
                current_game_state_function = game_state_new_game;
                break;
            case GAME_STATE_NEW_LEVEL:
                current_game_state_function = game_state_new_level;
                break;
            case GAME_STATE_PLAYER_START:
                current_game_state_function = game_state_player_start;
                break;
            case GAME_STATE_PLAY_LEVEL:
                current_game_state_function = game_state_play_level;
                break;
            case GAME_STATE_PLAYER_DIE:
                current_game_state_function = game_state_player_die;
                break;
            case GAME_STATE_GAME_OVER:
                current_game_state_function = game_state_game_over;
                break;
        }
    }
    
    function game_state_title()
    {
        if (!title_started)
        {
            fill_background();
            set_text_style();
            context.fillText("Geo Blaster Basic", 130, 70);
            context.fillText("Press Space To Play", 120, 140);
            
            title_started = true;
        }
        else
        {
            if (key_pressed_list[32] == true)
            {
                ConsoleLog.log("space pressed");
                switch_game_state(GAME_STATE_NEW_GAME);
                title_started = false;
            }
        }
    }
    
    function game_state_new_game()
    {
        ConsoleLog.log("game_state_new_game");
        
        level = 0;
        score = 0;
        player_ships = 3;
        player.max_velocity = 5;
        player.width = 20;
        player.height = 20;
        player.half_width = 20;
        player.half_height = 20;
        player.rotational_velocity = Math.PI / 36;
        player.thrust_acceleration = .05;
        player.missile_frame_delay = 5;
        player.thrust = false;
        
        fill_background();
        render_score_board();
        switch_game_state(GAME_STATE_NEW_LEVEL);
    }
    
    function game_state_new_level()
    {
        rocks = [];
        saucers = [];
        player_missiles = [];
        particles = [];
        saucer_missiles = [];
        level++;
        
        level_rock_max_speed_adjust = Math.min(level * 0.25, 3);
        level_saucer_max = Math.min(1 + Math.floor(level/10), 5);
        level_saucer_occurrence_rate = Math.min(10 + 3 * level, 35);
        level_saucer_speed = Math.min(1 + 0.5 * level, 20);
        level_saucer_fire_delay = Math.max(120 - 10 * level, 20);
        level_saucer_fire_rate = Math.max(20 + 3 * level, 50);
        level_saucer_missile_speed = Math.min(1 + 0.2 * level, 4);
        
        for(var i = 0; i < level + 3; i++)
        {
            var rock = {};
            rock.scale = 1;
            rock.width = 50;
            rock.height = 50;
            rock.half_width = 25;
            rock.half_height = 25;
            
            rock.x = Math.floor(Math.random() * 50);
            rock.y = Math.floor(Math.random() * 50);
            
            rock.dx = (Math.random()*2) + level_rock_max_speed_adjust;
            if (Math.random() < 0.5)
            {
                rock.dx *= -1;
            }
            rock.dy = (Math.random()*2) + level_rock_max_speed_adjust;
            if (Math.random() < 0.5)
            {
                rock.dy *= -1;
            }
            
            // delta rotation
            rock.dr = (Math.random() * 5) + 1;
            if (Math.random() < 0.5)
            {
                rock.dr *= -1;
            }
            rock.rotation = 0;
            
            rock.score_value = big_rock_score;
            
            rocks.push(rock);
        }
        reset_player();
        switch_game_state(GAME_STATE_PLAYER_START);
    }
    
    function game_state_player_start()
    {
        fill_background();
        render_score_board();
        if (player.alpha < 1)
        {
            player.alpha += 0.02;
            context.globalAlpha = player.alpha;
        }
        else
        {
            switch_game_state(GAME_STATE_PLAY_LEVEL);
        }
        render_player_ship(player.x, player.y, 270, 1);
        context.globalAlpha = 1;
        update_rocks();
        render_rocks();
    }
    
    function game_state_play_level()
    {
        check_keys();
        update();
        render();
        check_collisions();
        check_for_extra_ship();
        check_for_end_of_level();
        frame_rate_counter.count_frames();
    }
    
    function reset_player()
    {
        player.rotation = 3 * Math.PI / 2;
        player.x = 0.5 * xMax;
        player.y = 0.5 * yMax;
        player.facingX = 0;
        player.facingY = 0;
        player.movingX = 0;
        player.movingY = 0;
        player.alpha = 0
        player.missile_frame_count = 0;
    }
    
    function check_for_extra_ship()
    {
        if (Math.floor(score / extra_ship_at_each) > extra_ships_earned)
        {
            player_ships++;
            extra_ships_earned++;
        }
    }
    
    function check_for_end_of_level()
    {
        if (rocks.length == 0)
        {
            switch_game_state(GAME_STATE_NEW_LEVEL);
        }
    }
    
    function check_object_boarder(object)
    {
        if (object.x > xMax)
        {
            object.x = -object.width;
        }
        else if (object.x < -object.width)
        {
            object.x = xMax;
        }
        
        if (object.y > yMax)
        {
            object.y = -object.height;
        }
        else if (object.y < -object.height)
        {
            object.y = yMax;
        }
    }
    
    function game_state_player_die()
    {
        if (particles.length > 0 || player_missiles.length > 0)
        {
            fill_background();
            render_score_board();
            update_rocks();
            update_saucers();
            update_particles();
            update_saucer_missiles();
            update_player_missiles();
            render_rocks();
            render_saucers();
            render_saucer_missiles();
            render_player_missiles();
            frame_rate_counter.count_frames();
        }
        else
        {
            player_ships--;
            if (player_ships < 1)
            {
                switch_game_state(GAME_STATE_GAME_OVER);
            }
            else
            {
                reset_player();
                switch_game_state(GAME_STATE_PLAYER_START);
            }
        }
    }
    
    function game_state_game_over()
    {
        if (game_over_started != true)
        {
            fill_background();
            render_score_board();
            set_text_style();
            context.fillText("Game Over!", 150, 70);
            context.fillText("Press Space To Play", 120, 140);
            
            game_over_started = true;
        }
        else
        {
            if (key_pressed_list[32] == true)
            {
                switch_game_state(GAME_STATE_TITLE);
                game_over_started = false;
            }
        }
    }
    
    function fill_background()
    {
        context.fillStyle = "#000000";
        context.fillRect(xMin, yMin, xMax, yMax);
    }
    
    function set_text_style()
    {
        context.fillStyle = "#ffffff";
        context.font = "15px _sans";
        context.textBaseline = "top";
    }
    
    function render_score_board()
    {
        context.fillStyle = "#ffffff";
        context.fillText("Score " + score, 10, 20);
        render_player_ship(200, 16, 270, 0.75);
        context.fillText("X " + player_ships, 220, 20);
        context.fillText("FPS: " + frame_rate_counter.last_frame_count, 300, 20);
    }
    
    function check_keys()
    {
        if (key_pressed_list[38] == true)
        {
            player.facingX = Math.cos(player.rotation);
            player.facingY = Math.sin(player.rotation);
            
            var movingXNew = player.movingX + player.thrust_acceleration * player.facingX;
            var movingYNew = player.movingY + player.thrust_acceleration * player.facingY;
            
            var current_velocity = Math.sqrt((movingXNew * movingXNew) + (movingYNew * movingYNew));
            
            if (current_velocity < player.max_velocity)
            {
                player.movingX = movingXNew;
                player.movingY = movingYNew;
            }
            player.thrust = true;
        }
        else
        {
            player.thrust = false;
        }
        
        if (key_pressed_list[37] == true)
        {
            player.rotation -= player.rotational_velocity;
        }
        
        if (key_pressed_list[39] == true)
        {
            player.rotation += player.rotational_velocity;
        }
        
        if (key_pressed_list[32] == true)
        {
            if (player.missile_frame_count > player.missile_frame_delay)
            {
                fire_player_missile();
                player.missile_frame_count = 0;
            }
        }
    }
    
    function update()
    {
        update_player();
        update_player_missiles();
        update_rocks();
        update_saucers();
        update_saucer_missiles();
        update_particles();
    }
    
    function render()
    {
        fill_background();
        render_score_board();
        render_player_ship(player.x, player.y, player.rotation, 1);
        render_player_missiles();
        render_rocks();
        render_saucers();
        render_saucer_missiles();
        render_particles();
    }
    
    function update_player()
    {
        player.missile_frame_count++;
        
        player.x += player.movingX;
        player.y += player.movingY;
        
        check_object_boarder(player);
    }
    
    function update_player_missiles()
    {
        for(var i = player_missiles.length - 1; i >= 0; i--)
        {
            var missile = player_missiles[i];
            missile.x += missile.dx;
            missile.y += missile.dy;
            
            check_object_boarder(missile);
            
            missile.life_ctr++;
            if (missile.life_ctr > missile.life)
            {
                player_missiles.splice(i, 1);
                missile = null;
            }
        }
    }
    
    function update_rocks()
    {
        for(var i = rocks.length - 1; i >= 0; i--)
        {
            var rock = rocks[i];
            rock.x += rock.dx;
            rock.y += rock.dy;
            rock.rotation += rock.dr;
            
            check_object_boarder(rock);
        }
    }
    
    function update_saucers()
    {
        var saucer = {};
        
        if (saucers.length < level_saucer_max &&
            Math.floor(Math.random() * 100) <= level_saucer_occurrence_rate)
        {
            saucer.width = 28;
            saucer.height = 13;
            saucer.half_width = 14;
            saucer.half_height = 6.5;
            saucer.score_value = saucer_score;
            saucer.fire_rate = level_saucer_fire_rate;
            saucer.fire_delay = level_saucer_fire_delay;
            saucer.fire_delay_count = 0;
            saucer.missile_speed = level_saucer_missile_speed;
            
            saucer.dy = 2 * Math.random();
            if (Math.random() < 0.5)
            {
                saucer.dy *= -1;
            }
            
            if (Math.random() < 0.5)
            {
                saucer.x = 450;
                saucer.dx = -1 * level_saucer_speed;
            }
            else 
            {
                saucer.x = -50;
                saucer.dx = level_saucer_speed
            }
            
            saucer.missile_speed = level_saucer_missile_speed;
            saucer.fire_delay = level_saucer_fire_delay;
            saucer.fire_rate = level_saucer_fire_rate;
            saucer.y = Math.floor(Math.random() * 100);
            
            saucers.push(saucer);
        }
        
        for(var i = saucers.length - 1; i >= 0; i--)
        {
            saucer = saucers[i];
            saucer.fire_delay_count++;
            
            if (Math.floor(Math.random() * 100) <= saucer.fire_rate &&
                saucer.fire_delay_counter > saucer.fire_delay)
            {
                fire_saucer_missile(saucer);
                saucer.fire_delay_counter = 0;
            }
            
            var remove = false;
            saucer.x += saucer.dx;
            saucer.y += saucer.dy;
            
            if (saucer.dx > 0 && saucer.x > xMax)
            {
                remove = true;
            }
            else if (saucer.dx < 0 && saucer.x < xMin - saucer.width)
            {
                remove = true;
            }
            
            if (saucer.y > yMax || saucer.y < yMin - saucer.width)
            {
                saucer.dy *= -1;
            }
            
            if (remove)
            {
                saucers.splice(i, 1);
            }
        }
    }
    
    function update_saucer_missiles()
    {
        for(var i = saucer_missiles.length - 1; i >= 0; i--)
        {
            var missile = saucer_missiles[i];
            missile.x += missile.dx;
            missile.y += missile.dy;
            
            check_object_boarder(missile);
            
            missile.life_ctr++;
            
            if (missile.life_ctf > missile.life)
            {
                saucer_missiles.splice(i, 1);
            }
        }
    }
    
    function update_particles()
    {
        for(var i = particles.length - 1; i >= 0; i--)
        {
            var remove = false;
            var particle = particles[i];
            particle.x += particle.dx;
            particle.y += particle.dy;
            
            particle.life_ctr++;
            
            if (particle.life_ctr > particle.life)
            {
                remove = true;
            }
            else if ((particle.x > xMax) || 
                     (particle.x < xMin) ||
                     (particle.y > yMax) ||
                     (particle.y < yMin))
            {
                remove = true;
            }
                
            if (remove)
            {
                particles.splice(i, 1);
                particle = null;
            }
        }
    }
    
    function render_player_ship(x, y, rotation, scale)
    {
        context.save();
        context.setTransform(1, 0, 0, 1, 0, 0);
        context.translate(x + player.half_width, y + player.half_height);
        context.rotate(rotation);
        context.scale(scale, scale);
        
        context.strokeStyle = "#ffffff";
        context.beginPath();
        
        context.moveTo(-10, -10);
        context.lineTo(10, 0);
        context.moveTo(10, 1);
        context.lineTo(-10, 10);
        context.lineTo(1, 1);
        context.moveTo(1, -1);
        context.lineTo(-10, -10);
        
        if (player.thrust && scale == 1)
        {
            context.moveTo(-4, -2);
            context.lineTo(-4, 1);
            context.moveTo(-5, -1);
            context.lineTo(-10, -1);
            context.moveTo(-5, 0);
            context.lineTo(-10, 0);
        }
        
        context.stroke();
        context.closePath();
        
        context.restore();
    }
    
    function render_player_missiles()
    {
        for(var i = 0; i < player_missiles.length; i++)
        {
            var missile = player_missiles[i];
            context.save();
            context.setTransform(1, 0, 0, 1, 0, 0);
            
            context.translate(missile.x + 1, missile.y + 1);
            context.strokeStyle = "#ffffff";
            
            context.beginPath();
            context.moveTo(-1, -1);
            context.lineTo(1, -1);
            context.lineTo(1, 1);
            context.lineTo(-1, 1);
            context.lineTo(-1, -1);
            context.stroke();
            context.closePath();
            context.restore();
        }
    }
    
    function render_rocks()
    {
        for(var i = 0; i < rocks.length; i++)
        {
            var rock = rocks[i];
            context.save();
            context.setTransform(1, 0, 0, 1, 0, 0);
            
            context.translate(rock.x + rock.half_width, rock.y + rock.half_height);
            context.rotate(rock.rotation);
            context.strokeStyle = "#ffffff";
            context.beginPath();
            
            context.moveTo(-(rock.half_width - 1), -(rock.half_height - 1));
            context.lineTo((rock.half_width - 1), -(rock.half_height - 1));
            context.lineTo((rock.half_width - 1), (rock.half_height - 1));
            context.lineTo(-(rock.half_width - 1), (rock.half_height - 1));
            context.lineTo(-(rock.half_width - 1), -(rock.half_height - 1));
            
            context.stroke();
            context.closePath();
            context.restore();
        }
    }
    
    function render_saucers()
    {
        for(var i = 0; i < saucers.length; i++)
        {
            var missile = saucers[i];
            context.save();
            context.setTransform(1, 0, 0, 1, 0, 0);
            
            context.translate(missile.x + missile.half_width, missile.y + missile.half_height);
            context.strokeStyle = "#ffffff";
            
            context.beginPath();
            context.moveTo(4, 0);
            context.lineTo(9, 0);
            context.lineTo(12, 3);
            context.lineTo(13, 3);
            context.moveTo(13, 4);
            context.lineTo(10, 7);
            context.lineTo(3, 7);
            context.lineTo(1, 5);
            context.lineTo(12, 5);
            context.moveTo(0, 4);
            context.lineTo(0, 3);
            context.lineTo(13, 3);
            context.moveTo(5, 1);
            context.lineTo(5, 2);
            context.moveTo(8, 1);
            context.lineTo(8, 2);
            context.moveTo(2, 2);
            context.lineTo(4, 0);
            context.stroke();
            context.closePath();
            context.restore();
        }
    }
    
    function render_saucer_missiles()
    {
        for(var i = 0; i < saucer_missiles.length; i++)
        {
            var missile = saucer_missiles[i];
            context.save();
            context.setTransform(1, 0, 0, 1, 0, 0);
            
            context.translate(missile.x + 1, missile.y + 1);
            context.strokeStyle = "#ffffff";
            
            context.beginPath();
            context.moveTo(-1, -1);
            context.lineTo(1, -1);
            context.lineTo(1, 1);
            context.lineTo(-1, 1);
            context.lineTo(-1, -1);
            context.stroke();
            context.closePath();
            context.restore();
        }
    }
    
    function render_particles()
    {
        for(var i = 0; i < particles.length; i++)
        {
            var particle = particles[i];
            context.save();
            context.setTransform(1, 0, 0, 1, 0, 0);
            
            context.translate(particle.x, particle.y);
            context.strokeStyle = "#ffffff";
            
            context.beginPath();
            context.moveTo(0, 0);
            context.lineTo(1, 1);
            context.stroke();
            context.closePath();
            context.restore();
        }
    }
    
    function check_collisions()
    {
        rocks: for(var i = rocks.length - 1; i >= 0; i--)
        {
            var rock = rocks[i];
            
            missiles: for(var m = player_missiles.length - 1; m >= 0; m--)
            {
                var missile = player_missiles[m];
                
                if (bounding_box_collide(rock, missile))
                {
                    create_explode(rock.x + rock.half_width, rock.y + rock.half_height, 10);
                    if (rock.scale < 3)
                    {
                        split_rock(rock.scale + 1, rock.x, rock.y);
                    }
                    add_to_score(rock.score_value);
                    player_missiles.splice(m, 1);
                    missile = null;
                    
                    rock.splice(i, 1);
                    rock = null;
                    
                    break rocks;
                    break missiles;
                }
            }
            
            saucers: for(var s = saucers.length - 1; s >= 0; s--)
            {
                var saucer = saucers[s];
                if (bounding_box_collide(rock, saucer))
                {
                    create_explode(saucer.x + saucer.half_width, saucer.y + saucer.half_height, 10);
                    create_explode(rock.x + rock.half_width, rock.y + rock.half_height, 10);
                    
                    if (rock.scale < 3)
                    {
                        split_rock(rock.scale + 1, rock.x, rock.y);
                    }
                    
                    saucers.splice(s, 1);
                    rock = null;
                    
                    break rocks;
                    break saucers;
                }
            }
            
            saucer_missiles: for(var sm = saucer_missiles.length - 1; sm >= 0; sm--)
            {
                var saucer_missile = saucer_missiles[sm];
                
                if (bounding_box_collide(rock, saucer_missile))
                {
                    create_explode(rock.x + rock.half_width, rock.y + rock.half_height, 10);
                    if (rock.scale < 3)
                    {
                        split_rock(rock.scale + 1, rock.x, rock.y);
                    }
                    saucer_missiles.splice(sm, 1);
                    rock = null;
                    
                    break rocks;
                    break saucer_missiles;
                }
            }
            
            if (bounding_box_collide(rock, player))
            {
                create_explode(rock.x + rock.half_width, rock.y + rock.half_height, 10);
                add_to_score(rock.score_value);
                if (rock.scale < 3)
                {
                    split_rock(rock.scale + 1, rock.x, rock.y);
                }
                rocks.splice(sm, 1);
                rock = null;
                
                player_die();
            }
        }
        
        saucers: for(s = saucers.length - 1; s >= 0; s--)
        {
            saucer = saucers[s];
            
            missiles: for(var pm = player_missiles.length - 1; pm >= 0; pm--)
            {
                missile = player_missiles[pm];
                if (bounding_box_collide(saucer, missile))
                {
                    create_explode(saucer.x + saucer.half_width, saucer.y + saucer.half_height, 10);
                    add_to_score(saucer.score_value);
                    player_missiles.splice(pm, 1);
                    missile = null;
                    
                    saucers.splice(s, 1);
                    saucer = null;
                    
                    break saucers;
                    break missiles;
                }
            }
            
            if (bounding_box_collide(saucer, player))
            {
                create_explode(saucer.x + saucer.half_width, saucer.y + saucer.half_height, 10);
                add_to_score(saucer.score_value);
                
                saucers.splice(s, 1);
                saucer = null;
                player_die();
            }
        }
        
        saucer_missiles: for(sm = saucer_missiles.length - 1; sm >= 0; sm--)
        {
            missile = saucer_missiles[sm];
            
            if (bounding_box_collide(player, missile))
            {
                player_die();
                saucer_missiles.splice(sm, 1);
                missile = null;
                break saucer_missiles;
            }
        }
    }
    
    function fire_player_missile()
    {
        var missile = {};
        missile.dx = 5 * Math.cos(player.rotation);
        missile.dy = 5 * Math.sin(player.rotation);
        missile.x = player.x + player.half_width;
        missile.y = player.y + player.half_height;
        missile.life = 60;
        missile.life_ctr = 0;
        missile.width = 2;
        missile.height = 2;
        player_missiles.push(missile);
    }
    
    function fire_saucer_missile(saucer)
    {
        var missile = {};
        missile.x = saucer.x + 0.5 * saucer.width;
        missile.y = saucer.y + 0.5 * saucer.height;
        missile.width = 2;
        missile.height = 2;
        missile.speed = saucer.missile_speed;
        var radians = Math.atan2(player.x - saucer.x, player.y - saucer.y);
        missile.dx = saucer.missile_speed * Math.cos(radians);
        missile.dy = saucer.missile_speed * Math.sin(radians);
        missile.life = 60;
        missile.life_ctr = 0;
        saucer_missiles.push(missile);
    }
    
    function player_die()
    {
        create_explode(player.x + player.half_width, player.y + player.half_height, 10);
        switch_game_state(GAME_STATE_PLAYER_DIE);
    }
    
    function create_explode(x, y, num)
    {
        for(var i = 0; i < num; i++)
        {
            var particle = new Object();
            particle.dx = Math.random() * 3;
            if (Math.random() < 0.5)
            {
                particle.dy *= -1;
            }
            particle.dy = Math.random() * 3;
            if (Math.random() < 0.5)
            {
                particle.dy *= -1;
            }
            particle.life = Math.floor(Math.random() * 30 + 30);
            particle.life_ctr = 0;
           
            particle.x = x;
            particle.y = y;
            
            particles.push(particle);
        }
    }
    
    function bounding_box_collide(o1, o2)
    {
        if (o1.y + o1.height < o2.y)
        {
            return false;
        }
        if (o1.y > o2.y + o2.height)
        {
            return false;
        }
        if (o1.x + o1.width < o2.x)
        {
            return false;
        }
        if (o1.x > o2.x + o2.width)
        {
            return false;
        }
        
        return true;
    }
    
    function split_rock(scale, x, y)
    {
        for(var i = 0; i < 2; i++)
        {
            var rock = {};
            if (scale == 2)
            {
                rock.score_value = med_rock_score;
                rock.width = 25;
                rock.height = 25;
                rock.half_width = 12.5;
                rock.half_height = 12.5;
            }
            else
            {
                rock.score_value = sml_rock_score;
                rock.width = 16;
                rock.height = 16;
                rock.half_width = 8;
                rock.half_height = 8;
            }
            
            rock.scale = scale;
            rock.x = x;
            rock.y = y;
            rock.dx = Math.random() * 3;
            if (Math.random() < 0.5)
            {
                rock.dx *= -1;
            }
            rock.dy = Math.random() * 3;
            if (Math.random() < 0.5)
            {
                rock.dy *= -1;
            }
            rock.dr = Math.random() * 5 + 1;
            if (Math.random() < 0.5)
            {
                rock.dr *= -1;
            }
            rock.rotation = 0;
            rocks.push(rock);
        }
    }
    
    function add_to_score(value)
    {
        score += value;
    }
    
    document.onkeydown = function(e)
    {
        e = e ? e : window.event;
        key_pressed_list[e.keyCode] = true;
    }
    
    document.onkeyup = function(e)
    {
        e = e ? e : window.event;
        key_pressed_list[e.keyCode] = false;
    }
    
    switch_game_state(GAME_STATE_TITLE);
    frame_rate_counter = new FrameRateCounter();
    
    const FRAME_RATE = 40;
    var interval_time = 1000/FRAME_RATE;
    setInterval(run_game, interval_time);
}

function ConsoleLog(){}

console_log = function(message)
{
    if (typeof(console) !== 'undefined' && console != null)
    {
        console.log(message);
    }
}

ConsoleLog.log = console_log;

function FrameRateCounter()
{
    this.last_frame_count = 0;
    var date = new Date();
    this.frame_last = date.getTime();
    delete date;
    this.frame_ctr = 0;
}
FrameRateCounter.prototype.count_frames = function()
{
    var date = new Date();
    this.frame_ctr++;
    
    if (date.getTime() >= this.frame_last + 1000)
    {
        this.last_frame_count = this.frame_ctr;
        this.frame_last = date.getTime();
        this.frame_ctr = 0;
    }
    
    delete date;
}