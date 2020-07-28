/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model;

import com.mongodb.DBObject;
import com.rf.fledwar.model.util.OrbitStatus;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class GalaxySystem extends BasicVTO
{
    public static final String ID = "_id";
    
    public static final String NAME = "name";
    
    public static final String LAST_UPDATE = "last_update";
    
    public static final String ORBIT_STATUS_GALAXY = "orbit_status_galaxy";
    
    public static final String ORBIT_STATUS_CENTER = "orbit_status_center";
    
    public static final String ORBIT_STATUS = "orbit_status";
    
    public static final String ORBITALS = "orbitals";
    
    public static final String SYSTEM_TYPE = "system_type";
    
    public static final String STATE = "state";
    
    public static final String UPDATE_COUNT = "update_count";

    public GalaxySystem()
    {
        super();
    }

    public GalaxySystem(Map data)
    {
        super(data);
    }
    
    public ObjectId getId()
    {
        Object check = this.get(ID);
        ObjectId result = null;
        if (check instanceof ObjectId)
        {
            result = (ObjectId) check;
        }
        if (check instanceof Map)
        {
            Map checkmap = (Map) check;
            result = new ObjectId(
                    ((Number) checkmap.get("_time")).intValue(), 
                    ((Number) checkmap.get("_machine")).intValue(), 
                    ((Number) checkmap.get("_inc")).intValue());
        }
        return result;
    }

    public String getName()
    {
        return (String) this.get(NAME);
    }

    public void setName(String name)
    {
        this.put(NAME, name);
    }

    public long getLastUpdate()
    {
        return ((Number) this.get(LAST_UPDATE)).longValue();
    }

    public void triggerLastUpdate()
    {
        this.put(LAST_UPDATE, System.currentTimeMillis());
    }
    
    public void incrementUpdateCount()
    {
        this.put(UPDATE_COUNT, getUpdateCount() + 1);
    }
    
    public int getUpdateCount()
    {
        Integer check = (Integer) this.get(UPDATE_COUNT);
        return (check != null) ? check.intValue() : 0;
    }
    
    public void setState(GalaxySystemState state)
    {
        this.put(STATE, state.ordinal());
    }
    
    public GalaxySystemState getState()
    {
        Integer state = (Integer) this.get(STATE);
        return GalaxySystemState.ordinalOf(state);
    }
    
    public void setSystemType(GalaxySystemType type)
    {
        this.put(SYSTEM_TYPE, type.ordinal());
    }
    
    public GalaxySystemType getSystemType()
    {
        Integer state = (Integer) this.get(SYSTEM_TYPE);
        return GalaxySystemType.ordinalOf(state);
    }

    /**
     * this gets the orbit status of this galaxy system with respect
     * to the center of the galaxy
     * @return 
     */
    public OrbitStatus getGalaxyOrbitStatus()
    {
        return new OrbitStatus(this.ensuredObjectGet(ORBIT_STATUS_GALAXY));
    }
    
    /**
     * this gets the orbit status with respect to the system or
     * object that it is actually orbiting.
     * @return 
     */
    public OrbitStatus getCenterOrbitStatus()
    {
        return new OrbitStatus(this.ensuredObjectGet(ORBIT_STATUS_CENTER));
    }
    
    /**
     * this gets a list of orbit statuses that are used within
     * the galaxy for complex orbiting. 
     * @return 
     */
    public BasicVTOList<OrbitStatus> getSubOrbitStatus()
    {
        return new BasicVTOList<>(
                ensuredListGet(ORBIT_STATUS),
                DustCloud.class);
    }
    
    public void addOrbital(Orbital orbital)
    {
        OrbitalType type = orbital.getOrbitalType();
        if (type == null || type == OrbitalType.PLANET_MOON)
        {
            throw new IllegalArgumentException(
                    String.format("invalid orbital type [%s]", type));
        }
        String name = orbital.getName();
        if (name == null || name.isEmpty())
        {
            throw new IllegalArgumentException(
                    "orbital.name cannot be null or empty");
        }
        DBObject object = orbital.getDBObject();
        
        Map map = ensuredObjectGet(ORBITALS);
        map.put(name, object);
    }
    
    public Orbital getOrbital(String name)
    {
        Map map = ensuredObjectGet(ORBITALS);
        Map orbital = (Map) map.get(name);
        
        if (orbital == null)
        {
            return null;
        }
        else
        {
            String type = (String) orbital.get(Orbital.ORBITAL_TYPE);
            OrbitalType provider = OrbitalType.valueOf(type);
            return provider.getNew(orbital);
        }
    }
    
    public Set<String> getOrbitalNames()
    {
        return ensuredObjectGet(ORBITALS).keySet();
    }
}
