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
package me.tabinol.factoid.listeners;

import me.tabinol.factoid.BKVersion;
import me.tabinol.factoid.Factoid;
import me.tabinol.factoid.config.players.PlayerConfEntry;
import me.tabinol.factoid.config.players.PlayerStaticConfig;
import me.tabinol.factoid.parameters.PermissionList;
import me.tabinol.factoidapi.lands.IDummyLand;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

/**
 * Players listener (for 1.8+)
 */
public class PlayerListener18 extends CommonListener implements Listener {

	/** The player conf. */
	private PlayerStaticConfig playerConf;

	/**
	 * Instantiates a new player listener.
	 */
	public PlayerListener18() {

		super();
		playerConf = Factoid.getThisPlugin().iPlayerConf();
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {

		IDummyLand land;
		EntityType et = event.getRightClicked().getType();
		Player player = event.getPlayer();
		Material mat = player.getItemInHand().getType();
		PlayerConfEntry entry;
		Location loc = event.getRightClicked().getLocation();

		Factoid.getThisPlugin().iLog().write(
				"PlayerInteractAtEntity player name: " + event.getPlayer().getName()
						+ ", Entity: " + et.name());

		// Citizen bug, check if entry exist before
		if ((entry = playerConf.get(player)) != null
				&& !entry.isAdminMod()) {
			land = Factoid.getThisPlugin().iLands().getLandOrOutsideArea(loc);
			
			// Remove and add an item from an armor stand
			if(BKVersion.isArmorStand(et)) {
				if (((!checkPermission(land, event.getPlayer(), PermissionList.BUILD.getPermissionType())
						|| !checkPermission(land, event.getPlayer(), PermissionList.BUILD_DESTROY.getPermissionType()))
						&& mat == Material.AIR)
						|| ((!checkPermission(land, event.getPlayer(), PermissionList.BUILD.getPermissionType())
								|| !checkPermission(land, event.getPlayer(), PermissionList.BUILD_PLACE.getPermissionType()))
								&& mat != Material.AIR)) {
					messagePermission(player);
					event.setCancelled(true);
				}
			}
		}
	}
}
