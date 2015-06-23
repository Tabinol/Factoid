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
package me.tabinol.factoid.event.sponge;

import org.spongepowered.api.event.Cancellable;

import me.tabinol.factoid.lands.DummyLand;
import me.tabinol.factoid.lands.Land;
import me.tabinol.factoid.lands.areas.Point;
import me.tabinol.factoid.minecraft.FPlayer;

/**
 * The Class PlayerLandChangeEvent. This event is called every time a player
 * moves from a land to an other, or to an other world.
 */
public class PlayerLandChangeEvent extends LandEvent implements Cancellable {
    
    /** The cancelled. */
    protected boolean cancelled = false;
    
    /** The player. */
    FPlayer player;
    
    /** The from loc. */
    Point fromLoc;
    
    /** The to loc. */
    Point toLoc;
    
    /** The last land. */
    Land lastLand;
    
    /** The last dummy land. */
    DummyLand lastDummyLand;
    
    /** The if this is a player teleport. */
    boolean isTp;

    /**
     * Instantiates a new player land change event.
     *
     * @param lastDummyLand the last dummy land
     * @param dummyLand the actual dummy land
     * @param player the player
     * @param fromLoc from location
     * @param toLoc the to location
     * @param isTp the is a player teleport
     */
    public PlayerLandChangeEvent(final DummyLand lastDummyLand, final DummyLand dummyLand, final FPlayer player, 
            final Point fromLoc, final Point toLoc, final boolean isTp) {

        super(dummyLand);
        this.lastDummyLand = lastDummyLand;
        
        if(lastDummyLand instanceof Land) {
            lastLand = (Land) lastDummyLand;
        } else {
            lastLand = null;
        }
        
        this.player = player;
        this.fromLoc = fromLoc;
        this.toLoc = toLoc;
        this.isTp = isTp;
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.Cancellable#isCancelled()
     */
    public boolean isCancelled() {
        
        return cancelled;
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.Cancellable#setCancelled(boolean)
     */
    public void setCancelled(boolean bln) {
        
        cancelled = bln;
    }
    
    /**
     * Gets the player.
     *
     * @return the player
     */
    public FPlayer getPlayer() {
        
        return player;
    }
    
    /**
     * Gets the last land.
     *
     * @return the last land
     */
    public Land getLastLand() {
        
        return lastLand;
    }
    
    /**
     * Gets the last land or outside area (World).
     *
     * @return the last land or dummy land (World)
     */
    public DummyLand getLastLandOrOutside() {
        
        return lastDummyLand;
    }
    
    /**
     * Gets the from location.
     *
     * @return the from location
     */
    public Point getFromLoc() {
        
        return fromLoc;
    }

    /**
     * Gets the to location.
     *
     * @return the to location
     */
    public Point getToLoc() {
        
        return toLoc;
    }
    
    /**
     * Checks if this is a player teleport.
     *
     * @return true, if it is a player teleport
     */
    public boolean isTp() {
        
        return isTp;
    }
}