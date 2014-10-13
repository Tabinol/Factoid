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
package me.tabinol.factoid.lands;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import me.tabinol.factoid.Factoid;
import me.tabinol.factoidapi.event.PlayerContainerAddNoEnterEvent;
import me.tabinol.factoidapi.lands.IDummyLand;
import me.tabinol.factoidapi.parameters.IFlagType;
import me.tabinol.factoidapi.parameters.IFlagValue;
import me.tabinol.factoidapi.parameters.ILandFlag;
import me.tabinol.factoidapi.parameters.IPermission;
import me.tabinol.factoid.parameters.PermissionList;
import me.tabinol.factoid.playercontainer.PlayerContainer;
import me.tabinol.factoidapi.parameters.IPermissionType;
import me.tabinol.factoidapi.playercontainer.IPlayerContainer;

import org.bukkit.World;
import org.bukkit.entity.Player;


/**
 * The Class DummyLand.
 */
public class DummyLand implements IDummyLand {

    /** The permissions. */
    protected TreeMap<IPlayerContainer, TreeMap<IPermissionType, IPermission>> permissions; // String for playerName
    
    /** The flags. */
    protected TreeMap<IFlagType, ILandFlag> flags;
    
    /** The world name. */
    protected String worldName;

    /**
     * Instantiates a new dummy land.
     *
     * @param worldName the world name
     */
    public DummyLand(String worldName) {

        permissions = new TreeMap<IPlayerContainer, TreeMap<IPermissionType, IPermission>>();
        flags = new TreeMap<IFlagType, ILandFlag>();
        this.worldName = worldName;
    }

    /**
     * Gets the world name.
     *
     * @return the world name
     */
    public String getWorldName() {

        return worldName;
    }

    /**
     * Gets the world.
     *
     * @return the world
     */
    public World getWorld() {

        return Factoid.getThisPlugin().getServer().getWorld(worldName);
    }

    /**
     * Adds the permission.
     *
     * @param pc the pc
     * @param perm the perm
     */
	@SuppressWarnings("deprecation")
	public void addPermission(IPlayerContainer pc, IPermission perm) {

        TreeMap<IPermissionType, IPermission> permPlayer;

        if (this instanceof Land) {
            ((PlayerContainer)pc).setLand((Land) this);
        }
        
        if (!permissions.containsKey(pc)) {
            permPlayer = new TreeMap<IPermissionType, IPermission>();
            permissions.put(pc, permPlayer);
        } else {
            permPlayer = permissions.get(pc);
        }
        permPlayer.put(perm.getPermType(), perm);
        doSave();

        // Start Event
        if (this instanceof Land && perm.getPermType() == PermissionList.LAND_ENTER.getPermissionType()
                && perm.getValue() != perm.getPermType().getDefaultValue()) {
            Factoid.getThisPlugin().getServer().getPluginManager().callEvent(
                    new PlayerContainerAddNoEnterEvent((Land) this, pc));
            
            // Deprecated to remove
            Factoid.getThisPlugin().getServer().getPluginManager().callEvent(
                    new me.tabinol.factoid.event.PlayerContainerAddNoEnterEvent((Land) this, (PlayerContainer) pc));
        }

    }

    /**
     * Removes the permission.
     *
     * @param pc the pc
     * @param permType the perm type
     * @return true, if successful
     */
    public boolean removePermission(IPlayerContainer pc, 
    		me.tabinol.factoidapi.parameters.IPermissionType permType) {

        TreeMap<IPermissionType, IPermission> permPlayer;

        if (!permissions.containsKey(pc)) {
            return false;
        }
        permPlayer = permissions.get(pc);
        if (permPlayer.remove(permType) == null) {
            return false;
        }

        // remove key for PC if it is empty
        if (permPlayer.isEmpty()) {
            permissions.remove(pc);
        }

        doSave();
        return true;
    }

    /**
     * Gets the sets the pc have permission.
     *
     * @return the sets the pc have permission
     */
    public final Set<IPlayerContainer> getSetPCHavePermission() {

        return permissions.keySet();
    }

    /**
     * Gets the permissions for pc.
     *
     * @param pc the pc
     * @return the permissions for pc
     */
    public final Collection<IPermission> getPermissionsForPC(IPlayerContainer pc) {

        return permissions.get(pc).values();
    }

