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

package me.tabinol.factoid.lands.areas;

import me.tabinol.factoid.Factoid;
import me.tabinol.factoid.minecraft.FWorld;

/**
 * Represent a location
 * @author Tabinol
 *
 */
public class Point implements Comparable<Point> {
	
	private final String worldName;
	private final double x;
	private final double y;
	private final double z;
	private final float yaw;
	private final float pitch;
	
	public Point(String worldName, double x, double y, double z) {
		
		this(worldName, x, y, z, 0, 0);
	}
	
	public Point(String worldName, double x, double y, double z, float yaw, float pitch) {
		
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public boolean equals(Point point2) {
		
		return worldName.equals(point2.worldName) && x == point2.x && y == point2.y && z == point2.z;
	}
	
	@Override
    public int compareTo(Point t) {

        int worldCompare = worldName.compareTo(t.worldName);
        if (worldCompare != 0) {
            return worldCompare;
        }
        if (x < t.x) {
            return -1;
        }
        if (x > t.x) {
            return 1;
        }
        if (z < t.z) {
            return -1;
        }
        if (z > t.z) {
            return 1;
        }
        if (y < t.y) {
            return -1;
        }
        if (y > t.y) {
            return 1;
        }

        return 0;
    }

	/**************************************************************************
	 * Gets
	 *************************************************************************/
	
	public String getWorldName() {
		
		return worldName;
	}
	
	public FWorld getWorld() {
		
		return Factoid.getServer().getWorld(worldName);
	}
	
	public double getX() {
		
		return x;
	}

	public double getY() {
		
		return y;
	}
	
	public double getZ() {
		
		return z;
	}
	
	public int getBlockX() {
		
		return (int) x;
	}

	public int getBlockY() {
		
		return (int) y;
	}
	
	public int getBlockZ() {
		
		return (int) z;
	}
	
	public float getYaw() {
		
		return yaw;
	}
	
	public float getPitch() {
		
		return pitch;
	}
	
	public double distance(Point pt2) {

		double dx = x - pt2.x;
	    double dy = y - pt2.y;
	    double dz = z - pt2.z;

	    return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
}
