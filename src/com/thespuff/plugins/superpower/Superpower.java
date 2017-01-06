package com.thespuff.plugins.superpower;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by nsmith on 2016-12-29.
 */
public class Superpower extends JavaPlugin implements Listener{

  public static String pluginName;
  public static String pluginVersion;
  public static Server server;
  public static Superpower plugin;
  private Map<String, SuperpowerPlayer> players;

  public void onDisable(){
    this.saveConfig();

    log("Disabled");
  }

  public void onEnable(){
    pluginName=this.getDescription().getName();
    pluginVersion=this.getDescription().getVersion();
    server=this.getServer();
    plugin=this;
    players = new HashMap();

    getServer().getPluginManager().registerEvents(this, this);
    setMetadata();

    log("Enabled.");
  }

  public void log(Object in){
    System.out.println("[" + pluginName + "] " + String.valueOf(in));
  }

  private void setMetadata(){
    try{
      Set<String> shrines=getConfig().getConfigurationSection("superpower.shrines").getKeys(false);

      for(String shrineName : shrines){
        getBlock(shrineName).setMetadata("Superpower", new FixedMetadataValue(this, shrineName));
      }
    }catch(NullPointerException npe){
      log("No shrines found.");
    }catch(Exception e){
      log("Problem loading.");
    }
  }

  private Block getBlock(String shrineName){
    World world = server.getWorld((String) getConfig().get("superpower.shrines."+shrineName+".world"));
    Integer x =(Integer) getConfig().get("superpower.shrines."+shrineName+".x");
    Integer y =(Integer) getConfig().get("superpower.shrines."+shrineName+".y");
    Integer z =(Integer) getConfig().get("superpower.shrines."+shrineName+".z");
    return world.getBlockAt(x,y,z);
  }

  // ----------------------------- Actual Code:

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerLogin(PlayerLoginEvent event) {
    String playerName=event.getPlayer().getName();
    players.put(event.getPlayer().getName(),new SuperpowerPlayer(event.getPlayer()));
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void shrineBreak(BlockBreakEvent event){
    if(event.getBlock().hasMetadata("Superpower")){
      if(!event.getPlayer().isOp()){
        event.setCancelled(true);
        return;
      }
      String shrineName=event.getBlock().getMetadata("Superpower").get(0).asString();
      log("Shrine "+shrineName+" broken.");
      event.getBlock().removeMetadata("Superpower", this);
    }
  }

//  @EventHandler(priority = EventPriority.HIGH)
//  public void harvestBlock(BlockBreakEvent event){
//    if(event.isCancelled()) return;
//    Block block = event.getBlock();
//    Location location = block.getLocation();
//    World world = location.getWorld();
//    switch(event.getBlock().getType()){
//      case DIRT: world.dropItemNaturally(location, new ItemStack(Material.DIRT,2));
//    }
//  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void shrineBuild(PlayerInteractEvent event){
    if(!event.getPlayer().isOp()) return;
    if(!event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.GOLD_SPADE)) return;
    if(!event.hasBlock()) return;
    if(event.getClickedBlock().hasMetadata("Superpower")) return;

    if(!(event.getClickedBlock().getState() instanceof Sign)) return;
    Sign sign = (Sign) event.getClickedBlock().getState();
    if(!sign.getLine(0).equalsIgnoreCase("superpower")) return;

    String shrineName = (new SimpleDateFormat("yyMMddHHmmss").format(new Date()));
    event.getClickedBlock().setMetadata("Superpower", new FixedMetadataValue(this, shrineName));

    getConfig().set("superpower.shrines."+shrineName+".world", event.getClickedBlock().getWorld().getName());
    getConfig().set("superpower.shrines."+shrineName+".x", event.getClickedBlock().getX());
    getConfig().set("superpower.shrines."+shrineName+".y", event.getClickedBlock().getY());
    getConfig().set("superpower.shrines."+shrineName+".z", event.getClickedBlock().getZ());
    saveConfig();

    event.getPlayer().sendMessage("Superpower shrine created.");
  }
  @EventHandler(priority = EventPriority.HIGHEST)
  public void shrineClick(PlayerInteractEvent event){
    //TODO: Pay-for-use. Need to apply a cost somehow.
    if(!event.hasBlock()) return;
    if(!event.getClickedBlock().hasMetadata("Superpower")) return;

    if(!(event.getClickedBlock().getState() instanceof Sign)) return;
    Sign sign = (Sign) event.getClickedBlock().getState();
    SuperpowerPlayer player=players.get(event.getPlayer().getName());

    for(String line:sign.getLines()){
      try{
        String[] segments=line.split("\\s+");
        switch(segments.length){
          case 1:
            switch(segments[0]){
              case "reset":
                player.resetAll();
                break;
              case "display":
                player.list();
                break;
            }
          case 2:
            String ability=segments[0];
            if(segments[1].equalsIgnoreCase("reset")){
              player.reset(ability);
            }else{
              Double levels=Double.parseDouble(segments[1]);
              player.add(ability,levels.intValue());
            }
          default:
            //do nothing. Not a usable line.
        }
      } catch(Exception e){
        //do nothing.
      }
    }
  }

  public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] args)
  {
    if (paramCommand.getName().equalsIgnoreCase("superpowers")){
      if(!(paramCommandSender instanceof Player)) return false;
      Player player = (Player) paramCommandSender;

      players.get(player.getName()).list();

      log("Config reloaded.");
      return true;
    }
//    if (paramCommand.getName().equalsIgnoreCase("tpad"))
//    {
//      if(!(paramCommandSender instanceof Player)) return false;
//      Player player = (Player) paramCommandSender;
//      if(!player.isOp()) return false;
//
//      if(args.length<1) {
//        return false;
//      }
//
//      telepadPlayer(player, args[0]);
//
//      player.sendMessage("TelePad to: "+args[0]);
//      return true;
//    }
    return false;
  }

}