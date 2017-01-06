package com.thespuff.plugins.superpower.power;

import org.bukkit.entity.Player;

/**
 * Created by nsmith on 2017-01-03.
 */
public class Mining extends Power{

  public Mining(Player player){
    super(player);

    setName("mining");
    setDisplayName("Mining");
    setMinimum(0);
    setBase(0);
    log(getName()+" loaded");
  }
}
