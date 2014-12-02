package com.jbion.leekwars.algo.data;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jbion.leekwars.model.Chip;
import com.jbion.leekwars.model.Weapon;

public class AttackPlanTest {

    @Test
    public void testCount() {
        AttackPlan plan = new AttackPlan();
        plan.add(Weapon.DOUBLE_GUN);
        plan.add(Weapon.DOUBLE_GUN);
        plan.add(Chip.SPARK);
        plan.add(Weapon.DOUBLE_GUN);
        plan.add(Chip.SPARK);
        plan.add(Chip.ICE);
        assertEquals(3, plan.getCount(Weapon.DOUBLE_GUN));
        assertEquals(2, plan.getCount(Chip.SPARK));
        assertEquals(1, plan.getCount(Chip.ICE));
    }

    @Test
    public void testEquals() {
        AttackPlan plan = new AttackPlan();
        plan.add(Weapon.DOUBLE_GUN);
        plan.add(Weapon.DOUBLE_GUN);
        plan.add(Chip.SPARK);
        plan.add(Weapon.DOUBLE_GUN);
        plan.add(Chip.SPARK);
        plan.add(Chip.ICE);
        AttackPlan plan2 = new AttackPlan();
        plan2.add(Weapon.DOUBLE_GUN);
        plan2.add(Weapon.DOUBLE_GUN);
        plan2.add(Chip.SPARK);
        plan2.add(Weapon.DOUBLE_GUN);
        plan2.add(Chip.SPARK);
        plan2.add(Chip.ICE);
        AttackPlan plan3 = new AttackPlan();
        plan3.add(Chip.SPARK);
        plan3.add(Weapon.DOUBLE_GUN);
        plan3.add(Chip.SPARK);
        plan3.add(Weapon.DOUBLE_GUN);
        plan3.add(Chip.ICE);
        plan3.add(Weapon.DOUBLE_GUN);
        assertEquals(true, plan.equals(plan2));
        assertEquals(true, plan.equals(plan3));
        assertEquals(true, plan2.equals(plan3));
    }

}
