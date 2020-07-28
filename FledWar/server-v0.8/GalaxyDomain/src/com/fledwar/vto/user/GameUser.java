/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.user;

import com.fledwar.vto.BaseVTO;
import com.fledwar.vto.BasicVTO;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bson.types.ObjectId;

/**
 * 
 * @author REx
 */
public class GameUser extends BaseVTO
{
    public static final Pattern VALID_USERNAME = Pattern.compile(
            "^[a-zA-Z][-a-zA-Z0-9_@#$%^&*()]{3,24}$");
    
    public static final String PASSWORD = "password";
    
    public static final Pattern VALID_PASSWORD = Pattern.compile(
            "^[-a-zA-Z0-9_\"':;\\\\/+=\\[\\]{}<>,\\.\\|!@#$%^&*()]{5,30}$");
    
    public static final String SALT = "salt";
    
    public static final String SESSION_ID = "session_id";
    
    public static final String SESSION_TIME = "session_time";
    
    public static final String EMAIL = "email";
    
    public static final Pattern VALID_EMAIL = Pattern.compile(
            "^.{5,100}$"); // we will send validation emails...
    
    public static final String USER_STATE = "user_state";
    
    public static final String RIGHTS = "rights";
    
    public static final String USER_SETTINGS = "user_settings";
    
    public static final String SESSION_VARS = "session_vars";
    
    public static final String MESSAGES = "messages";
    
    public GameUser()
    {
        nullSessionTime();
    }
    
    public GameUser(Map data)
    {
        super(data);
    }
    
    @Override
    public void setName(String username)
    {
        Matcher pass_check = VALID_USERNAME.matcher(username);
        if (!pass_check.matches())
        {
            throw new IllegalArgumentException(
                    String.format("username %s is invalid", username));
        }
        put(NAME, username);
    }
    
    public String getEmail()
    {
        return getAsString(EMAIL);
    }
    
    public void setEmail(String email)
    {
        Matcher email_check = VALID_EMAIL.matcher(email);
        if (!email_check.matches())
        {
            throw new IllegalArgumentException("email is not valid");
        }
        put(EMAIL, email);
    }
    
    public UserState getUserState()
    {
        return UserState.valueOf(getAsString(USER_STATE));
    }
    
    public void setUserState(UserState state)
    {
        put(USER_STATE, state.name());
    }
    
    public Password getPassword()
    {
        return new Password(getAsByteArray(PASSWORD));
    }
    
    public String getSalt()
    {
        return getAsString(SALT);
    }
    
    private String generateNewSaltAndSet()
    {
        String salt = Password.genrateNewSalt();
        put(SALT, salt);
        return salt;
    }
    
    public void setPassword(String password)
    {
        Matcher pass_check = VALID_PASSWORD.matcher(password);
        if (!pass_check.matches())
        {
            throw new IllegalArgumentException("password is not valid");
        }
        Password new_pass = new Password(
                password, 
                generateNewSaltAndSet());
        put(PASSWORD, new_pass.toByteArray());
    }
    
    public SessionId getSessionId()
    {
        byte[] bytes = getAsByteArray(SESSION_ID);
        return (bytes != null) ? new SessionId(bytes) : null;
    }
    
    public void setSessionId(SessionId session_id)
    {
        if (session_id == null)
        {
            put(SESSION_ID, null);
        }
        else
        {
            put(SESSION_ID, session_id.toByteArray());
        }
    }
    
    public long getSessionTime()
    {
        return getAsLong(SESSION_TIME);
    }
    
    public void triggerSessionTime()
    {
        put(SESSION_TIME, System.currentTimeMillis());
    }
    
    public void nullSessionTime()
    {
        put(SESSION_TIME, -1);
    }
    
    public List<String> getRights()
    {
        return ensuredListGet(RIGHTS);
    }
    
    public List<Map> getMessages()
    {
        return ensuredListGet(MESSAGES);
    }
    
    public BasicVTO getUserSettings()
    {
        return new BasicVTO(ensuredMapGet(USER_SETTINGS));
    }
    
    public BasicVTO getSessionVars()
    {
        return new BasicVTO(ensuredMapGet(SESSION_VARS));
    }
}
