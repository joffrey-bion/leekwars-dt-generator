package org.hildan.leekwars.algo;

import org.hildan.leekwars.algo.data.DecisionTree;
import org.hildan.leekwars.model.Item;

public class Generator {

    private DecisionTree tree;

    public Generator(DecisionTree tree) {
        super();
        this.tree = tree;
    }

    /**
     * Prints the Leekscript code using this generator's decision tree.
     */
    public void printLeekscript() {
        System.out.println("//==============================");
        System.out.println("// GENERATED FILE - DO NOT EDIT");
        System.out.println("//==============================");
        System.out.println();
        printAvailableItemsGetter();
        System.out.println();
        printAttackPlanGetter();
        System.out.println();
        printItemsToCodeFunction();
        System.out.println();
        generateCodeToItems();
        System.out.println();
        System.out.println("global tree = " + tree.asCode("") + ";");
    }

    private static void printAttackPlanGetter() {
        System.out.println("function getAttackPlan(maxTP, usableItems) {");
        System.out.println("\tvar usableItemsCode = itemsToCode(usableItems, true);");
        System.out.println("\treturn tree[usableItemsCode][getWeapon()][maxTP];\n}");
    }

    private static void printAvailableItemsGetter() {
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

    private void printItemsToCodeFunction() {
        System.out.println("function itemsToCode(items, displayErrors) {");
        System.out.println("\tvar code = 0;");
        System.out.println("\tfor (var item in items) {");
        System.out.print("\t\t");
        for (Item item : tree.getItems()) {
            System.out.println("if (item == " + item + ") {");
            System.out.println("\t\t\tcode += " + tree.getMask(item) + ";");
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
            System.out.println("\tif (code & " + tree.getMask(item) + ") {");
            System.out.println("\t\tpush(items, " + item + ");");
            System.out.println("\t}");
        }
        System.out.println("\treturn items;\n}");
    }
}
