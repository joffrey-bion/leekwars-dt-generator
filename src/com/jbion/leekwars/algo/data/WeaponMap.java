package com.jbion.leekwars.algo.data;

import java.util.HashMap;
import java.util.List;

import com.jbion.leekwars.model.Item;
import com.jbion.leekwars.model.Weapon;

public class WeaponMap extends HashMap<Weapon, TPMap> {

    private final List<Weapon> weapons;

    public WeaponMap(List<Weapon> weapons) {
        this.weapons = weapons;
    }

    public WeaponMap(WeaponMap source) {
        this.weapons = source.weapons;
        for (Weapon weapon : weapons) {
            put(weapon, new TPMap(source.get(weapon)));
        }
    }

    public void limitPlansTo(List<Item> usableItems) {
        for (TPMap tpMap : values()) {
            tpMap.limitPlansTo(usableItems);
        }
    }

    public void normalize() {
        for (TPMap tpMap : values()) {
            tpMap.normalize();
        }
    }

    public String asCode() {
        return asCode("");
    }

    public String asCode(String indent) {
        StringBuilder sb = new StringBuilder();
        for (Weapon weapon : weapons) {
            sb.append(indent);
            sb.append(weapon).append(" : [\n");
            sb.append(get(weapon).asCode(indent + "\t"));
            sb.append(indent).append("],\n");
        }
        return sb.deleteCharAt(sb.lastIndexOf(",")).toString();
    }

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String prefix) {
        StringBuilder sb = new StringBuilder();
        for (Weapon weapon : weapons) {
            sb.append(prefix);
            sb.append(weapon.getName() + " equipped {\n");
            sb.append(get(weapon).toString(prefix + "   "));
            sb.append(prefix).append("}\n");
        }
        return sb.toString();
    }
}
