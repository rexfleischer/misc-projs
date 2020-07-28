/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model;

import com.rf.fledwar.model.util.ChanceRole;

/**
 *
 * @author REx
 */
public enum PlanetType
{
    /**
     * A gas giant is a massive planet with a thick atmosphere of hydrogen 
     * and helium. They may have a dense molten core of rocky elements or 
     * the core may have completely dissolved and dispersed throughout the 
     * planet if the planet is hot enough.[4] The hydrogen and helium in 
     * "traditional" gas giants like Jupiter and Saturn constitutes most of 
     * the planet, whereas the hydrogen/helium only makes up an outer envelope 
     * on Uranus and Neptune which are sometimes called ice giants, as they 
     * are mostly composed of water, ammonia, and methane molten ices.
     * {@see: http://en.wikipedia.org/wiki/Gas_giant}
     */
    JOVIAN(30),
    
    /**
     * An ocean planet (also termed a waterworld) is a type of planet whose 
     * surface is completely covered with an ocean of water.
     * {@see: http://en.wikipedia.org/wiki/Ocean_planet}
     */
    OCEAN(30),
    
    /**
     * Such a planet would probably have an iron or steel-rich core like 
     * the known terrestrial planets. Surrounding that would be molten 
     * silicon carbide and titanium carbide. Above that, a layer of 
     * carbon in the form of graphite, possibly with a kilometers-thick 
     * substratum of diamond if there is sufficient pressure. During volcanic 
     * eruptions, it is possible that diamonds from the interior could 
     * come up to the surface, resulting in mountains of diamonds and silicon 
     * carbides. The surface would contain frozen or liquid hydrocarbons 
     * (e.g. tar and methane) and carbon monoxide.
     * {@see: http://en.wikipedia.org/wiki/Carbon_planet}
     */
    CARBON(30),
    
    /**
     * A terrestrial planet, telluric planet or rocky planet is a planet that 
     * is composed primarily of silicate rocks or metals. Within the Solar 
     * System, the terrestrial planets are the inner planets closest to the 
     * Sun. The terms are derived from Latin words for Earth (Terra and Tellus), 
     * as these planets are, in a certain way, "Earth-like".
     * {@see: http://en.wikipedia.org/wiki/Terrestrial_planet}
     */
    SILICATE(30),
    
    /**
     * Iron-rich planets may be the remnants of normal metal/silicate rocky 
     * planets whose rocky mantles were stripped away by giant impacts. Some 
     * are believed to consist of diamond fields. Current planet formation 
     * models predict iron-rich planets will form in close-in orbits or
     * orbiting massive stars where the protoplanetary disk presumably consists 
     * of iron-rich material.
     * 
     * Iron-rich planets are smaller and more dense than other types of planets 
     * of comparable mass.[3] Such planets would have no plate tectonics 
     * or strong magnetic field as they cool rapidly after formation.
     * {@see: http://en.wikipedia.org/wiki/Iron_planet}
     */
    IRON(30);
    
    
    private int _chance;
    
    private PlanetType(int chance)
    {
        this._chance = chance;
    }
    
    public static PlanetType roleType()
    {
        return (PlanetType) chance.role();
    }
    
    private final static ChanceRole chance = getChanceRole();
    
    private static ChanceRole getChanceRole()
    {
        ChanceRole result = new ChanceRole();
        for(PlanetType type : values())
        {
            result.set(type._chance, type);
        }
        return result;
    }
}