    /**
     * Check permission and inherit.
     *
     * @param player the player
     * @param pt the pt
     * @return the boolean
     */
    public boolean checkPermissionAndInherit(Player player, 
    		me.tabinol.factoidapi.parameters.IPermissionType pt) {

        return checkPermissionAndInherit(player, pt, false);
    }

    /**
     * Check permission no inherit.
     *
     * @param player the player
     * @param pt the pt
     * @return the boolean
     */
    public boolean checkPermissionNoInherit(Player player, 
    		me.tabinol.factoidapi.parameters.IPermissionType pt) {

        Boolean value = getPermission(player, pt, false);
        
        if(value != null) {
        	return value;
        } else {
        	return pt.getDefaultValue();
        }
    }

    /**
     * Check permission and inherit.
     *
     * @param player the player
     * @param pt the pt
     * @param onlyInherit the only inherit
     * @return the boolean
     */
    protected Boolean checkPermissionAndInherit(Player player, 
    		me.tabinol.factoidapi.parameters.IPermissionType pt, boolean onlyInherit) {

        if (this instanceof Land) {
            return ((Land) this).checkLandPermissionAndInherit(player, pt, onlyInherit);
        }
        return Factoid.getThisPlugin().iLands().getPermissionInWorld(worldName, player, pt, onlyInherit);
    }

    /**
     * Gets the permission.
     *
     * @param player the player
     * @param pt the pt
     * @param onlyInherit the only inherit
     * @return the permission
     */
    protected Boolean getPermission(Player player, 
    		me.tabinol.factoidapi.parameters.IPermissionType pt, boolean onlyInherit) {

        for (Map.Entry<IPlayerContainer, TreeMap<IPermissionType, IPermission>> permissionEntry : permissions.entrySet()) {
            if (permissionEntry.getKey().hasAccess(player)) {
                IPermission perm = permissionEntry.getValue().get(pt);
                if (perm != null) {
                    Factoid.getThisPlugin().iLog().write("Container: " + permissionEntry.getKey().toString() + ", PermissionType: " + perm.getPermType() + ", Value: " + perm.getValue() + ", Heritable: " + perm.isHeritable());
                    if ((onlyInherit && perm.isHeritable()) || !onlyInherit) {
                        return perm.getValue();
                    }
                }
            }
        }

        return null;
    }

    /**
     * Adds the flag.
     *
     * @param flag the flag
     */
    public void addFlag(ILandFlag flag) {

        flags.put(flag.getFlagType(), flag);
        doSave();
    }

    /**
     * Removes the flag.
     *
     * @param flagType the flag type
     * @return true, if successful
     */
    public boolean removeFlag(IFlagType flagType) {

        if (flags.remove(flagType) == null) {
            return false;
        }
        doSave();
        return true;
    }

    /**
     * Gets the flags.
     *
     * @return the flags value or default
     */
    public Collection<ILandFlag> getFlags() {

        return flags.values();
    }

    public IFlagValue getFlagAndInherit(IFlagType ft) {

        return getFlagAndInherit(ft, false);
    }

    /**
     * Gets the flag no inherit.
     *
     * @param ft the ft
     * @return the flag value or default
     */
    public IFlagValue getFlagNoInherit(IFlagType ft) {

        IFlagValue value = getFlag(ft, false);
        
        if(value != null) {
        	return value;
        } else {
        	return ft.getDefaultValue();
        }
    }

    /**
     * Gets the flag and inherit.
     *
     * @param ft the ft
     * @param onlyInherit the only inherit
     * @return the flag and inherit
     */
    protected IFlagValue getFlagAndInherit(IFlagType ft, 
    		boolean onlyInherit) {

        if (this instanceof Land) {
            return ((Land) this).getLandFlagAndInherit(ft, onlyInherit);
        }
        return Factoid.getThisPlugin().iLands().getFlagInWorld(worldName, ft, onlyInherit);
    }

    /**
     * Gets the flag.
     *
     * @param ft the ft
     * @param onlyInherit the only inherit
     * @return the flag value
     */
    protected IFlagValue getFlag(IFlagType ft, 
    		boolean onlyInherit) {

        ILandFlag flag = flags.get(ft);
        if (flag != null) {
            Factoid.getThisPlugin().iLog().write("Flag: " + flag.toString());

            if ((onlyInherit && flag.isHeritable()) || !onlyInherit) {
                return flag.getValue();
            }
        }

        return null;
    }

    /**
     * Do save.
     */
    protected void doSave() {

        if (this instanceof Land) {
            ((Land) this).doSave();
        }
    }
}
