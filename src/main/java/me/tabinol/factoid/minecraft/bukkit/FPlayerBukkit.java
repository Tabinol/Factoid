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

package me.tabinol.factoid.minecraft.bukkit;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.tabinol.factoid.lands.areas.Point;
import me.tabinol.factoid.minecraft.FPlayer;

public class FPlayerBukkit extends FSenderBukkit implements FPlayer {
	
	private final Player player;
	
	protected FPlayerBukkit(Player player) {
		
		super(player, player.getUniqueId());
		this.player = player;
	}

	@Override
    public Point getLocation() {
	    
		Location loc = player.getLocation();
		
		return new Point(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

	@Override
    public String getName() {

		return player.getName();
    }
	
	@Override
	public UUID getUUID() {
		
		return player.getUniqueId();
	}

	@Override
    public String getDisplayName() {

		return player.getDisplayName();
    }

	@Override
    public boolean isOnline() {

		return player.isOnline();
    }
}
