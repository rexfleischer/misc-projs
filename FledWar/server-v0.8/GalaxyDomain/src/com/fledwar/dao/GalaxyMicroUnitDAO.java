/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.dao;

import com.fledwar.vto.galaxy.unit.MicroUnit;
import com.mongodb.DBCollection;
import java.util.Map;

/**
 *
 * @author REx
 */
public class GalaxyMicroUnitDAO extends BasicDAO<MicroUnit>
{

  public GalaxyMicroUnitDAO(DBCollection collection) throws BasicDAOException
  {
    super(collection);
  }

  @Override
  protected MicroUnit factory(Map data)
  {
    return new MicroUnit(data);
  }
  
}
