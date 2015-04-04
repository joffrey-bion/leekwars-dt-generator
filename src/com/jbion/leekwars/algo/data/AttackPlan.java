package com.jbion.leekwars.algo.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;

import com.jbion.leekwars.model.Item;
import com.jbion.leekwars.model.Weapon;

/**
 * Represents a list of items to be used in one single turn. Items may appear multiple times if they
 * need to be used multiple times in the turn.
 */
public class AttackPlan extends ArrayList<Item> {

    public static final AttackPlan EMPTY = new AttackPlan();

    public AttackPlan() {}

    public AttackPlan(AttackPlan p) {
        super(p);
    }

    public static Collector<Item, AttackPlan, AttackPlan> getCollector() {
        return Collector.of(AttackPlan::new, AttackPlan::add, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        });
    }

    /**
     * Adds the specified item to this AttackPlan. If the item has a cooldown and has already been
     * used in the plan, this method does nothing and returns false.
     *
     * @param item
     *            the item to add
     * @return true if the item was actually added, false if it didn't fit in
     */
    @Override
    public boolean add(Item item) {
        if (item.getCooldown() > 0 && contains(item)) {
            // the item has a cooldown, we can't use it twice in this attack plan
            return false;
        }
        return super.add(item);
    }

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
     * Returns the minimum range necessary to use all items in this plan.
     *
     * @return the minimum range necessary to use all items in this plan.
     */
    public int getMinRange() {
        return stream().mapToInt(Item::getMinRange).max().orElse(Integer.MAX_VALUE);
    }

    /**
     * Returns the maximum range necessary to use all items in this plan.
     *
     * @return the maximum range necessary to use all items in this plan.
     */
    public int getMaxRange() {
        return stream().mapToInt(Item::getMaxRange).min().orElse(0);
    }

    public int getCount(Item item) {
        return (int) stream().filter(i -> i.equals(item)).count();
    }

    public void reorderWeaponFirst(Weapon weapon) {
        int count = getCount(weapon);
        while (remove(weapon))
            ;
        for (int i = 0; i < count; i++) {
            add(0, weapon);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AttackPlan)) {
            return false;
        }
        AttackPlan other = (AttackPlan) o;
        if (other.size() != this.size()) {
            return false;
        }
        return stream().allMatch(i -> getCount(i) == other.getCount(i));
    }

    @Override
    public int hashCode() {
        // very tolerant, but it does not matter
        return stream().mapToInt(i -> i.hashCode()).sum();
    }

    public String asCode(boolean withSpaces) {
        final String SP = withSpaces ? " " : "";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Item item : this) {
            sb.append(item.getId());
            sb.append("," + SP);
        }
        if (!isEmpty()) {
            sb.delete(sb.lastIndexOf(","), sb.length());
        }
        return sb.append("]").toString();
    }
}
