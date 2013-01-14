package com.octagami.mobtweaks;

import org.bukkit.plugin.java.JavaPlugin;

import com.octagami.mobtweaks.commands.MobTweaksCommand;
import com.octagami.mobtweaks.config.Config;
import com.octagami.mobtweaks.listeners.SpawnListener;

/**
 * MobTweaks
 * 
 * Website: https://github.com/octagami
 * 
 */

public class MobTweaks extends JavaPlugin {
	
	private Config config = null;

	@Override
	public void onEnable() {
		
		config = new Config(this);
		config.loadSettings();

		getCommand("mobtweaks").setExecutor(new MobTweaksCommand(this));
		
		getServer().getPluginManager().registerEvents(new SpawnListener(this), this);
	}
	
	public void reload() {
	
		config.reload();
	}

	@Override
	public void onDisable() {

		config = null;
		
		getServer().getScheduler().cancelTasks(this);
	}
	
	public Config getPluginConfig() {
    	
    	return config;
    }
	
}