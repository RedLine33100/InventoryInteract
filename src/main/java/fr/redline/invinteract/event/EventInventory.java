package fr.redline.invinteract.event;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class EventInventory extends JavaPlugin {

    @Override
    public void onEnable(){
        System.out.println("Start Inventory System");
        Bukkit.getLogger().log(Level.FINE, "Start Inventory System");
        enableEvent();
    }

    public void enableEvent() {
        new ClickInventory(this);
        new CloseInventory(this);
    }

}