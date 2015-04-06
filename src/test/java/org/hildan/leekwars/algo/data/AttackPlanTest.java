package org.hildan.leekwars.algo.data;

import static org.junit.Assert.*;

import org.hildan.leekwars.algo.data.AttackPlan;
import org.hildan.leekwars.model.Chip;
import org.hildan.leekwars.model.Weapon;
import org.junit.Test;

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
    public void testReorder1() {
        AttackPlan plan = new AttackPlan();
        plan.add(Weapon.DOUBLE_GUN);
        plan.add(Chip.SPARK);
        plan.add(Weapon.DOUBLE_GUN);
        plan.add(Weapon.DOUBLE_GUN);
        plan.add(Chip.SPARK);
        plan.add(Chip.ICE);
        AttackPlan plan2 = new AttackPlan();
        plan2.add(Weapon.DOUBLE_GUN);
        plan2.add(Weapon.DOUBLE_GUN);
        plan2.add(Weapon.DOUBLE_GUN);
        plan2.add(Chip.SPARK);
        plan2.add(Chip.SPARK);
        plan2.add(Chip.ICE);
        plan.reorderWeaponFirst(Weapon.DOUBLE_GUN);
        for (int i = 0; i < plan.size(); i++) {
            assertEquals(plan2.get(i), plan.get(i));
        }
    }

    @Test
    public void testReorder2() {
        AttackPlan plan = new AttackPlan();
        plan.add(Chip.SPARK);
        plan.add(Weapon.DOUBLE_GUN);
        plan.add(Chip.SPARK);
        plan.add(Chip.ICE);
        plan.add(Weapon.DOUBLE_GUN);
        plan.add(Weapon.DOUBLE_GUN);
        System.out.println("plan = " + plan);
        AttackPlan plan2 = new AttackPlan();
        plan2.add(Weapon.DOUBLE_GUN);
        plan2.add(Weapon.DOUBLE_GUN);
        plan2.add(Weapon.DOUBLE_GUN);
        plan2.add(Chip.SPARK);
        plan2.add(Chip.SPARK);
        plan2.add(Chip.ICE);
        plan.reorderWeaponFirst(Weapon.DOUBLE_GUN);
        System.out.println("r.plan = " + plan);
        for (int i = 0; i < plan.size(); i++) {
            assertEquals(plan2.get(i), plan.get(i));
        }
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
