package fr.redline.invinteract.event;

import org.bukkit.plugin.java.JavaPlugin;

public class EventInventory extends JavaPlugin {

    public static void enableEvent(JavaPlugin javaPlugin) {
        new ClickInventory(javaPlugin);
        new CloseInventory(javaPlugin);
    }

}