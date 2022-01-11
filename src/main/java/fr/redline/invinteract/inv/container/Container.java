package fr.redline.invinteract.inv.container;

import fr.redline.invinteract.inv.InventoryCreator;
import fr.redline.invinteract.inv.holder.InventoryInfoHolder;
import fr.redline.invinteract.inv.page.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Container {

    String name;
    InventoryCreator inventoryCreator;

    List<Page> pageList = new ArrayList<>();
    List<InventoryInfoHolder> inventoryOnContainer = new ArrayList<>();

    Page header = null, footer = null;

    public Container(InventoryCreator inventoryCreator, String containerName) {
        this.inventoryCreator = inventoryCreator;
        this.name = containerName;
        createPage();
    }

    public InventoryCreator getInventoryCreator() {
        return inventoryCreator;
    }

    /**
     * Get the page Number
     *
     * @return the ContainerName
     */
    public String getContainerName() {
        return name;
    }

    public List<Page> getPageList() {
        return pageList;
    }

    public List<InventoryInfoHolder> getInvOnContainer() {
        return inventoryOnContainer;
    }

    public boolean setHeader(boolean value) {

        if (inventoryCreator.getLine() <= 1)
            return false;

        if (value)
            header = new Page(this, 0, 8);
        else header = null;

        int maxPosition = getPageMaxItemPosition();

        for (Page page : pageList) {

            page.setMaxPosition(maxPosition);

        }

        return true;

    }

    public Optional<Page> getHeader() {
        return Optional.ofNullable(header);
    }

    public boolean setFooter(boolean value) {
        if (inventoryCreator.getLine() <= 2)
            return false;

        if (value)
            footer = new Page(this, 0, 8);
        else footer = null;

        int maxPosition = getPageMaxItemPosition();

        for (Page page : pageList) {

            page.setMaxPosition(maxPosition);

        }

        return true;
    }

    public Optional<Page> getFooter() {
        return Optional.ofNullable(footer);
    }


    public Page createPage() {
        int newPageNumber = getHigherPageNumber() + 1;
        Page page = new Page(this, newPageNumber, getPageMaxItemPosition());
        pageList.add(page);
        return page;
    }

    public void removePage(Page toRemove) {

        if (pageList.size() == 1)
            return;

        pageList.remove(toRemove);

        for (Page page : pageList)
            if (page.getPageNumber() > toRemove.getPageNumber())
                page.setPageNumber(page.getPageNumber() - 1);

        Optional<Page> beforePage = getPage(toRemove.getPageNumber() - 1);

        for (InventoryInfoHolder inventoryInfoHolder : getInvOnContainer()) {
            Optional<Page> invPage = inventoryInfoHolder.getInventoryPage();
            if (!invPage.isPresent())
                continue;
            if (invPage.get() == toRemove) {
                if (beforePage.isPresent())
                    inventoryInfoHolder.openPage(beforePage.get());
                else inventoryInfoHolder.closeInventory();
            }
        }

    }

    public Optional<Page> getPage(int number) {
        for (Page page : getPageList())
            if (page.getPageNumber() == number)
                return Optional.of(page);
        return Optional.empty();
    }

    public int getHigherPageNumber() {
        return pageList.size();
    }

    public Optional<Page> getHigherPage() {
        return getPage(getHigherPageNumber());
    }

    public int getLowerPageNumber() {
        return 1;
    }

    public Optional<Page> getLowerPage() {
        return getPage(getLowerPageNumber());
    }


    public Optional<Page> getNextPage(Page page) {
        return getNextPage(page.getPageNumber());
    }

    public Optional<Page> getNextPage(int number) {
        return getPage(number + 1);
    }

    public Optional<Page> getPreviousPage(Page page) {
        return getNextPage(page.getPageNumber());
    }

    public Optional<Page> getPreviousPage(int number) {
        return getPage(number - 1);
    }


    public void updatePage(Page page) {
        applyInvUpdate(page, (inventory) -> inventory.openPage(page));
    }

    /**
     * @param page       the page that need to be updated
     * @param biConsumer the action to do to update the inventory, In case of Global Inventory, player is null
     */
    public void applyInvUpdate(Page page, Consumer<InventoryInfoHolder> biConsumer) {

        if (inventoryCreator.isGlobalInventory()) {
            biConsumer.accept((InventoryInfoHolder) inventoryCreator.getGlobalInventory().getInventory().getHolder());
            return;
        }

        for (InventoryInfoHolder inventoryInfoHolder : getInvOnContainer()) {
            Optional<Page> invPage = inventoryInfoHolder.getInventoryPage();
            invPage.ifPresent(pageC -> {
                if (pageC == page)
                    biConsumer.accept(inventoryInfoHolder);
            });
        }

    }

    public int getPageMaxItemPosition() {
        int removeLine = 0;
        if (header != null)
            removeLine += 1;
        if (footer != null)
            removeLine += 1;

        return (getInventoryCreator().getLine() - removeLine) * 9 - 1;
    }

}
