package org.hildan.leekwars.algo;

import java.util.ArrayList;
import java.util.List;

import org.hildan.leekwars.algo.data.DecisionTree;
import org.hildan.leekwars.model.Chip;
import org.hildan.leekwars.model.Weapon;

public class Main {

    public static void main(String[] args) {
        int maxTP = 13;

        List<Weapon> weapons = new ArrayList<>();
        weapons.add(Weapon.MAGNUM);
        weapons.add(Weapon.LASER);
        weapons.add(Weapon.GRENADE_LAUNCHER);

        List<Chip> chips = new ArrayList<>();
        chips.add(Chip.SPARK);

        Optimizer opt = new Optimizer(weapons, chips);
        DecisionTree decisionTree = opt.buildDecisionTree(maxTP);
        new Generator(decisionTree).printLeekscript();
    }
}
