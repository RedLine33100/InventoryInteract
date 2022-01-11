package fr.redline.invinteract.item;

import fr.redline.invinteract.inv.holder.InventoryInfoHolder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Item {

    /**
     * This is what gonna be in the inventory
     *
     * @return the ItemStack equivalent of the Item (Used on IndividualInv)
     */
    public abstract ItemStack getItemStack(InventoryInfoHolder inventoryInfoHolder);

    /**
     * Event called on item click
     *
     * @param inventoryInfoHolder The Holder of the clicked inventory
     * @param inventoryClickEvent the click event
     */
    public abstract void onItemClicked(InventoryInfoHolder inventoryInfoHolder, InventoryClickEvent inventoryClickEvent);

}
