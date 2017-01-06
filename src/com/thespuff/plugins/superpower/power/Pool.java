package com.thespuff.plugins.superpower.power;

import org.bukkit.entity.Player;

/**
 * Created by nsmith on 2017-01-03.
 */
public class Pool extends Power{

  public Pool(Player player){
    super(player);

    setName("pool");
    setDisplayName("Pool");
    setBase(0);
    log(getName()+" loaded");
  }

}
