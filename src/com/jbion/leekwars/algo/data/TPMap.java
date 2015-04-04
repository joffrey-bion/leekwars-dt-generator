package com.jbion.leekwars.algo.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.jbion.leekwars.model.Item;
import com.jbion.leekwars.model.Weapon;

/**
 * A map between TP amounts and attack plans.
 * <p>
 * During the creation phase (before normalization), a TP amount may be associated to 0, 1, or more
 * plans, thus {@link #getPlan(int)} may not be called (it would throw an
 * {@link IllegalStateException}).
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
        this.normalizedMap = new HashMap<>();
    }

    public TPMap(TPMap source) {
        this.maxTP = source.maxTP;
        this.planSets = new HashMap<>(maxTP);
        this.normalizedMap = new HashMap<>();
        for (int tp : source.planSets.keySet()) {
            planSets.put(tp, new AttackPlansSet(source.planSets.get(tp)));
            if (source.isNormalized()) {
                normalizedMap.put(tp, new AttackPlan(source.normalizedMap.get(tp)));
            }
        }
    }

    public void addPlan(int tp, AttackPlan plan) {
        AttackPlansSet set = planSets.get(tp);
        if (set == null) {
            set = new AttackPlansSet();
            planSets.put(tp, set);
        }
        set.add(plan);
    }

    public void limitPlansTo(Collection<Item> items) {
        planSets.values().stream().forEach(ps -> ps.limitPlansTo(items));
    }

    public void normalize() {
        normalizedMap.clear();
        int maxDamage = 0;
        AttackPlan currentMaxDamagePlan = AttackPlan.EMPTY;
        for (int tp : planSets.keySet()) {
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

    public void reorderWeaponFirst(Weapon weapon) {
        for (AttackPlansSet set : planSets.values()) {
            set.stream().forEach(p -> p.reorderWeaponFirst(weapon));
        }
        normalizedMap.values().stream().forEach(p -> p.reorderWeaponFirst(weapon));
    }

    public String asCode(String indent) {
        final String NL = indent == null ? "" : "\n";
        final String SP = indent == null ? "" : " ";
        StringBuilder sb = new StringBuilder();
        for (int tp : (isNormalized() ? normalizedMap.keySet() : planSets.keySet())) {
            if (indent != null) {
                sb.append(indent);
            }
            sb.append(indent == null ? tp : String.format("%2s", tp)).append(SP + ":" + SP);
            sb.append(isNormalized() ? normalizedMap.get(tp).asCode(indent != null) : planSets.get(tp));
            sb.append("," + NL);
        }
        return sb.deleteCharAt(sb.lastIndexOf(",")).toString();
    }

    public String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        for (int tp : (isNormalized() ? normalizedMap.keySet() : planSets.keySet())) {
            sb.append(indent);
            sb.append(String.format("%2s", tp)).append(" TP: ");
            sb.append(isNormalized() ? normalizedMap.get(tp) : planSets.get(tp));
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString("");
    }
}
