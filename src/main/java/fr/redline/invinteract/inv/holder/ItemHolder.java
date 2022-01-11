package fr.redline.invinteract.inv.holder;

import fr.redline.invinteract.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ItemHolder {

    protected HashMap<Integer, Item> itemListPos = new HashMap<>();
    int maxPos;

    public ItemHolder(int maxPos) {
        this.maxPos = maxPos;
    }

    /**
     * Get the Item and Inv Position
     *
     * @return Map with the item and the inventory position
     */
    public Map<Integer, Item> getItemMap() {
        return itemListPos;
    }

    public Optional<Integer> getFreePosition() {

        for (int i = 0; i <= maxPos; i++) {
            if (itemListPos.containsKey(i))
                continue;
            return Optional.of(i);
        }

        return Optional.empty();

    }

    public boolean hasFreeSpace() {
        return getFreePosition().isPresent();
    }

    public Optional<Integer> getPosition(Item id) {
        for (Map.Entry<Integer, Item> entry : itemListPos.entrySet()) {
            if (entry.getValue() == id)
                return Optional.of(entry.getKey());
        }
        return Optional.empty();
    }

    public Optional<Item> getItem(int pos) {
        return Optional.ofNullable(itemListPos.get(pos));
    }

    public Map<Integer, Item> getItemAfter(int position) {
        return new HashMap<Integer, Item>() {{
            itemListPos.forEach((value, item) -> {
                if (value > position)
                    put(value, item);
            });
        }};
    }

    public Map<Integer, Item> getItemBefore(int position) {
        return new HashMap<Integer, Item>() {{
            itemListPos.forEach((value, item) -> {
                if (value < position)
                    put(value, item);
            });
        }};
    }

    public boolean setItem(Item item, int position) {
        if (position > maxPos || position < 0)
            return false;

        if (itemListPos.containsKey(position))
            itemListPos.replace(position, item);
        else itemListPos.put(position, item);

        return true;
    }

    public Optional<Integer> addItem(Item item) {
        Optional<Integer> freePos = getFreePosition();
        if (!freePos.isPresent())
            return freePos;
        setItem(item, freePos.get());
        return freePos;
    }

    public boolean addItem(Item item, int position) {
        if (itemListPos.containsKey(position))
            return false;
        return setItem(item, position);
    }


    public Optional<Item> removeItem(int position) {
        return Optional.ofNullable(itemListPos.remove(position));
    }

    public Optional<Integer> removeItem(Item item) {
        Optional<Integer> position = getPosition(item);
        position.ifPresent(this::removeItem);
        return position;
    }

    public int getMaxPosition() {
        return maxPos;
    }

    public void setMaxPosition(int pos) {
        this.maxPos = pos;
        Map<Integer, Item> map = getItemAfter(pos);
        if (map.isEmpty())
            return;
        map.keySet().forEach(this::removeItem);
    }

}
