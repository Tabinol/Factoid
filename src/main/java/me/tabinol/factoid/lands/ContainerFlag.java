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

import me.tabinol.factoid.parameters.LandFlag;
import java.util.HashSet;
import me.tabinol.factoid.playercontainer.PlayerContainer;


/**
 * The Class ContainerFlag.
 */
public class ContainerFlag {
    
    /** The pc. */
    PlayerContainer pc;
    
    /** The flags. */
    HashSet <LandFlag> flags;
    
    /**
     * Instantiates a new container flag.
     *
     * @param pc the pc
     * @param flags the flags
     */
    public ContainerFlag(PlayerContainer pc, HashSet <LandFlag> flags) {
        
        this.pc = pc;
        if(flags == null) {
            this.flags = new HashSet<LandFlag>();
        } else {
            this.flags = flags;
        }
    }
    
    /**
     * Equals.
     *
     * @param cf2 the cf2
     * @return true, if successful
     */
    public boolean equals(ContainerFlag cf2) {
        
        return pc.equals(cf2.pc);
    }
}
