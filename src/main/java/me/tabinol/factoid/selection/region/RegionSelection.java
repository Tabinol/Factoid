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
package me.tabinol.factoid.selection.region;

import me.tabinol.factoid.selection.PlayerSelection.SelectionType;
import org.bukkit.entity.Player;


/**
 * The Class RegionSelection.
 */
public abstract class RegionSelection {

    /** The selection type. */
    final SelectionType selectionType;
    
    /** The player. */
    final Player player;

    /**
     * Instantiates a new region selection.
     *
     * @param selectionType the selection type
     * @param player the player
     */
    RegionSelection(SelectionType selectionType, Player player) {

        this.selectionType = selectionType;
        this.player = player;
    }

    /**
     * Gets the selection type.
     *
     * @return the selection type
     */
    public SelectionType getSelectionType() {

        return selectionType;
    }
    
    /**
     * Remove any visual and replace blocks.
     */
    public abstract void removeSelection();
}
