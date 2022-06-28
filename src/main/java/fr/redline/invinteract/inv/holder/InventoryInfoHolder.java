package fr.redline.invinteract.inv.holder;

import fr.redline.invinteract.inv.InventoryCreator;
import fr.redline.invinteract.inv.container.Container;
import fr.redline.invinteract.inv.page.Page;
import fr.redline.invinteract.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryInfoHolder extends ItemHolder implements InventoryHolder {

    final InventoryCreator inventoryCreator;
    final Inventory inventory;
    Page page;
    Player playerRelated;

    public InventoryInfoHolder(InventoryCreator inventoryCreator, Player playerRelated) {
        super(inventoryCreator.getLine() * 9 - 1);
        this.inventoryCreator = inventoryCreator;
        this.playerRelated = playerRelated;
        this.inventory = Bukkit.createInventory(this, inventoryCreator.getLine() * 9, inventoryCreator.getName());
    }

    public InventoryCreator getInventoryCreator() {
        return inventoryCreator;
    }


    public Optional<Page> getInventoryPage() {
        return Optional.ofNullable(page);
    }

    public InventoryInfoHolder openPage(Page page) {

        if (this.page == page)
            return this;

        this.page = page;

        inventory.clear();
        itemListPos.clear();

        if (page == null)
            return this;

        Container container = page.getContainer();

        AtomicInteger toAdd = new AtomicInteger();

        Optional<Page> header = container.getHeader();
        header.ifPresent(headerPage -> {
            toAdd.addAndGet(9);
            headerPage.getItemMap().forEach((position, item) -> this.setItem(item, position));
        });

        page.getItemMap().forEach((position, item) -> this.setItem(item, position + toAdd.get()));

        toAdd.addAndGet(container.getPageMaxItemPosition() + 1);
        container.getFooter().ifPresent(value -> value.getItemMap().forEach((position, item) -> this.setItem(item, position + toAdd.get())));

        return this;
    }

    public Player getPlayerRelated() {
        return playerRelated;
    }

    public InventoryInfoHolder setPlayerRelated(Player playerRelated) {
        this.playerRelated = playerRelated;
        return this;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean setItem(Item item, int position) {
        if (super.setItem(item, position)) {
            getInventory().setItem(position, item.getItemStack(this));
            return true;
        }
        return false;
    }

    @Override
    public Optional<Item> removeItem(int position) {
        Optional<Item> itemOptional = super.removeItem(position);
        if (itemOptional.isPresent())
            getInventory().setItem(position, new ItemStack(Material.AIR));
        return itemOptional;
    }

    public InventoryInfoHolder closeInventory() {
        getInventory().getViewers().forEach(HumanEntity::closeInventory);
        return this;
    }

}
