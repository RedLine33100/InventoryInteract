package fr.redline.invinteract.item;

import fr.redline.invinteract.inv.holder.InventoryInfoHolder;
import fr.redline.invinteract.inv.page.Page;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PageOpenerItem extends Item {

    final Page page;
    final ItemStack itemStack;

    public PageOpenerItem(Page page, ItemStack itemStack) {
        this.page = page;
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getItemStack(InventoryInfoHolder inventoryInfoHolder) {
        return itemStack;
    }

    @Override
    public void onItemClicked(InventoryInfoHolder inventoryInfoHolder, InventoryClickEvent inventoryClickEvent) {
        inventoryInfoHolder.openPage(page);
    }
}
