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

import me.tabinol.factoidapi.parameters.IPermission;

import org.bukkit.ChatColor;


/**
 * The Class Permission.
 */
public class Permission implements IPermission {

    /** The perm type. */
    PermissionType permType;
    
    /** The value. */
    boolean value;
    
    /** The heritable. */
    boolean heritable;

    /**
     * Instantiates a new permission.
     *
     * @param permType the perm type
     * @param value the value
     * @param heritable the heritable
     */
    public Permission(final PermissionType permType, final boolean value, final boolean heritable) {

        this.permType = permType;
        this.value = value;
        this.heritable = heritable;
    }

    public Permission copyOf() {
    	
    	return new Permission(permType, value, heritable);
    }
    
    /**
     * Gets the perm type.
     *
     * @return the perm type
     */
    public PermissionType getPermType() {

        return permType;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public boolean getValue() {

        return value;
    }

    /**
     * Gets the value print.
     *
     * @return the value print
     */
    public final String getValuePrint() {

        if (value) {
            return "" + ChatColor.GREEN + value;
        } else {
            return "" + ChatColor.RED + value;
        }
    }

    /**
     * Checks if is heritable.
     *
     * @return true, if is heritable
     */
    public boolean isHeritable() {

        return heritable;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return permType.toString() + ":" + value + ":" + heritable;
    }
}
