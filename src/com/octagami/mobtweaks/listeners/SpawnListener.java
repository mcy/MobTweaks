package com.octagami.mobtweaks.listeners;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Enderman;
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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import com.octagami.mobtweaks.MobTweaks;

public class SpawnListener implements Listener {

	private final MobTweaks plugin;

	private Random random = new Random();

	public SpawnListener(MobTweaks plugin) {

		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
		
		if (event.getEntity().getType() != EntityType.ENDERMAN)
			return;
		
		Enderman enderman = (Enderman)event.getEntity();
		
		MaterialData blockData = enderman.getCarriedMaterial();
		
		if (blockData == null)
			return;
		
		if (blockData.getItemType() != Material.AIR) {
			
			ItemStack item = new ItemStack(blockData.getItemType());
			enderman.getLocation().getWorld().dropItemNaturally(enderman.getLocation(), item);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event) {

		if (!event.getSpawnReason().equals(SpawnReason.NATURAL)){
			if(event.getEntityType() == EntityType.PIG_ZOMBIE){
				if(plugin.getPluginConfig().doPigZombieSigns)
					event.getEntity().getEquipment().setItemInHand(createBattleSign(false));
				if(plugin.getPluginConfig().doPigZombiePants)
					event.getEntity().getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));			}			
			return;
		}
		
		if (!(event.getEntity() instanceof LivingEntity))
			return;

		if (event.getLocation().getWorld().getName().equals("world")) {

			if (event.getEntityType() == EntityType.ZOMBIE) {
				
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
			else if (event.getEntityType() == EntityType.ENDERMAN) {
				
				if (random.nextInt(100) < plugin.getPluginConfig().endermanEndstonePercentageWorld) {
					
					Enderman enderman = (Enderman)event.getEntity();
					
					enderman.setCarriedMaterial(new MaterialData(121));	
					
					if (plugin.getPluginConfig().DEBUG)
						logChange(event.getEntity(), enderman);
				}
				
			}

		} else if (event.getLocation().getWorld().getName().equals("world_nether")) {

			if (event.getEntityType() == EntityType.PIG_ZOMBIE) {

				Location spawnLoc = event.getLocation();

				if (spawnLoc.getBlock().getRelative(BlockFace.DOWN, 1).getType() == Material.NETHER_BRICK) {
					
					if (random.nextInt(100) < plugin.getPluginConfig().pigzombieToWitherskeletonPercentage) {

						Skeleton skeleton = (Skeleton) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.SKELETON);
						skeleton.setSkeletonType(SkeletonType.WITHER);
						skeleton.getEquipment().setItemInHand(new ItemStack(Material.STONE_SWORD));

						if (plugin.getPluginConfig().DEBUG)
							logChange(event.getEntity(), skeleton);

						event.setCancelled(true);
					}
					
				}else {
					
					PigZombie pigZombie = (PigZombie) event.getEntity();
					
					if(plugin.getPluginConfig().doPigZombieSigns)
						pigZombie.getEquipment().setItemInHand(createBattleSign(true));
					if(plugin.getPluginConfig().doPigZombiePants)
						pigZombie.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
					
					if (random.nextInt(100) < plugin.getPluginConfig().babyPigZombiePercentage) {	
						
						if (plugin.getPluginConfig().DEBUG)
							logChange(pigZombie, pigZombie);

						pigZombie.setBaby(true);
					}
					
				}

			} else if (event.getEntityType() == EntityType.SKELETON && plugin.getPluginConfig().witherSkeletonsOnly()) {

				Skeleton skeleton = (Skeleton) event.getEntity();

				if (skeleton.getSkeletonType() == SkeletonType.NORMAL) {
					
					if (plugin.getPluginConfig().DEBUG)
						logChange(skeleton, skeleton);

					skeleton.setSkeletonType(SkeletonType.WITHER);
				}

			}

		} else if (event.getLocation().getWorld().getName().equals("world_the_end")) {
			
			if (event.getEntityType() == EntityType.ENDERMAN) {
				
				if (random.nextInt(100) < plugin.getPluginConfig().endermanEndstonePercentageTheEnd) {
					
					Enderman enderman = (Enderman)event.getEntity();
					
					enderman.setCarriedMaterial(new MaterialData(121));	
					
					if (plugin.getPluginConfig().DEBUG)
						logChange(event.getEntity(), enderman);
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
	
	private ItemStack createBattleSign(boolean enchanted){
		final ItemStack sign = new ItemStack(Material.SIGN);
		
		ItemMeta meta = sign.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Battlesign");
		sign.setItemMeta(meta);
		
		if(!enchanted)
			return sign;
		
		final int chance = random.nextInt(100);
		int givenLevel = 0;
				
		if(chance < plugin.getPluginConfig().pigZombieLevel3SignPercentage){
			sign.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 3);
			sign.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
			sign.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
			givenLevel = 3;
		}
		
		else if(chance < plugin.getPluginConfig().pigZombieLevel2SignPercentage){
			sign.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
			sign.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
			sign.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
			givenLevel = 2;
		}
		
		else if(chance < plugin.getPluginConfig().pigZombieLevel1SignPercentage){
			sign.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
			sign.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
			givenLevel = 1;
		}
		
		if(plugin.getPluginConfig().DEBUG)
			plugin.getLogger().info("Created battlesign of level " + givenLevel + ".");
					
		return sign;
		
	}

}
