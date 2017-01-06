package com.thespuff.plugins.superpower.power;

import com.thespuff.plugins.superpower.Superpower;
import org.bukkit.entity.Player;

/**
 * Created by nsmith on 2017-01-03.
 */
public abstract class Power{
  private String name = "powerconfigname";
  private String displayName = "Power Display Name";
  private int minimum = -1000;
  private int maximum = 1000;
  private int base = 0;
  private int level = 0;
  private Player player;

  public Power(Player player){
    this.player = player;
  }
  public void log(Object in){ Superpower.plugin.log(in); }
  public String getName(){return name;}

  public void reset(){
    set(base);
  }
  public boolean canSet(Integer level){
    return (level >= minimum && level <= maximum);
  }
  public void list(){
    player.sendMessage(displayName+": level "+get());
  }

  public Integer get(){
    return level;
  }
  public boolean set(Integer level){
    if(canSet(level)){
      this.level = level;
      return true;
    } else{
      return false;
    }
  }
  public boolean add(Integer levels){
    return set(get() + levels);
  }
  public boolean apply(){
    return true;
  }
  public void examine() {}
  public void load(){
    Double levels;
    try{
      String levelstring=Superpower.plugin.getConfig().getString("superpower.players." + player.getName() + "." + name);
      levels=Double.parseDouble(levelstring);
    }catch(NullPointerException npe){
      levels =Double.valueOf(base);
      log("Player "+player.getName()+": Failed to load "+name+". Defaulting to "+base);
      save();
    }
    this.level = levels.intValue();
  }
  public void save(){
    log("superpower.players." + player.getName() +"."+name+" = "+level);
    Superpower.plugin.getConfig().set("superpower.players." + player.getName() +"."+name, level);
  }

  public Player getPlayer(){
    return player;
  }
  public void setDisplayName(String displayName){
    this.displayName=displayName;
  }
  public void setName(String name){
    this.name=name;
  }
  public void setBase(int base){
    this.base=base;
  }
  public void setMaximum(int maximum){
    this.maximum=maximum;
  }
  public void setMinimum(int minimum){
    this.minimum=minimum;
  }
  public String getDisplayName(){
    return this.displayName;
  }
  public int getLevel(){
    return this.level;
  }
  public void setLevel(int level){
    this.level=level;
  }



}
