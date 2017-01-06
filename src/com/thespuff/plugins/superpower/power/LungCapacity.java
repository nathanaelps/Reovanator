package com.thespuff.plugins.superpower.power;

import org.bukkit.entity.Player;

/**
 * Created by nsmith on 2017-01-03.
 */
public class LungCapacity extends Power{

  public LungCapacity(Player player){
    super(player);

    setName("lungcapacity");
    setDisplayName("Lung Capacity");
    setMinimum(0);
    setMaximum(60);
    setBase(20);
    log(getName()+" loaded");
  }

  @Override public void list(){
    getPlayer().sendMessage(getDisplayName()+": level "+get()+" ("+getPlayer().getMaximumAir()+")");
  }

  @Override public boolean apply(){
    try{
      getPlayer().setMaximumAir(this.getLevel() * 15);
    }catch (Exception e){
      return false;
    }
    return true;
  }
  @Override public void examine(){
    setLevel(getPlayer().getMaximumAir() / 15);
  }
}
