package com.jbion.leekwars.model;

public abstract class Item {

    private final int id;
    private final String name;
    private final int tpCost;
    private final int minDamage;
    private final int maxDamage;
    private final int minRange;
    private final int maxRange;
    private final boolean inLine;
    private final int failRate;
    private final int aoeRange;
    
    protected Item(int id, String name, int tpCost, int minDamage, int maxDamage, int minRange, int maxRange, boolean inLine,
            int failRate, int aoeRange) {
        super();
        this.id = id;
        this.name = name;
        this.tpCost = tpCost;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.inLine = inLine;
        this.failRate = failRate;
        this.aoeRange = aoeRange;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTPCost() {
        return tpCost;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public int getMinRange() {
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public boolean isInLine() {
        return inLine;
    }

    public int getFailRate() {
        return failRate;
    }

    public int getAoeRange() {
        return aoeRange;
    }
    
    public boolean isWeapon() {
        return false;
    }
    
    public boolean isChip() {
        return false;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Item)) {
            return false;
        }
        return ((Item) o).id == this.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
