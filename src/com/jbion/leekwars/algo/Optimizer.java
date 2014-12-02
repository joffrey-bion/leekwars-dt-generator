package com.jbion.leekwars.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jbion.leekwars.algo.data.AttackPlan;
import com.jbion.leekwars.algo.data.AttackPlansSet;
import com.jbion.leekwars.algo.data.TPMap;
import com.jbion.leekwars.model.Chip;
import com.jbion.leekwars.model.Item;
import com.jbion.leekwars.model.Weapon;

public class Optimizer {

    private List<Weapon> weapons;
    private List<Item> items;
    private Map<Integer, AttackPlansSet> allPlansSets;

    private Map<Weapon, TPMap> plansSetsByTPByWeapon;

    public Optimizer(List<Weapon> weapons, List<Chip> chips) {
        this.weapons = weapons;
        this.items = new ArrayList<>();
        items.addAll(weapons);
        items.addAll(chips);
        allPlansSets = new HashMap<>();
        plansSetsByTPByWeapon = new HashMap<>(weapons.size());
    }

    public void optimize(int maxTP) {

        initializePlansSets(maxTP);

        System.out.println(allPlansSets);

        sortByWeaponEquipped(maxTP);

        for (Weapon equippedWeapon : weapons) {
            TPMap plansByTP = plansSetsByTPByWeapon.get(equippedWeapon);
            System.out.println("\nFor Equipped Weapon " + equippedWeapon);
            System.out.println(plansByTP);
            plansByTP.normalize();
            System.out.println("\n=> NORMALIZED");
            System.out.println(plansByTP);
        }
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

    private void sortByWeaponEquipped(int maxTP) {
        plansSetsByTPByWeapon.clear();
        for (Weapon equippedWeapon : weapons) {
            TPMap tpMap = new TPMap(maxTP);
            allPlansSets.values().stream().flatMap(Set::stream).forEach(plan -> {
                int tp = plan.getTpCost(equippedWeapon);
                if (tp <= maxTP) {
                    tpMap.addPlan(tp, plan);
                }
            });
            plansSetsByTPByWeapon.put(equippedWeapon, tpMap);
        }
    }

    public AttackPlan getBestItems(boolean canGetAligned, int maxTP) {
        // TODO
        return null;
    }
}