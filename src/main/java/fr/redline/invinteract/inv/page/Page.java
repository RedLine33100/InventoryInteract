package fr.redline.invinteract.inv.page;

import fr.redline.invinteract.inv.container.Container;
import fr.redline.invinteract.inv.holder.InventoryInfoHolder;
import fr.redline.invinteract.inv.holder.ItemHolder;

import java.util.Optional;
import java.util.function.Consumer;

public class Page extends ItemHolder {

    int pageNumber;
    Container container;
    Consumer<InventoryInfoHolder> openPageListener = null;

    public Page(Container container, int pageNumber, int maxPos) {
        super(maxPos);
        this.container = container;
        this.pageNumber = pageNumber;
    }

    public Container getContainer() {
        return container;
    }

    /**
     * Get the page Number
     *
     * @return the pageNumber
     */
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int number) {
        this.pageNumber = number;
    }

    public int getInInvEquivalent(int position) {
        int toAdd = 0;
        Optional<Page> header = container.getHeader();
        if (header.isPresent()) {
            if (header.get() == this)
                return position;
            toAdd += 9;
        }

        Optional<Page> footer = container.getFooter();
        if (footer.isPresent() && footer.get() == this) {
            toAdd += container.getPageMaxItemPosition();
        }
        return position + toAdd;
    }

    public Optional<Consumer<InventoryInfoHolder>> getOpenPageAction() {
        return Optional.ofNullable(this.openPageListener);
    }

    public void setOpenPageAction(Consumer<InventoryInfoHolder> onOpenPageAction) {
        this.openPageListener = onOpenPageAction;
    }
}
