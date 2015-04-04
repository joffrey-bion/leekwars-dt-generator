package com.jbion.leekwars.algo;

import com.jbion.leekwars.algo.data.DecisionTree;
import com.jbion.leekwars.model.Item;

public class Generator {

    private DecisionTree tree;

    public Generator(DecisionTree tree) {
        super();
        this.tree = tree;
    }

    public void generate() {
        System.out.println("//==============================");
        System.out.println("// GENERATED FILE - DO NOT EDIT");
        System.out.println("//==============================");
        System.out.println();
        generateAvailableItemsGetter();
        System.out.println();
        generateGetAttackPlan();
        System.out.println();
        generateItemsToCode();
        System.out.println();
        generateCodeToItems();
        System.out.println();
        System.out.println("global tree = " + tree.asCode("") + ";");
    }

    private static void generateGetAttackPlan() {
        System.out.println("function getAttackPlan(maxTP, usableItems) {");
        System.out.println("\tvar usableItemsCode = itemsToCode(usableItems, true);");
        System.out.println("\treturn tree[usableItemsCode][getWeapon()][maxTP];\n}");
    }

    private static void generateAvailableItemsGetter() {
        System.out.println("global AVAILABLE_ITEMS = null;");
        System.out.println();
        System.out.println("function getAvailableItems() {");
        System.out.println("\tif (AVAILABLE_ITEMS == null) {");
        System.out.println("\t\tvar equippedItems = getWeapons();");
        System.out.println("\t\tpushAll(equippedItems, getChips());");
        System.out.println("\t\tvar equippedItemsFilteredCode = itemsToCode(equippedItems, false);");
        System.out.println("\t\tAVAILABLE_ITEMS = codeToItems(equippedItemsFilteredCode);");
        System.out.println("\t}");
        System.out.println("\treturn AVAILABLE_ITEMS;");
        System.out.println("}");
    }

    private void generateItemsToCode() {
        System.out.println("function itemsToCode(items, displayErrors) {");
        System.out.println("\tvar code = 0;");
        System.out.println("\tfor (var item in items) {");
        System.out.print("\t\t");
        for (Item item : tree.getItems()) {
            System.out.println("if (item == " + item + ") {");
            System.out.println("\t\t\tcode += " + tree.getCode(item) + ";");
            System.out.print("\t\t} else ");
        }
        System.out.println("if (displayErrors) {");
        System.out
                .println("\t\t\tdebugE(\"unsupported item provided '\" + item + \"', regeneration of the decision tree needed\");");
        System.out.println("\t\t}");
        System.out.println("\t}");
        System.out.println("\treturn code;");
        System.out.println("}");
    }

    private void generateCodeToItems() {
        System.out.println("function codeToItems(code) {");
        System.out.println("\tvar items = [];");
        for (Item item : tree.getItems()) {
            System.out.println("\tif (code & " + tree.getCode(item) + ") {");
            System.out.println("\t\tpush(items, " + item + ");");
            System.out.println("\t}");
        }
        System.out.println("\treturn items;\n}");
    }
}
