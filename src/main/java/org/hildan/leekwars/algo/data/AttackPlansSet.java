package org.hildan.leekwars.algo.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.hildan.leekwars.model.Item;

public class AttackPlansSet extends HashSet<AttackPlan> {

    public AttackPlansSet() {}

    public AttackPlansSet(AttackPlansSet source) {
        source.stream().forEach(p -> add(new AttackPlan(p)));        
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
