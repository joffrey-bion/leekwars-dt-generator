package org.hildan.leekwars.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.leekwars.algo.data.AttackPlan;
import org.hildan.leekwars.algo.data.AttackPlansSet;
import org.hildan.leekwars.algo.data.DecisionTree;
import org.hildan.leekwars.algo.data.TPMap;
import org.hildan.leekwars.algo.data.WeaponMap;
import org.hildan.leekwars.model.Chip;
import org.hildan.leekwars.model.Item;
import org.hildan.leekwars.model.Weapon;

public class Optimizer {

    private final int MIN_TP;

    private final List<Weapon> weapons;

    private final List<Item> items;

    private final Map<Integer, AttackPlansSet> allPlansSets;

    public Optimizer(List<Weapon> weapons, List<Chip> chips) {
        this.weapons = weapons;
        this.items = new ArrayList<>();
        items.addAll(weapons);
        items.addAll(chips);
        allPlansSets = new HashMap<>();
        // MIN_TP allows to prune the tree
        MIN_TP = items.stream().mapToInt(Item::getTPCost).min().orElse(0);
    }

    /**
     * Creates a {@link DecisionTree} based on the available weapons and chips, and the given
     * maximum TP points available.
     *
     * @param maxTP
     *            the max number of TP to handle in the tree
     * @return the built {@link DecisionTree}
     */
    public DecisionTree buildDecisionTree(int maxTP) {
        initializePlansSets(maxTP);
        WeaponMap weaponMap = sortByWeaponEquipped(maxTP);
        DecisionTree decisionTree = new DecisionTree(items);
        for (int code = 1; code < Math.pow(2, items.size()); code++) {
            WeaponMap copy = new WeaponMap(weaponMap);
            copy.limitPlansTo(decisionTree.getItems(code));
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
        for (int tp = MIN_TP; tp <= maxTP; tp++) {
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
        // use cache if possible
        if (allPlansSets.get(tp) != null) {
            return allPlansSets.get(tp);
        }
        // no plan set available yet, create one
        AttackPlansSet plansSet = new AttackPlansSet();
        for (Item item : items) {
            // ignore too expensive items
            if (tp < item.getTPCost()) {
                continue;
            }
            // single-item attack plan
            if (tp == item.getTPCost()) {
                AttackPlan plan = new AttackPlan();
                if (plan.add(item)) {
                    plansSet.add(plan);
                }
                continue;
            }
            // recursive call to keep going with other items but less TP
            AttackPlansSet subPlansSet = getPlansSetForTP(tp - item.getTPCost());
            for (AttackPlan subPlan : subPlansSet) {
                // copy the subplan to keep it consistent in the cache
                AttackPlan plan = new AttackPlan();
                plan.addAll(subPlan);
                if (plan.add(item)) {
                    // the plan set is valid only if the item is actually added (otherwise wrong TP count)
                    plansSet.add(plan);
                }
            }
        }
        return plansSet;
    }

    /**
     * Create the {@link WeaponMap} for each possible equipped weapon, based on the plan sets
     * initialized by a previous call to {@link #initializePlansSets(int)}.
     *
     * @param maxTP
     *            the maximum TP available
     * @return the created {@link WeaponMap}
     */
    private WeaponMap sortByWeaponEquipped(int maxTP) {
        WeaponMap weaponMap = new WeaponMap(weapons);
        for (Weapon equippedWeapon : weapons) {
            TPMap tpMap = new TPMap(maxTP);
            allPlansSets.values().stream().flatMap(Set::stream).forEach(plan -> {
                int tp = plan.getTpCost(equippedWeapon);
                // depending on the equipped weapon, some plans might cost too many TP
                    if (tp <= maxTP) {
                        // only use copies, because other TPMaps can reuse the attack plans
                        tpMap.addPlan(tp, new AttackPlan(plan));
                    }
                });
            tpMap.reorderWeaponFirst(equippedWeapon);
            weaponMap.put(equippedWeapon, tpMap);
        }
        return weaponMap;
    }
}
