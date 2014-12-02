package com.jbion.leekwars.algo.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.jbion.leekwars.model.Item;

public class AttackPlansSet extends HashSet<AttackPlan> {

    public AttackPlansSet() {}

    public AttackPlansSet(AttackPlansSet source) {
        super(source);
    }

    public void limitPlansTo(Collection<Item> items) {
        List<AttackPlan> toRemove = stream().filter(p -> p.stream().anyMatch(i -> !items.contains(i))).collect(
                Collectors.toList());
        removeAll(toRemove);
    }

    public int getMaxAverageDamage() {
        return stream().mapToInt(AttackPlan::getAverageDamage).max().orElse(0);
    }
}
