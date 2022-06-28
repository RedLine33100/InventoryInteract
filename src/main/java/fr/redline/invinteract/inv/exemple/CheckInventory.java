package fr.redline.invinteract.inv.exemple;

import fr.redline.invinteract.inv.InventoryCreator;
import fr.redline.invinteract.inv.container.Container;
import fr.redline.invinteract.inv.holder.InventoryInfoHolder;
import fr.redline.invinteract.inv.page.Page;
import fr.redline.invinteract.item.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class CheckInventory extends InventoryCreator {

    Container container;
    Page page;
    final Consumer<Player> onAccept;
    final Consumer<Player> onRefuse;
    final Consumer<Player> onClosed;
    final List<Player> answered = new ArrayList<>();

    public CheckInventory(String name, ItemStack trueItem, Consumer<Player> onAccept, ItemStack falseItem, Consumer<Player> onRefuse, Consumer<Player> onClosed) {

        super(name, 3);

        this.onClosed = onClosed;
        this.onRefuse = onRefuse;
        this.onAccept = onAccept;

        Optional<Container> optContainer = this.createContainer("checker");
        if (!optContainer.isPresent())
            return;

        this.container = optContainer.get();
        this.page = this.container.createPage();

        this.page.setItem(new Item() {
            @Override
            public ItemStack getItemStack(InventoryInfoHolder inventoryInfoHolder) {
                return trueItem;
            }

            @Override
            public void onItemClicked(InventoryInfoHolder inventoryCreator, InventoryClickEvent inventoryClickEvent) {
                ((CheckInventory) inventoryCreator.getInventoryCreator()).onAccept.accept((Player) inventoryClickEvent.getWhoClicked());
                ((CheckInventory) inventoryCreator.getInventoryCreator()).answered.add((Player) inventoryClickEvent.getWhoClicked());
            }
        }, 12);

        this.page.setItem(new Item() {
            @Override
            public ItemStack getItemStack(InventoryInfoHolder inventoryInfoHolder) {
                return falseItem;
            }

            @Override
            public void onItemClicked(InventoryInfoHolder inventoryCreator, InventoryClickEvent inventoryClickEvent) {
                ((CheckInventory) inventoryCreator.getInventoryCreator()).onRefuse.accept((Player) inventoryClickEvent.getWhoClicked());
                ((CheckInventory) inventoryCreator.getInventoryCreator()).answered.add((Player) inventoryClickEvent.getWhoClicked());
            }
        }, 14);


        setGlobalInventory(true, page);

    }

    @Override
    public void onClickInventory(InventoryInfoHolder container, InventoryClickEvent inventoryClickEvent) {
        inventoryClickEvent.setCancelled(true);
    }

    @Override
    public void onCloseInventory(Player container, InventoryInfoHolder inventoryClickEvent) {
        if (answered.contains(container)) {
            answered.remove(container);
            return;
        }
        onClosed.accept(container);
    }

    @Override
    public void onClickUnderInventory(InventoryInfoHolder inventoryInfoHolder, InventoryClickEvent inventoryClickEvent) {
        inventoryClickEvent.setCancelled(true);
    }

    @Override
    public void onDrag(InventoryDragEvent inventoryDragEvent) {
        inventoryDragEvent.setCancelled(true);
    }
}
