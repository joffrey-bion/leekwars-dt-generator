package com.jbion.leekwars.algo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;

import com.jbion.leekwars.model.Item;
import com.jbion.leekwars.model.Weapon;

public class ItemList extends ArrayList<Item> {

    private static final Collector<Item, ItemList, ItemList> COLLECTOR = Collector.of(ItemList::new, ItemList::add, (list1, list2) -> {
        list1.addAll(list2);
        return list1;
    });

    public boolean containsInLine() {
        return stream().anyMatch(Item::isInLine);
    }

    private int otherWeaponsCount(Weapon weapon) {
        Set<Weapon> otherWeapons = new HashSet<>();
        stream().filter(i -> i.isWeapon() && !i.equals(weapon)).forEach(i -> {
            otherWeapons.add((Weapon) i);
        });
        return otherWeapons.size();
    }

    public int getTpCost(Weapon equippedWeapon) {
        return stream().mapToInt(Item::getTPCost).sum() + otherWeaponsCount(equippedWeapon);
    }
    
    public int getAverageDamage() {
        return stream().mapToInt(i -> (i.getMinDamage() + i.getMaxDamage()) / 2).sum();
    }

    /**
     * Returns the minimum range necessary to use all items in this set.
     * 
     * @return the minimum range necessary to use all items in this set.
     */
    public int getMinRange() {
        return stream().mapToInt(Item::getMinRange).max().orElse(Integer.MAX_VALUE);
    }

    /**
     * Returns the maximum range necessary to use all items in this set.
     * 
     * @return the maximum range necessary to use all items in this set.
     */
    public int getMaxRange() {
        return stream().mapToInt(Item::getMaxRange).min().orElse(0);
    }

    public int getCount(Item item) {
        return (int) stream().filter(i -> i.equals(item)).count();
    }
    
    public static Collector<Item, ItemList, ItemList> getCollector() {
        return COLLECTOR;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ItemList)) {
            return false;
        }
        ItemList other = (ItemList) o;
        if (other.size() != this.size()) {
            return false;
        }
        return stream().allMatch(i -> getCount(i) == other.getCount(i));
    }
}
