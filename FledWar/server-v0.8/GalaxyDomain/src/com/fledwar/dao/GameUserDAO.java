/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.dao;

import com.fledwar.vto.user.GameUser;
import com.fledwar.vto.user.Password;
import com.fledwar.vto.user.SessionId;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class GameUserDAO extends BasicDAO<GameUser>
{

  private static final Logger logger = Logger.getLogger(GameUserDAO.class);

  public GameUserDAO(DBCollection collection)
          throws BasicDAOException
  {
    super(collection);
  }

  @Override
  protected GameUser factory(Map data)
  {
    return new GameUser(data);
  }

  public SessionId login(String username, String password)
          throws BasicDAOException
  {
    GameUser user = findByUsername(username);
    if (user == null) {
      logger.warn(String.format(
              "unable to login user because not found [%s]",
              username));
      return null;
    }

    Password expected = user.getPassword();
    Password actual = new Password(password, user.getSalt());

    if (!expected.equals(actual)) {
      logger.warn(String.format(
              "unable to login user because password mismatch [%s]",
              username));
      return null;
    }

    SessionId session_id = new SessionId();
    user.setSessionId(session_id);
    user.triggerSessionTime();
    update(user);

    logger.warn(String.format(
            "user successfully logged in [%s]",
            username));

    return session_id;
  }

  public void logout(String username, SessionId session)
          throws BasicDAOException
  {
    GameUser user = findByUsername(username);
    if (user == null) {
      throw new BasicDAOException(String.format(
              "HACK WARNING: unable to logout "
              + "user because not found [%s]",
              username));
    }

    SessionId expected_session = user.getSessionId();
    if (expected_session == null) {
      throw new BasicDAOException(String.format(
              "HACK WARNING: unable to login user because "
              + "session does not exist [%s]",
              username));
    }

    if (!expected_session.equals(session)) {
      throw new BasicDAOException(String.format(
              "HACK WARNING: unable to logout user because "
              + "session mismatch [user:%s]",
              username));
    }

    user.setSessionId(null);
    user.nullSessionTime();
    update(user);
  }

  public GameUser fetchSessionedUser(String username,
                                       SessionId session_id)
          throws BasicDAOException
  {
    BasicDBObject query = new BasicDBObject()
            .append(GameUser.NAME, username)
            .append(GameUser.SESSION_ID, session_id.toByteArray());
    BasicDBObject update = new BasicDBObject().append(
            BasicDAO.UPDATE_SET,
            new BasicDBObject(GameUser.SESSION_TIME, System.currentTimeMillis()));
    DBObject result = super.getCollection().findAndModify(
            query, null, null, false, update, true, false);
    return (result != null) ? factory((Map) result) : null;
  }

  public boolean checkSessionedUser(String username,
                                    SessionId session_id,
                                    long timeout)
          throws BasicDAOException
  {
    BasicDBObject query = new BasicDBObject()
            .append(GameUser.NAME, username)
            .append(GameUser.SESSION_ID, session_id.toByteArray());
    GameUser user = findOne(query);

    if (user == null) {
      // this means the user doesnt even have an
      // active session
      return false;
    }

    long currtime = System.currentTimeMillis();
    long timeout_at = user.getSessionTime() + timeout;
    if (currtime > timeout_at) {
      // if this happens, then we need to logout the
      // user and return false
      logout(user.getName(), user.getSessionId());
      return false;
    }

    // if we get here then we need to update the user time
    // to make sure the timeout timer is reset. then return 
    // true that it is a valid session still
    user.triggerSessionTime();
    update(user);

    return true;
  }

  public GameUser findByUsername(String username)
          throws BasicDAOException
  {
    BasicDBObject query = new BasicDBObject()
            .append(GameUser.NAME, username);
    return findOne(query);
  }

  public List<GameUser> getLoggedInUsers()
          throws BasicDAOException
  {
    BasicDBObject query = new BasicDBObject().append(
            GameUser.SESSION_ID,
            new BasicDBObject(BasicDAO.QUERY_NOT_EQUAL, null));

    return find(query);
  }
}
