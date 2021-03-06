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
package me.tabinol.factoid.exceptions;

import me.tabinol.factoid.Factoid;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


/**
 * The Class FactoidCommandException.
 */
public class FactoidCommandException extends Exception {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 5585486767311219615L;

	/**
     * Instantiates a new factoid command exception.
     *
     * @param logMsg the log msg
     * @param sender the sender
     * @param langMsg the lang msg
     * @param param the param
     */
    public FactoidCommandException(String logMsg, CommandSender sender, String langMsg, String... param) {
        
        super(logMsg);
        if (sender != null) {
            Factoid.getThisPlugin().iLog().write("Player: " + sender.getName() + ", Lang Msg: " + langMsg + ", " + logMsg);
        } else {
            Factoid.getThisPlugin().iLog().write(logMsg);
        }
        if (sender != null) {
            sender.sendMessage(ChatColor.RED + "[Factoid] " + Factoid.getThisPlugin().iLanguage().getMessage(langMsg, param));
        }
    }
}
