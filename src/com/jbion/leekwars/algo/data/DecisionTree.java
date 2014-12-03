package com.jbion.leekwars.algo.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.jbion.leekwars.model.Item;

public class DecisionTree extends HashMap<Integer, WeaponMap>{
    
    private List<Item> allItemsSorted;
    
    public DecisionTree(List<Item> allItems) {
        allItemsSorted = allItems.stream().sorted(Comparator.comparingInt(Item::getId)).collect(Collectors.toList());
    }
    
    public List<Item> getItems() {
        return allItemsSorted;
    }

    public List<Item> getUsableItems(int code) {
        List<Item> usable = new ArrayList<>();
        int position = 0;
        while (code > 0 && position < allItemsSorted.size()) {
            if (code % 2 == 1) {
                usable.add(allItemsSorted.get(position));
            }
            code /= 2;
            position++;
        }
        return usable;
    }
    
    public int getCode(Item item) {
        return (int) Math.pow(2, allItemsSorted.indexOf(item));
    }

    public String asCode() {
        return asCode("");
    }

    public String asCode(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("[\n");
        for (Integer code : keySet()) {
            sb.append(indent).append("\t").append(code).append(" : [\n");
            sb.append(get(code).asCode(indent + "\t\t"));
            sb.append(indent).append("\t],\n");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(indent).append("]");
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
            sb.append("Code ").append(code).append(" : usable=").append(getUsableItems(code));
            sb.append(" {\n");
            sb.append(get(code).toString(prefix + "   "));
            sb.append("}\n");
        }
        return sb.toString();
    }
}
