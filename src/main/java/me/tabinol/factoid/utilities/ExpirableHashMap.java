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
package me.tabinol.factoid.utilities;

import java.util.HashMap;

import me.tabinol.factoid.Factoid;

// TODO Auto-generated Javadoc
/**
 * The Class ExpirableTreeMap.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class ExpirableHashMap<K, V> extends HashMap<K, V> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8255110767996977825L;
	
	/** The delay in tick. */
	private final long delay;
	
	/**
	 * Instantiates a new expirable tree map.
	 *
	 * @param delay the delay (in ticks)
	 */
	public ExpirableHashMap(long delay) {
		
		super();
		this.delay = delay;
	}
	
	/**
	 * The Class BestBefored.
	 */
	private class BestBefored extends FactoidRunnable {

		/** The key. */
		K key;
		
		/**
		 * Instantiates a new best befored.
		 *
		 * @param key the key
		 */
		private BestBefored(K key) {
			
			this.key = key;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			remove(key);
			this.setOneTimeDone();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.TreeMap#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V put(K key, V value) {
		
		Factoid.getServer().createTask(new BestBefored(key), delay, false);
		return super.put(key, value);
	}
}
