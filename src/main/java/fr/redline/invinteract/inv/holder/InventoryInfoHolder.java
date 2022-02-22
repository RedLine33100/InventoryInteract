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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryInfoHolder extends ItemHolder implements InventoryHolder {

    final Map<String, Object> dataStore = new HashMap<>();
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

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public InventoryInfoHolder closeInventory() {
        getInventory().getViewers().forEach(HumanEntity::closeInventory);
        return this;
    }

    public Player getPlayerRelated() {
        return playerRelated;
    }

    public InventoryInfoHolder setPlayerRelated(Player playerRelated) {
        this.playerRelated = playerRelated;
        return this;
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

    public InventoryInfoHolder updatePage() {
        Optional<Page> optionalPage = getInventoryPage();
        if (optionalPage.isPresent())
            this.openPage(optionalPage.get());
        else this.openPage(null);
        return this;
    }


    public InventoryInfoHolder updateItem(int position) {
        Optional<Item> item = getItem(position);
        ItemStack itemStack = item.isPresent() ? item.get().getItemStack(this) : new ItemStack(Material.AIR);
        getInventory().setItem(position, itemStack);
        return this;
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


    public InventoryInfoHolder storeData(String key, Object object) {
        removeData(key);
        dataStore.put(key, object);
        return this;
    }

    public boolean hasData(String key) {
        return dataStore.containsKey(key);
    }

    public Optional<Object> getData(String key) {
        return Optional.ofNullable(dataStore.get(key));
    }

    public Object removeData(String key) {
        return Optional.ofNullable(dataStore.remove(key));
    }

    public InventoryInfoHolder clearData() {
        dataStore.clear();
        return this;
    }

}
