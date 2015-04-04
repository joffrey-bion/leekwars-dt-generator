package com.jbion.leekwars.algo.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.jbion.leekwars.model.Item;

public class DecisionTree extends HashMap<Integer, WeaponMap> {

    private List<Item> allItemsSorted;

    public DecisionTree(List<Item> allItems) {
        allItemsSorted = allItems.stream().sorted(Comparator.comparingInt(Item::getId)).collect(Collectors.toList());
    }

    public List<Item> getItems() {
        return allItemsSorted;
    }

    /**
     * Gets the sublist of items corresponding to the given binary mask, with respect to the
     * internal list of items of this tree.
     * <p>
     * The position of each bit in the mask corresponds to the position of each item in the list
     * (sorted by item id). The least significant bit corresponds to the first item in the list (the
     * one with the smallest ID).
     * <p>
     * Assume the list of items sorted by ID is [Flame, Spark, Pistol, Shotgun]. The items codes
     * with respect to this list are then:
     *
     * <pre>
     * getItems(1) = getItems(0b0001) = [Flame]
     * getItems(4) = getItems(0b0100) = [Pistol]
     * getItems(5) = getItems(0b0101) = [Flame, Pistol]
     * getItems(6) = getItems(0b0110) = [Spark, Pistol]
     * </pre>
     *
     * @param mask
     *            a binary mask where each bit set to 1 means the corresponding item should be
     *            returned in the output list
     * @return the list of items selected by the mask. The input mask is the sum of the masks (as
     *         returned by {@link #getMask(Item)}) of each item in the returned list.
     * @see #getMask(Item)
     */
    public List<Item> getItems(int mask) {
        List<Item> usable = new ArrayList<>();
        int position = 0;
        while (mask > 0 && position < allItemsSorted.size()) {
            if (mask % 2 == 1) {
                usable.add(allItemsSorted.get(position));
            }
            mask /= 2;
            position++;
        }
        return usable;
    }

    /**
     * Gets the given item's mask with respect to the list of items of this tree.
     * <p>
     * The binary mask of an item has a single bit set to 1. The position of that bit is the
     * position of the item in the list when the list is sorted by item id. The least significant
     * bit corresponds to the first item in the list (the one with the smallest ID).
     * <p>
     * Assume the list of items sorted by ID is [Flame, Spark, Pistol, Shotgun]. The items' masks
     * with respect to this list are then:
     *
     * <pre>
     * getMask(Flame)   = 0b0001 = 1
     * getMask(Spark)   = 0b0010 = 2
     * getMask(Pistol)  = 0b0100 = 4
     * getMask(Shotgun) = 0b1000 = 8
     * </pre>
     *
     * @param item
     *            the item to get the code for
     * @return the int value of the code of the given item
     * @see #getItems(int)
     */
    public int getMask(Item item) {
        return (int) Math.pow(2, allItemsSorted.indexOf(item));
    }

    public String asCode(String indent) {
        final String NL = indent == null ? "" : "\n";
        final String SP = indent == null ? "" : " ";
        final String subIndent = indent == null ? null : indent + "\t";
        final String subSubIndent = subIndent == null ? null : subIndent + "\t";
        StringBuilder sb = new StringBuilder();
        if (indent != null) {
            sb.append(indent);
        }
        sb.append("[" + NL);
        for (Integer code : keySet()) {
            if (subIndent != null) {
                sb.append(subIndent);
            }
            sb.append(code).append(SP + ":" + SP + "[" + NL);
            sb.append(get(code).asCode(subSubIndent));
            if (subIndent != null) {
                sb.append(subIndent);
            }
            sb.append("]," + NL);
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        if (indent != null) {
            sb.append(indent);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String prefix) {
        StringBuilder sb = new StringBuilder();
        for (Integer code : keySet()) {
            sb.append(prefix);
            sb.append("Code ").append(code).append(" : usable=").append(getItems(code));
            sb.append(" {\n");
            sb.append(get(code).toString(prefix + "   "));
            sb.append("}\n");
        }
        return sb.toString();
    }
}
