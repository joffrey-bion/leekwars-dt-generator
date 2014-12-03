package com.jbion.leekwars.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jbion.leekwars.algo.data.AttackPlan;
import com.jbion.leekwars.algo.data.AttackPlansSet;
import com.jbion.leekwars.algo.data.DecisionTree;
import com.jbion.leekwars.algo.data.TPMap;
import com.jbion.leekwars.algo.data.WeaponMap;
import com.jbion.leekwars.model.Chip;
import com.jbion.leekwars.model.Item;
import com.jbion.leekwars.model.Weapon;

public class Optimizer {

    private List<Weapon> weapons;
    private List<Item> items;
    private Map<Integer, AttackPlansSet> allPlansSets;

    public Optimizer(List<Weapon> weapons, List<Chip> chips) {
        this.weapons = weapons;
        this.items = new ArrayList<>();
        items.addAll(weapons);
        items.addAll(chips);
        allPlansSets = new HashMap<>();
    }

    public DecisionTree buildDecisionTree(int maxTP) {
        initializePlansSets(maxTP);
        WeaponMap weaponMap = sortByWeaponEquipped(maxTP);
        DecisionTree decisionTree = new DecisionTree(items);
        for (int code = 1; code < Math.pow(2, items.size()); code++) {
            WeaponMap copy = new WeaponMap(weaponMap);
            copy.limitPlansTo(decisionTree.getUsableItems(code));
            copy.normalize();
            decisionTree.put(code, copy);
        }
        return decisionTree;
    }

    /**
     * Enumerates and stores all {@link AttackPlansSet}s for the given maximum of TP.
     * 
     * @param maxTP
     *            the maximum turn points available
     */
    private void initializePlansSets(int maxTP) {
        allPlansSets.clear();
        for (int tp = 1; tp <= maxTP; tp++) {
            allPlansSets.put(tp, getPlansSetForTP(tp));
        }
    }

    /**
     * Returns the set of {@link AttackPlan}s costing exactly tp (ignoring weapon change).
     * 
     * @param tp
     *            the number of turn points the {@link AttackPlan}s should cost
     * @return a new (or cached) set of {@link AttackPlan}s, guaranteed to cost tp (ignoring weapon
     *         change).
     */
    private AttackPlansSet getPlansSetForTP(int tp) {
        if (allPlansSets.get(tp) != null) {
            return allPlansSets.get(tp);
        }
        AttackPlansSet plansSet = new AttackPlansSet();
        for (Item item : items) {
            if (tp < item.getTPCost()) {
                continue;
            }
            if (tp == item.getTPCost()) {
                AttackPlan plan = new AttackPlan();
                if (plan.add(item)) {
                    plansSet.add(plan);
                }
                continue;
            }
            AttackPlansSet subPlansSet = getPlansSetForTP(tp - item.getTPCost());
            for (AttackPlan subPlan : subPlansSet) {
                AttackPlan plan = new AttackPlan();
                plan.addAll(subPlan);
                if (plan.add(item)) {
                    plansSet.add(plan);
                }
            }
        }
        return plansSet;
    }

    private WeaponMap sortByWeaponEquipped(int maxTP) {
        WeaponMap weaponMap = new WeaponMap(weapons);
        for (Weapon equippedWeapon : weapons) {
            TPMap tpMap = new TPMap(maxTP);
            allPlansSets.values().stream().flatMap(Set::stream).forEach(plan -> {
                int tp = plan.getTpCost(equippedWeapon);
                if (tp <= maxTP) {
                    tpMap.addPlan(tp, new AttackPlan(plan));
                }
            });
            tpMap.reorderWeaponFirst(equippedWeapon);
            weaponMap.put(equippedWeapon, tpMap);
        }
        return weaponMap;
    }
}
