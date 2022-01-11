package fr.redline.invinteract.inv;

import fr.redline.invinteract.inv.container.Container;
import fr.redline.invinteract.inv.holder.InventoryInfoHolder;
import fr.redline.invinteract.inv.page.Page;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public abstract class InventoryCreator {

    String name;
    int line;

    HashMap<String, Container> containerHashMap = new HashMap<>();

    private InventoryInfoHolder inventory = null;

    public InventoryCreator(String name, int line) {
        this.name = name;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public void setGlobalInventory(boolean value, Page page) {

        if (value && inventory != null)
            return;

        if (value) {
            inventory = createInventory(null);
            inventory.openPage(page);
        } else inventory = null;

        for (Container container : containerHashMap.values()) {
            List<InventoryInfoHolder> inventoryInfoHolderList = container.getInvOnContainer();
            while (!inventoryInfoHolderList.isEmpty()) {
                InventoryInfoHolder inventoryInfoHolder = inventoryInfoHolderList.get(0);
                for (HumanEntity onInv : inventoryInfoHolder.getInventory().getViewers()) {
                    if (!(onInv instanceof Player))
                        continue;
                    onInv.closeInventory();
                    onInv.openInventory(createInventory(null).openPage(page).getInventory());
                }
                inventoryInfoHolderList = container.getInvOnContainer();
            }
        }
    }

    public InventoryInfoHolder getGlobalInventory() {
        return inventory;
    }

    public int getLine() {
        return line;
    }

    public Optional<Container> getContainer(String containerName) {
        return Optional.ofNullable(containerHashMap.get(containerName));
    }

    public Optional<Container> createContainer(String containerName) {
        if (getContainer(containerName).isPresent())
            return Optional.empty();
        return Optional.of(addContainer(new Container(this, containerName)));
    }

    private Container addContainer(Container container) {
        containerHashMap.put(container.getContainerName(), container);
        return container;
    }

    public void removeContainer(Container container) {

        for (InventoryInfoHolder inventoryInfoHolder : container.getInvOnContainer()) {
            inventoryInfoHolder.closeInventory();
            inventoryInfoHolder.openPage(null);
        }

        containerHashMap.remove(container.getContainerName());

    }


    public boolean isGlobalInventory() {
        return inventory != null;
    }

    public InventoryInfoHolder createInventory(Player playerRelated) {
        if (isGlobalInventory())
            return inventory;
        return new InventoryInfoHolder(this, playerRelated);
    }

    public void closeAllInventory() {
        for (Container container : containerHashMap.values()) {
            for (InventoryInfoHolder inventoryInfoHolder : container.getInvOnContainer()) {
                inventoryInfoHolder.closeInventory();
            }
        }
    }

    public void closeInventory(Player player) {
        InventoryView invView = player.getOpenInventory();
        if (invView == null)
            return;

        Inventory topInventory = invView.getTopInventory();
        if (topInventory == null)
            return;

        if (!(topInventory.getHolder() instanceof InventoryInfoHolder))
            return;

        if (((InventoryInfoHolder) topInventory.getHolder()).getInventoryCreator() == this)
            player.closeInventory();
    }

    public abstract void onClickInventory(InventoryInfoHolder container, InventoryClickEvent inventoryClickEvent);

    public abstract void onCloseInventory(Player container, InventoryInfoHolder inventoryClickEvent);

    public abstract void onClickUnderInventory(InventoryInfoHolder inventoryInfoHolder, InventoryClickEvent inventoryClickEvent);

    public abstract void onDrag(InventoryDragEvent inventoryDragEvent);

}