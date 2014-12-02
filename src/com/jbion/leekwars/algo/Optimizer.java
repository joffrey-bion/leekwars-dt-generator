package com.jbion.leekwars.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jbion.leekwars.model.Chip;
import com.jbion.leekwars.model.Item;
import com.jbion.leekwars.model.Weapon;

public class Optimizer {

    private List<Item> items;
    private Map<Integer, List<ItemList>> itemListsCache;

    private Map<Weapon, Map<Integer, List<ItemList>>> itemListsByTPByWeapon;

    public void optimize(int maxTP, List<Weapon> weapons, List<Chip> chips) {
        this.items = new ArrayList<>();
        items.addAll(weapons);
        items.addAll(chips);

        itemListsCache = new HashMap<>(maxTP);
        for (int tp = 1; tp <= maxTP; tp++) {
            itemListsCache.put(tp, getItemListsForTP(tp));
        }
        removeDuplicates(itemListsCache.values());

        for (int tp = 1; tp <= maxTP; tp++) {
            System.out.println(tp + " TP: " + itemListsCache.get(tp));
        }

        itemListsByTPByWeapon = new HashMap<>(weapons.size());
        for (Weapon equippedWeapon : weapons) {
            Map<Integer, List<ItemList>> itemListsByTP = new HashMap<>(maxTP);
            for (int tp = 1; tp <= maxTP; tp++) {
                itemListsByTP.put(tp, new ArrayList<>());
            }
            itemListsCache.values().stream().flatMap(List::stream).forEach(list -> {
                int tp = list.getTpCost(equippedWeapon);
                if (tp <= maxTP) {
                    itemListsByTP.get(tp).add(list);
                }
            });
            removeDuplicates(itemListsByTP.values());
            clearUninterestingSets(maxTP, itemListsByTP);
            itemListsByTPByWeapon.put(equippedWeapon, itemListsByTP);

            System.out.println("\nFor Equipped Weapon " + equippedWeapon);
            for (int tp = 1; tp <= maxTP; tp++) {
                System.out.println(tp + " TP: " + itemListsByTP.get(tp));
            }
        }
    }

    /**
     * Returns the list of all item lists costing exactly tp (ignoring weapon change).
     * 
     * @param tp
     *            the number of turn points the item lists should cost
     * @return a new (or cached) list of item lists, guaranteed to cost tp.
     */
    private List<ItemList> getItemListsForTP(int tp) {
        if (itemListsCache.get(tp) != null) {
            return itemListsCache.get(tp);
        }
        List<ItemList> itemLists = new ArrayList<>();
        for (Item item : items) {
            if (tp < item.getTPCost()) {
                continue;
            }
            if (tp == item.getTPCost()) {
                ItemList list = new ItemList();
                list.add(item);
                itemLists.add(list);
                continue;
            }
            List<ItemList> subLists = getItemListsForTP(tp - item.getTPCost());
            for (ItemList subList : subLists) {
                ItemList list = new ItemList();
                list.addAll(subList);
                list.add(item);
                itemLists.add(list.stream().sorted(Comparator.comparingInt(Item::getTPCost))
                        .collect(ItemList.getCollector()));
            }
        }
        return itemLists;
    }

    private static void removeDuplicates(Collection<List<ItemList>> collection) {
        Set<ItemList> temp = new HashSet<>();
        for (List<ItemList> itemListsList : collection) {
            temp.addAll(itemListsList);
            itemListsList.clear();
            for (ItemList list : temp) {
                if (!itemListsList.contains(list)) {
                    itemListsList.add(list);
                }
            }
            temp.clear();
        }
    }

    private static void clearUninterestingSets(int maxTP, Map<Integer, List<ItemList>> itemListsByTP) {
        int maxDamage = 0;
        for (int tp = 1; tp <= maxTP; tp++) {
            List<ItemList> itemListsList = itemListsByTP.get(tp);

            for (ItemList list : itemListsList) {
                maxDamage = Math.max(maxDamage, list.getAverageDamage());
            }
            final int finalMaxDamage = maxDamage;
            itemListsList.removeIf(list -> list.getAverageDamage() < finalMaxDamage);
        }
    }

    public ItemList getBestItems(boolean canGetAligned, int maxTP) {
        // TODO
        return null;
    }
}
