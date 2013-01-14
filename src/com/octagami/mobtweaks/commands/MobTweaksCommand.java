package com.octagami.mobtweaks.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.octagami.mobtweaks.MobTweaks;

public class MobTweaksCommand implements CommandExecutor {
	
	private MobTweaks plugin;

    public MobTweaksCommand(MobTweaks plugin) {
        this.plugin = plugin;

    }
	
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
    	
    	if (args.length > 0) {
    		
    		if (args[0].equalsIgnoreCase("help")) {
				
				showVersion(sender);
				
			}else if (args[0].equalsIgnoreCase("reload")) {
				
				plugin.reload();
				sender.sendMessage(plugin.getDescription().getName() + "has been reloaded");
			}
    		
    	} else {

    		showVersion(sender);
		}
    	
    	return true;
    }
    
    public void showVersion(CommandSender sender) {
    	
    	sender.sendMessage(plugin.getDescription().getName() + " " + plugin.getDescription().getVersion());
    }
	
}
