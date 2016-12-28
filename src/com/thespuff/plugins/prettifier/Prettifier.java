package com.thespuff.plugins.prettifier;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by nsmith on 2016-12-27.
 */
public class Prettifier extends JavaPlugin implements Listener{

  public static String pluginName;
  public static String pluginVersion;
  public static Server server;
  public static Prettifier plugin;
  private HashSet<Material> protectedBlocks;

  public void onDisable(){
    this.saveConfig();

    log("Disabled");
  }

  public void onEnable(){
    pluginName=this.getDescription().getName();
    pluginVersion=this.getDescription().getVersion();
    server=this.getServer();
    plugin=this;
    protectedBlocks=new HashSet<Material>();

    getServer().getPluginManager().registerEvents(this, this);
    getProtectedBlocks();

    log("Enabled.");
  }

  private void getProtectedBlocks(){
    log("Loading protection...");
    if(!this.getConfig().contains("prettifier")) {
      log("Bad config file. Replacing.");
      this.saveDefaultConfig();
    }
    for(Iterator<?> blocks=this.getConfig().getList("prettifier").iterator(); blocks.hasNext();){
      String block =(String) blocks.next();
      try{
        this.protectedBlocks.add(Material.getMaterial(block));
      } catch(Exception e){
        log("Failed to load block: "+block);
      }
    }
  }

  public void log(Object in){
    System.out.println("[" + pluginName + "] " + String.valueOf(in));
  }

  // Actual Code:
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onTotemBreak(EntityExplodeEvent event) {
    for(Iterator<Block> blocks=event.blockList().iterator(); blocks.hasNext();){
      Block block = blocks.next();
      if(this.protectedBlocks.contains(block.getType())) {
        blocks.remove();
      }
    }
  }


}