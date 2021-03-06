/*
 Factoid: Lands and Factions plugin for Minecraft server
 Copyright (C) 2014 Kaz00, Tabinol

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.tabinol.factoid.parameters;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import me.tabinol.factoidapi.parameters.IParameters;

import org.bukkit.Material;


/**
 * The Class Parameters.
 *
 * @author Tabinol
 */
public class Parameters implements IParameters {

    /** The permissions. */
    private final TreeMap<String, PermissionType> permissions;
    
    /** The flags. */
    private final TreeMap<String, FlagType> flags;
    
    /** List of unregistered flags for an update **/
    protected final List<LandFlag> unRegisteredFlags;
    
    /** Special permission Map Prefix-->Material-->PermissionType */
    private final Map<SpecialPermPrefix, Map<Material, PermissionType>> specialPermMap;

    /**
     * Instantiates a new parameters.
     */
    public Parameters() {

        permissions = new TreeMap<String, PermissionType>();
        flags = new TreeMap<String, FlagType>();
        unRegisteredFlags = new ArrayList<LandFlag>();

        // Add flags and permissions
        for (PermissionList permissionList : PermissionList.values()) {
            permissionList.setPermissionType(registerPermissionType(permissionList.name(), permissionList.baseValue));
        }
        for (FlagList flagList : FlagList.values()) {
            flagList.setFlagType(registerFlagType(flagList.name(), flagList.baseValue));
        }
        // Add special permissions (PLACE_XXX and DESTROY_XXX, NOPLACE_XXX, NODESTROY_XXX)
        specialPermMap = new EnumMap<SpecialPermPrefix, Map<Material, PermissionType>>(SpecialPermPrefix.class);
        
        for(SpecialPermPrefix pref : SpecialPermPrefix.values()) {
        	Map<Material, PermissionType> matPerms = new EnumMap<Material, PermissionType>(Material.class);
            for(Material mat : Material.values()) {
            	matPerms.put(mat, registerPermissionType(pref.name() + "_" + mat.name(), false));
            }
            specialPermMap.put(pref, matPerms);
        }
    }

    /**
     * Register permission type.
     *
     * @param permissionName the permission name
     * @param defaultValue the default value
     * @return the permission type
     */
    public final PermissionType registerPermissionType(String permissionName, boolean defaultValue) {

        String permissionNameUpper = permissionName.toUpperCase();
        PermissionType permissionType = getPermissionTypeNoValid(permissionNameUpper);
        permissionType.setDefaultValue(defaultValue);
        permissionType.setRegistered();
        
        return permissionType;
    }

    /**
     * Register flag type.
     *
     * @param flagName the flag name
     * @param defaultValue the default value
     * @return the flag type
     */
    public final FlagType  registerFlagType(String flagName, 
    		Object defaultValue) {

    	FlagValue flagDefaultValue;
    	
    	// Check is default value is raw or is FlagDefaultValue
    	if(defaultValue instanceof FlagValue) {
    		flagDefaultValue = (FlagValue) defaultValue;
    	} else {
    		flagDefaultValue = new FlagValue(defaultValue);
    	}
    	
    	String flagNameUpper = flagName.toUpperCase();
        FlagType flagType = getFlagTypeNoValid(flagNameUpper);
        flagType.setDefaultValue(flagDefaultValue);
        flagType.setRegistered();
        
        // Update flag registration (for correct type)
        Iterator<LandFlag> iFlag = unRegisteredFlags.iterator();
        while (iFlag.hasNext()) {
           LandFlag flag = iFlag.next();
           if(flagType == flag.getFlagType()) {
        	   String str = flag.getValue().getValueString();
        	   flag.setValue(FlagValue.getFromString(str, flagType));
        	   iFlag.remove();
           }
        }
        
        return flagType;
    	
    }
    
    /**
     * Gets the permission type.
     *
     * @param permissionName the permission name
     * @return the permission type
     */
    public final PermissionType getPermissionType(String permissionName) {

        PermissionType pt = permissions.get(permissionName);

        if (pt != null && pt.isRegistered()) {
            return permissions.get(permissionName);
        } else {
            return null;
        }
    }

    /**
     * Gets the flag type.
     *
     * @param flagName the flag name
     * @return the flag type
     */
    public final FlagType getFlagType(String flagName) {

        FlagType ft = flags.get(flagName);

        if (ft != null && ft.isRegistered()) {
            return flags.get(flagName);
        } else {
            return null;
        }
    }

    /**
     * Gets the permission type no valid.
     *
     * @param permissionName the permission name
     * @return the permission type no valid
     */
    public final PermissionType getPermissionTypeNoValid(String permissionName) {

        PermissionType pt = permissions.get(permissionName);
        
        if(pt == null) {
            pt = new PermissionType(permissionName, false);
            permissions.put(permissionName, pt);
        }
        
        return pt;
    }

    /**
     * Gets the flag type no valid.
     *
     * @param flagName the flag name
     * @return the flag type no valid
     */
    public final FlagType getFlagTypeNoValid(String flagName) {

        FlagType ft = flags.get(flagName);
        
        if(ft == null) {
            ft = new FlagType(flagName, new String());
            flags.put(flagName, ft);
        }
        
        return ft;
    }
    
    public final PermissionType getSpecialPermission(SpecialPermPrefix prefix, Material mat) {
    	
    	Map<Material, PermissionType> matPerms = specialPermMap.get(prefix);
    	
    	if(matPerms == null) {
    		return null;
    	}
    	
    	return matPerms.get(mat);
    }
}
