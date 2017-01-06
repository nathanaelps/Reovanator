package com.thespuff.plugins.superpower.power;

import org.bukkit.entity.Player;

/**
 * Created by nsmith on 2017-01-03.
 * thespuff.com
 */
public class Durability extends Power{

  public Durability(Player player){
    super(player);

    setName("durability");
    setDisplayName("Health");
    setMinimum(1);
    setMaximum(100);
    setBase(20);
    log(getName()+" loaded");
  }

  @Override public void list(){
    getPlayer().sendMessage(getDisplayName()+": level "+get()+" ("+getPlayer().getHealthScale()+")");
  }

  @Override public boolean apply(){
    try{
      getPlayer().setHealthScale(getLevel());
    } catch (Exception e){
      return false;
    }
    return true;
  }
  @Override public void examine(){
    setLevel(((Double) getPlayer().getHealthScale()).intValue());
  }

}
