package com.jbion.leekwars.algo.data;

import java.util.HashMap;
import java.util.Map;

import com.jbion.leekwars.model.Item;

/**
 * A map between TP amounts and attack plans.
 * <p>
 * During the creation phase (before normalization), a TP amount may be associated to 0, 1, or more
 * plans, thus {@link #getPlan(int)} may not be called (it would throw an {@link IllegalStateException}).
 * <p>
 * After calling {@link #normalize()}, each TP amount is associated with exatcly one plan, thus
 * {@link #getPlan(int)} may be called.
 */
public class TPMap {



    private final int maxTP;
    private final HashMap<Integer, AttackPlansSet> planSets;
    private final HashMap<Integer, AttackPlan> normalizedMap;

    public TPMap(int maxTP) {
        this.maxTP = maxTP;
        this.planSets = new HashMap<>(maxTP);
        for (int tp = 1; tp <= maxTP; tp++) {
            planSets.put(tp, new AttackPlansSet());
        }
        this.normalizedMap = new HashMap<>();
    }

    public TPMap(TPMap source) {
        this.maxTP = source.maxTP;
        this.planSets = new HashMap<>(maxTP);
        for (int tp = 1; tp <= maxTP; tp++) {
            planSets.put(tp, new AttackPlansSet(source.planSets.get(tp)));
        }
        this.normalizedMap = new HashMap<>(source.normalizedMap);
    }

    public void addPlan(int tp, AttackPlan plan) {
        planSets.get(tp).add(plan);
    }

    public void removePlansContainingItem(Item item) {
        planSets.values().stream().forEach(ps -> ps.removePlansContainingItem(item));
    }

    public void normalize() {
        normalizedMap.clear();
        int maxDamage = 0;
        AttackPlan currentMaxDamagePlan = AttackPlan.EMPTY;
        for (int tp = 1; tp <= maxTP; tp++) {
            AttackPlansSet plansSet = new AttackPlansSet(planSets.get(tp));

            for (AttackPlan plan : plansSet) {
                int damage = plan.getAverageDamage();
                if (damage > maxDamage) {
                    maxDamage = damage;
                    currentMaxDamagePlan = plan;
                }
            }
            final int finalMaxDamage = maxDamage;
            plansSet.removeIf(plan -> plan.getAverageDamage() < finalMaxDamage);
            normalizedMap.put(tp, plansSet.stream().findAny().orElse(currentMaxDamagePlan));
        }
    }
    
    public boolean isNormalized() {
        return normalizedMap.size() > 0;
    }
    
    public Map<Integer, AttackPlan> getNormalizedMap() {
        if (!isNormalized()) {
            throw new IllegalStateException("The TP Map should be normalized before calling getNormalizedMap()");
        }
        return normalizedMap;
    }

    public AttackPlansSet getPlansSet(int tp) {
        return planSets.get(tp);
    }

    public AttackPlan getPlan(int tp) {
        if (!isNormalized()) {
            throw new IllegalStateException("The TP Map should be normalized before calling getPlan(tp)");
        }
        AttackPlansSet plansSet = planSets.get(tp);
        return plansSet.stream().findAny().orElse(AttackPlan.EMPTY);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int tp = 1; tp <= maxTP; tp++) {
            sb.append(String.format("%2s", tp)).append(" TP: ");
            sb.append(isNormalized() ? normalizedMap.get(tp) : planSets.get(tp));
            sb.append("\n");
        }
        return sb.toString();
    }
}
