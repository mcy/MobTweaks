package com.octagami.mobtweaks.listeners;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;

import com.octagami.mobtweaks.MobTweaks;

public class SpawnListener implements Listener {

	private final MobTweaks plugin;

	private Random random = new Random();

	public SpawnListener(MobTweaks plugin) {

		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event) {

		if (!event.getSpawnReason().equals(SpawnReason.NATURAL))
			return;

		if (!(event.getEntity() instanceof LivingEntity))
			return;

		if (event.getLocation().getWorld().getName().equals("world")) {

			if (event.getEntityType() == EntityType.PIG_ZOMBIE) {
				
				if (random.nextInt(100) < plugin.getPluginConfig().babyPigZombiePercentage) {
					
					PigZombie pigZombie = (PigZombie) event.getEntity();
					
					if (plugin.getPluginConfig().DEBUG)
						logChange(pigZombie, pigZombie);

					pigZombie.setBaby(true);
				}
				
			}
			
			else if (event.getEntityType() == EntityType.ZOMBIE) {
				
				if (random.nextInt(100) < plugin.getPluginConfig().babyZombiePercentage) {
					
					Zombie zombie = (Zombie) event.getEntity();
					
					if (plugin.getPluginConfig().DEBUG)
						logChange(zombie, zombie);

					zombie.setBaby(true);
				}
				
			}
			else if (event.getEntityType() == EntityType.SLIME) {

				Location spawnLoc = event.getLocation();

				if (spawnLoc.getBlock().getBiome() != Biome.SWAMPLAND)
					return;

				if (random.nextInt(100) < plugin.getPluginConfig().slimeToWitchPercentage) {

					Witch witch = (Witch) event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.WITCH);

					if (plugin.getPluginConfig().DEBUG)
						logChange(event.getEntity(), witch);

					event.setCancelled(true);
				}

			}

		} else if (event.getLocation().getWorld().getName().equals("world_nether")) {

			if (event.getEntityType() == EntityType.PIG_ZOMBIE) {

				Location spawnLoc = event.getLocation();

				if (spawnLoc.getBlock().getRelative(BlockFace.DOWN, 1).getType() != Material.NETHER_BRICK)
					return;

				if (random.nextInt(100) < plugin.getPluginConfig().pigzombieToWitherskeletonPercentage) {

					Skeleton skeleton = (Skeleton) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.SKELETON);
					skeleton.setSkeletonType(SkeletonType.WITHER);
					skeleton.getEquipment().setItemInHand(new ItemStack(Material.STONE_SWORD));

					if (plugin.getPluginConfig().DEBUG)
						logChange(event.getEntity(), skeleton);

					event.setCancelled(true);
				}

			} else if (event.getEntityType() == EntityType.SKELETON && plugin.getPluginConfig().witherSkeletonsOnly()) {

				Skeleton skeleton = (Skeleton) event.getEntity();

				if (skeleton.getSkeletonType() == SkeletonType.NORMAL) {
					
					if (plugin.getPluginConfig().DEBUG)
						logChange(skeleton, skeleton);

					skeleton.setSkeletonType(SkeletonType.WITHER);
				}

			}

		}

	}

	public void logChange(Entity oldEntity, Entity newEntity) {

		plugin.getLogger().info(oldEntity.getType().name() + " -> " + newEntity.getType().name() + " At loc: " + 
				                (int) newEntity.getLocation().getX() + " " + 
				                (int) newEntity.getLocation().getY() + " " + 
				                (int) newEntity.getLocation().getZ());
	}

}
