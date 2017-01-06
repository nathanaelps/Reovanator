package com.thespuff.plugins.superpower;

import com.thespuff.plugins.superpower.power.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nsmith on 2017-01-04.
 */
public class SuperpowerPlayer{
  protected Player player;
  protected HashSet<Power> powers;

  public SuperpowerPlayer(Player player){
    powers=new HashSet<>();
    this.player = player;

    powers.add(new Mining(player));
    powers.add(new Speed(player));
    powers.add(new Durability(player));
    powers.add(new LungCapacity(player));
    powers.add(new Pool(player));

    Superpower.server.getScheduler().scheduleSyncDelayedTask(Superpower.plugin, () -> loadAll(), 5L);
  }

  public Power select(String name){
    for(Iterator<Power> i=powers.iterator(); i.hasNext(); ){
      Power power=i.next();
      if(power.getName().equals(name)) return power;
    }
    return new DebugPower(player);
  }

  public void log(Object in){ Superpower.plugin.log(in); }
  public void reset(String powerName){
    Power power=select(powerName);
    power.reset();
    power.apply();
    power.save();
  }
  public void set(String powerName, Integer level){
    Power power=select(powerName);
    power.set(level);
    power.apply();
    power.save();
  }
  public void add(String powerName, Integer levels){
    Power power=select(powerName);
    power.add(levels);
    power.apply();
    power.save();
  }
  public void apply(String powerName){
    Power power=select(powerName);
    power.apply();
    power.save();
  }
  public void list(){
    for(Iterator<Power> i=powers.iterator(); i.hasNext(); ){
      Power power=i.next();
      power.list();
    }
  }
  public void loadAll(){
    for(Iterator<Power> i=powers.iterator(); i.hasNext(); ){
      Power power=i.next();
      power.load();
      power.apply();
    }
  }
  public void resetAll(){
    for(Iterator<Power> i=powers.iterator(); i.hasNext(); ){
      Power power=i.next();
      power.reset();
      power.apply();
      power.save();
    }
  }

}
