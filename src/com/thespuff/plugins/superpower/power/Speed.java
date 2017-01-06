package com.thespuff.plugins.superpower.power;

import org.bukkit.entity.Player;

/**
 * Created by nsmith on 2017-01-03.
 */
public class Speed extends Power{

  public Speed(Player player){
    super(player);

    setName("speed");
    setDisplayName("Speed");
    setMinimum(1);
    setMaximum(99);
    setBase(20);
    log(getName()+" loaded");
  }

  @Override public void list(){
    getPlayer().sendMessage(getDisplayName()+": level "+get()+" ("+getPlayer().getWalkSpeed()+")");
  }

  @Override public boolean apply(){
    try{
      getPlayer().setWalkSpeed(getLevel()/100f);
    } catch (Exception e){
      return false;
    }
    return true;
  }
  @Override public void examine(){
    Float speed=getPlayer().getWalkSpeed() * 100;
    setLevel(speed.intValue());
  }

}
