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

import java.util.Iterator;
import java.util.List;

import me.tabinol.factoid.Factoid;
import me.tabinol.factoid.config.Config;
import me.tabinol.factoid.parameters.FlagList;
import me.tabinol.factoidapi.lands.IDummyLand;
import me.tabinol.factoidapi.parameters.IFlagType;
import me.tabinol.factoidapi.parameters.IFlagValue;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Slime;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;


/**
 * World listener
 */
public class WorldListener extends CommonListener implements Listener {

    /** The conf. */
    private Config conf;

    /**
     * Instantiates a new world listener.
     */
    public WorldListener() {

        super();
        conf = Factoid.getThisPlugin().iConf();
    }

    /**
     * On explosion prime.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onExplosionPrime(ExplosionPrimeEvent event) {

        if (event.getEntity() == null) {
            return;
        }

        Location loc = event.getEntity().getLocation();
        IDummyLand land = Factoid.getThisPlugin().iLands().getLandOrOutsideArea(loc);
        EntityType entityType = event.getEntityType();

        // Check for Explosion cancel 
        if ((entityType == EntityType.CREEPER
                && land.getFlagAndInherit(FlagList.CREEPER_EXPLOSION.getFlagType()).getValueBoolean() == false)
                || ((entityType == EntityType.PRIMED_TNT || entityType == EntityType.MINECART_TNT)
                && land.getFlagAndInherit(FlagList.TNT_EXPLOSION.getFlagType()).getValueBoolean() == false)
                || land.getFlagAndInherit(FlagList.EXPLOSION.getFlagType()).getValueBoolean() == false) {
            event.setCancelled(true);
            if (entityType == EntityType.CREEPER) {
                event.getEntity().remove();
            }
        }
    }

    /**
     * On entity explode.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {

        if (event.getEntity() == null) {
            return;
        }

        if (conf.isOverrideExplosions()) {

            float power;

            // Creeper Explosion
            if (event.getEntityType() == EntityType.CREEPER) {
                if (((Creeper) event.getEntity()).isPowered()) {
                    // bugfix : droped item destroyed
                	power = 0L;
                } else {
                    // bugfix : droped item destroyed
                	power = 0L;
                }
                ExplodeBlocks(event, event.blockList(), FlagList.CREEPER_DAMAGE.getFlagType(), event.getLocation(),
                        event.getYield(), power, false, true);

                //  Wither
            } else if (event.getEntityType() == EntityType.WITHER_SKULL) {
                ExplodeBlocks(event, event.blockList(), FlagList.WITHER_DAMAGE.getFlagType(), event.getLocation(),
                        event.getYield(), 1L, false, true);
            } else if (event.getEntityType() == EntityType.WITHER) {
                ExplodeBlocks(event, event.blockList(), FlagList.WITHER_DAMAGE.getFlagType(), event.getLocation(),
                        event.getYield(), 7L, false, true);

                // Ghast
            } else if (event.getEntityType() == EntityType.FIREBALL) {
                ExplodeBlocks(event, event.blockList(), FlagList.GHAST_DAMAGE.getFlagType(), event.getLocation(),
                        event.getYield(), 1L, true, true);

                // TNT
            } else if (event.getEntityType() == EntityType.MINECART_TNT
                    || event.getEntityType() == EntityType.PRIMED_TNT) {
                ExplodeBlocks(event, event.blockList(), FlagList.TNT_DAMAGE.getFlagType(), event.getLocation(),
                        event.getYield(), 4L, false, true);
            } else if (event.getEntityType() == EntityType.ENDER_DRAGON) {
                ExplodeBlocks(event, event.blockList(), FlagList.ENDERDRAGON_DAMAGE.getFlagType(), event.getLocation(),
                        event.getYield(), 4L, false, false);
            }
        }
    }

    /**
     * On hanging break.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onHangingBreak(HangingBreakEvent event) {

        if (conf.isOverrideExplosions()) {
            // Check for painting
            if (event.getCause() == RemoveCause.EXPLOSION) {
                Factoid.getThisPlugin().iLog().write("Cancel HangingBreak : " + event.getEntity() + ", Cause: " + event.getCause());
                event.setCancelled(true);
            }
        }
    }

    /**
     * Explode blocks.
     *
     * @param event The cancellable event
     * @param blocks the blocks
     * @param ft the ft
     * @param loc the loc
     * @param yield the yield
     * @param power the power
     * @param setFire the set fire
     * @param doExplosion the do explosion
     */
    private void ExplodeBlocks(Cancellable event, List<Block> blocks, IFlagType ft, Location loc,
            float yield, float power, boolean setFire, boolean doExplosion) {

        IFlagValue value;
        boolean cancelEvent = false;
        Iterator<Block> itBlock = blocks.iterator();
        Block block;

        Factoid.getThisPlugin().iLog().write("Explosion : " + ", Yield: " + yield + ", power: " + power);

        // Check if 1 block or more is in a protected place
        while(itBlock.hasNext() && !cancelEvent) {
        	block = itBlock.next();
        	value = Factoid.getThisPlugin().iLands().getLandOrOutsideArea(block.getLocation()).getFlagAndInherit(ft);
            if (value.getValueBoolean() == false) {
                cancelEvent = true;
            }
        }
        
        if(cancelEvent) {
        	// Cancel Event and do a false explosion
        	event.setCancelled(true);
            loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(),
                    power, setFire, false);
        }
        
