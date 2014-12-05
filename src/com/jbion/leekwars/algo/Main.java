package com.jbion.leekwars.algo;

import java.util.ArrayList;
import java.util.List;

import com.jbion.leekwars.algo.data.DecisionTree;
import com.jbion.leekwars.model.Chip;
import com.jbion.leekwars.model.Item;
import com.jbion.leekwars.model.Weapon;

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
        generateCode(decisionTree);
    }

    private static void generateCode(DecisionTree tree) {
        System.out.println("//==============================");
        System.out.println("// GENERATED FILE - DO NOT EDIT");
        System.out.println("//==============================");
        System.out.println();
        System.out.println("function getAttackPlan(maxTP, usableItems) {");
        System.out.println("\tvar usableItemsCode = itemsToCode(usableItems);");
        System.out.println("\treturn tree[usableItemsCode][getWeapon()][maxTP];\n}");
        System.out.println();
        generateEnumerateFunction(tree);
        System.out.println();
        System.out.println(getFunctionItemsToCode(tree));
        System.out.println();
        System.out.println(getFunctionCodeToItems(tree));
        System.out.println();
        System.out.println("global tree = " + tree.asCode("") + ";");
    }

    private static void generateEnumerateFunction(DecisionTree tree) {
        int max = (int) Math.pow(2, tree.getItems().size());
        System.out.println("function getAllItemCombinations() {");
        System.out.println("\tvar itemSets = [];");
        System.out.println("\tfor (var i = 0; i < " + max + "; i++) {");
        System.out.println("\t\tpush(itemSets, codeToItems(i));");
        System.out.println("\t}");
        System.out.println("\treverse(itemSets);");
        System.out.println("\treturn itemSets;\n}");
    }

    private static String getFunctionItemsToCode(DecisionTree tree) {
        String str = "function itemsToCode(items) {\n\tvar code = 0;\n\tfor (var item in items) {\n\t\t";
        for (Item item : tree.getItems()) {
            str += getIfPartItemToCode("\t\t", item, tree);
        }
        str += "{\n\t\t\tdebugE(\"unpredicted item provided '\" + item + \"', regeneration of the decision tree needed\");\n";
        str += "\t\t}\n\t}\n\treturn code;\n}";
        return str;
    }

    private static String getIfPartItemToCode(String indent, Item item, DecisionTree tree) {
        String result = "if (item == " + item + ") {\n";
        result += indent + "\tcode += " + tree.getCode(item) + ";\n";
        result += indent + "} else ";
        return result;
    }

    private static String getFunctionCodeToItems(DecisionTree tree) {
        String str = "function codeToItems(code) {\n\tvar items = [];\n";
        for (Item item : tree.getItems()) {
            str += getIfPartCodeToItem("\t", item, tree);
        }
        str += "\treturn items;\n}";
        return str;
    }

    private static String getIfPartCodeToItem(String indent, Item item, DecisionTree tree) {
        String result = indent + "if (code & " + tree.getCode(item) + ") {\n";
        result += indent + "\tpush(items, " + item + ");\n";
        result += indent + "}\n";
        return result;
    }
}
