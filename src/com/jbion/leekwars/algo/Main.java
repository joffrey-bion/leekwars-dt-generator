package com.jbion.leekwars.algo;

import java.util.ArrayList;
import java.util.List;

import com.jbion.leekwars.algo.data.DecisionTree;
import com.jbion.leekwars.model.Chip;
import com.jbion.leekwars.model.Item;
import com.jbion.leekwars.model.Weapon;

public class Main {

    public static void main(String[] args) {

        int maxTP = 11;

        List<Weapon> weapons = new ArrayList<>();
        weapons.add(Weapon.DOUBLE_GUN);
        weapons.add(Weapon.LASER);
        weapons.add(Weapon.SHOTGUN);

        List<Chip> chips = new ArrayList<>();
        chips.add(Chip.SPARK);
        //chips.add(Chip.ICE);

        Optimizer opt = new Optimizer(weapons, chips);
        DecisionTree decisionTree = opt.buildDecisionTree(maxTP);

        System.out.println("//==============================");
        System.out.println("// GENERATED FILE - DO NOT EDIT");
        System.out.println("//==============================");
        System.out.println();
        System.out.println(getFunctionCode(decisionTree));
        System.out.println();
        System.out.println("global tree = " + decisionTree.asCode("") + ";");
    }

    private static String getFunctionCode(DecisionTree tree) {
        String str = "function getAttackPlan(maxTP, usableItems, equippedWeapon) {\n";
        str += "\tvar usableItemsCode = 0;\n";
        str += "\tfor (var item in usableItems) {\n";
        str += "\t\t";
        for (Item item : tree.getItems()) {
            str += getIfPart("\t\t", item, tree);
        }
        str += "{\n\t\t\tdebugE(\"unpredicted item provided '\" + item + \"', regeneration of the decision tree needed\");\n";
        str += "\t\t}\n\t}\n\treturn tree[usableItemsCode][equippedWeapon][maxTP];\n}";
        return str;
    }

    private static String getIfPart(String indent, Item item, DecisionTree tree) {
        String result = "";
        result += "if (item == " + item + ") {\n";
        result += indent + "\tusableItemsCode += " + tree.getCode(item) + ";\n";
        result += indent + "} else ";
        return result;
    }
}
