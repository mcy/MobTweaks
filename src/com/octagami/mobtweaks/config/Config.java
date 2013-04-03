
package com.octagami.mobtweaks.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import com.octagami.mobtweaks.MobTweaks;

public class Config {

    private MobTweaks plugin;
    
	private final String CONFIG_NAME = "config.yml";

	public boolean DEBUG = false;
	
    @SuppressWarnings("unused")
	private String configVersion = "";

    private boolean witherSkeletonsOnly = true;
    
    public int babyZombiePercentage = 5;
    public int babyPigZombiePercentage = 5;
    public int slimeToWitchPercentage = 10;
    public int pigzombieToWitherskeletonPercentage = 25;
    public int endermanEndstonePercentageWorld = 50;
    public int endermanEndstonePercentageTheEnd = 25;
    public int pigZombieLevel1SignPercentage = 15;
    public int pigZombieLevel2SignPercentage = 5;
    public int pigZombieLevel3SignPercentage = 1;
    
    public boolean doPigZombieSigns = true;
    public boolean doPigZombiePants = true;
	
    public Config(MobTweaks plugin) {
        this.plugin = plugin;
    }
    
    public void reload() {

        loadSettings();
    }
    
    public void loadSettings(){
    	
    	if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
    	
        File f = new File(plugin.getDataFolder(), CONFIG_NAME);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Unable to create " + CONFIG_NAME + "! No config options were loaded!");
                return;
            }
        }
        
        YamlConfiguration conf = new YamlConfiguration();
        conf.options().pathSeparator('/');
        conf.options().header(new StringBuilder().append(plugin.getDescription().getName() +  " configuration").append(System.getProperty("line.separator")).toString());
        
        try {
            conf.load(f);
        } catch (Exception e) {
            plugin.getLogger().severe("==================== " + plugin.getDescription().getName() + " ====================");
            plugin.getLogger().severe("Unable to load " + CONFIG_NAME);
            plugin.getLogger().severe("Check your config for formatting issues!");
            plugin.getLogger().severe("No config options were loaded!");
            plugin.getLogger().severe("Error: " + e.getMessage());
            plugin.getLogger().severe("====================================================");
            return;
        }

        if (!conf.contains("debug"))
        	conf.set("debug", false);
        DEBUG = conf.getBoolean("debug");
        
        if (!conf.contains("version"))
        	conf.set("version", 0.1);
        configVersion = conf.getString("version");
        
        if (!conf.contains("wither-skeletons-only"))
        	conf.set("wither-skeletons-only", true);
        witherSkeletonsOnly = conf.getBoolean("wither-skeletons-only");
        
        if (!conf.contains("baby-zombie-percentage"))
        	conf.set("baby-zombie-percentage", 5);
        babyZombiePercentage = conf.getInt("baby-zombie-percentage");
        
        if (!conf.contains("baby-pigzombie-percentage"))
        	conf.set("baby-pigzombie-percentage", 5);
        babyPigZombiePercentage = conf.getInt("baby-pigzombie-percentage");
        
        if (!conf.contains("slime-to-witch-percentage"))
        	conf.set("slime-to-witch-percentage", 10);
        slimeToWitchPercentage = conf.getInt("slime-to-witch-percentage");
        
        if (!conf.contains("pigzombie-to-witherskeleton-percentage"))
        	conf.set("pigzombie-to-witherskeleton-percentage", 25);
        pigzombieToWitherskeletonPercentage = conf.getInt("pigzombie-to-witherskeleton-percentage");
        
        if (!conf.contains("enderman-endstone-world-percentage"))
        	conf.set("enderman-endstone-world-percentage", 50);
        endermanEndstonePercentageWorld = conf.getInt("enderman-endstone-world-percentage");
        
        if (!conf.contains("enderman-endstone-end-percentage"))
        	conf.set("enderman-endstone-end-percentage", 25);
        endermanEndstonePercentageTheEnd = conf.getInt("enderman-endstone-end-percentage");
        
        if (!conf.contains("pigzombie-level-1-sign-percentage"))
        	conf.set("pigzombie-level-1-sign-percentage", 15);
        pigZombieLevel1SignPercentage = conf.getInt("pigzombie-level-1-sign-percentage");
        
        if (!conf.contains("pigzombie-level-2-sign-percentage"))
        	conf.set("pigzombie-level-2-sign-percentage", 5);
        pigZombieLevel2SignPercentage = conf.getInt("pigzombie-level-2-sign-percentage");
        
        if (!conf.contains("pigzombie-level-3-sign-percentage"))
        	conf.set("pigzombie-level-3-sign-percentage", 1);
        pigZombieLevel3SignPercentage = conf.getInt("pigzombie-level-3-sign-percentage");
        
        if (!conf.contains("do-pigzombie-signs"))
        	conf.set("do-pigzombie-signs", true);
        doPigZombieSigns = conf.getBoolean("do-pigzombie-signs");
        
        if (!conf.contains("do-pigzombie-pants"))
        	conf.set("do-pigzombie-pants", true);
        doPigZombiePants = conf.getBoolean("do-pigzombie-pants");
        
        try {
            conf.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean witherSkeletonsOnly() {
    	
    	return witherSkeletonsOnly;
    }

}