        // If not the event will be executed has is
    }

    /**
     * On entity change block.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {

        IDummyLand land = Factoid.getThisPlugin().iLands().getLandOrOutsideArea(event.getBlock().getLocation());
        Material matFrom = event.getBlock().getType();
        Material matTo = event.getTo();

        // Enderman removeblock
        if ((event.getEntityType() == EntityType.ENDERMAN
                && land.getFlagAndInherit(FlagList.ENDERMAN_DAMAGE.getFlagType()).getValueBoolean() == false)
                || (event.getEntityType() == EntityType.WITHER
                && land.getFlagAndInherit(FlagList.WITHER_DAMAGE.getFlagType()).getValueBoolean() == false)) {
            event.setCancelled(true);
        
        // Crop trample
        } else if (matFrom == Material.SOIL
        		&& matTo == Material.DIRT
                && land.getFlagAndInherit(FlagList.CROP_TRAMPLE.getFlagType()).getValueBoolean() == false) {
        	event.setCancelled(true);
        }
    }

    /**
     * On block ignite.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {

        IDummyLand land = Factoid.getThisPlugin().iLands().getLandOrOutsideArea(event.getBlock().getLocation());

        if (((event.getCause() == IgniteCause.SPREAD || event.getCause() == IgniteCause.LAVA)
                && land.getFlagAndInherit(FlagList.FIRESPREAD.getFlagType()).getValueBoolean() == false)
                || land.getFlagAndInherit(FlagList.FIRE.getFlagType()).getValueBoolean() == false) {
            event.setCancelled(true);
        }
    }

    /**
     * On block burn.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {

        IDummyLand land = Factoid.getThisPlugin().iLands().getLandOrOutsideArea(event.getBlock().getLocation());

        if ((land.getFlagAndInherit(FlagList.FIRESPREAD.getFlagType()).getValueBoolean() == false)
                || (land.getFlagAndInherit(FlagList.FIRE.getFlagType()).getValueBoolean() == false)) {
            event.setCancelled(true);
        }
    }

    /**
     * On creature spawn.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {

        IDummyLand land = Factoid.getThisPlugin().iLands().getLandOrOutsideArea(event.getEntity().getLocation());

        if ((event.getEntity() instanceof Animals
                && land.getFlagAndInherit(FlagList.ANIMAL_SPAWN.getFlagType()).getValueBoolean() == false)
                || ((event.getEntity() instanceof Monster
                || event.getEntity() instanceof Slime
                || event.getEntity() instanceof Flying)
                && land.getFlagAndInherit(FlagList.MOB_SPAWN.getFlagType()).getValueBoolean() == false)) {
            event.setCancelled(true);
        }
    }

    /**
     * On leaves decay.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {

        IDummyLand land = Factoid.getThisPlugin().iLands().getLandOrOutsideArea(event.getBlock().getLocation());

        if (land.getFlagAndInherit(FlagList.LEAF_DECAY.getFlagType()).getValueBoolean() == false) {
            event.setCancelled(true);
        }
    }

    /**
     * On block from to.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent event) {

        IDummyLand land = Factoid.getThisPlugin().iLands().getLandOrOutsideArea(event.getBlock().getLocation());
        Material ml = event.getBlock().getType();

        // Liquid flow
        if (((ml == Material.LAVA || ml == Material.STATIONARY_LAVA)
        		&& land.getFlagAndInherit(FlagList.LAVA_FLOW.getFlagType()).getValueBoolean() == false)
        		|| ((ml == Material.WATER || ml == Material.STATIONARY_WATER)
                		&& land.getFlagAndInherit(FlagList.WATER_FLOW.getFlagType()).getValueBoolean() == false)) {
            event.setCancelled(true);
        }
    }

    /**
     * On entity damage.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {

        if (conf.isOverrideExplosions()
                && event.getEntity() instanceof Hanging
                && (event.getCause() == DamageCause.BLOCK_EXPLOSION || event.getCause() == DamageCause.ENTITY_EXPLOSION
                || event.getCause() == DamageCause.PROJECTILE)) {
            // Check for ItemFrame
            Factoid.getThisPlugin().iLog().write("Cancel HangingBreak : " + event.getEntity() + ", Cause: " + event.getCause());
            event.setCancelled(true);
        }
    }
}
