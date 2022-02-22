package fr.redline.invinteract.event;

import fr.redline.invinteract.inv.holder.InventoryInfoHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;

public class ClickInventory implements Listener {

    public ClickInventory(JavaPlugin javaPlugin) {
        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent inventoryClickEvent) {

        if (!(inventoryClickEvent.getWhoClicked() instanceof Player))
            return;

        Inventory inventory = inventoryClickEvent.getClickedInventory();
        if (inventory == null)
            return;

        if (!(inventory.getHolder() instanceof InventoryInfoHolder)) {
            InventoryView viewInv = inventoryClickEvent.getWhoClicked().getOpenInventory();
            if (viewInv == null)
                return;
            Inventory topInv = viewInv.getTopInventory();
            if (topInv == null)
                return;
            if (topInv.getHolder() instanceof InventoryInfoHolder)
                ((InventoryInfoHolder) topInv.getHolder()).getInventoryCreator().onClickUnderInventory((InventoryInfoHolder) topInv.getHolder(), inventoryClickEvent);
            return;
        }

        InventoryInfoHolder inventoryInfoHolder = (InventoryInfoHolder) inventory.getHolder();

        inventoryInfoHolder.getInventoryCreator().onClickInventory(inventoryInfoHolder, inventoryClickEvent);

        inventoryInfoHolder.getItem(inventoryClickEvent.getSlot()).ifPresent(item1 -> item1.onItemClicked(inventoryInfoHolder, inventoryClickEvent));

    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof InventoryInfoHolder)
            ((InventoryInfoHolder) inventory.getHolder()).getInventoryCreator().onDrag(event);
    }

}
