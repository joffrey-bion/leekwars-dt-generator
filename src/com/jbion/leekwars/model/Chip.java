package com.jbion.leekwars.model;

public class Chip extends Item {
    
    public static final Chip SHOCK = new Chip(1, "CHIP_SHOCK", 2, 5, 7, 0, 6, false, 30, 0, 0);
    public static final Chip PEBBLE = new Chip(19, "CHIP_PEBBLE", 2, 2, 17, 0, 5, false, 9, 0, 1);
    public static final Chip SPARK = new Chip(18, "CHIP_SPARK", 3, 8, 16, 0, 10, false, 4, 0, 0);
    public static final Chip ICE = new Chip(2, "CHIP_ICE", 4, 17, 19, 0, 8, false, 5, 0, 0);
    
    public static final Chip ROCK = new Chip(7, "CHIP_ROCK", 5, 30, 31, 2, 6, false, 8, 0, 1);
    public static final Chip FLASH = new Chip(6, "CHIP_FLASH", 4, 19, 24, 1, 7, true, 1, 1, 1);
    public static final Chip FLAME = new Chip(5, "CHIP_FLAME", 4, 25, 27, 2, 7, false, 10, 0, 0);
    public static final Chip STALACTITE = new Chip(30, "CHIP_STALACTITE", 6, 64, 67, 2, 7, false, 20, 0, 3);
    
    public static final Chip LIGHTNING = new Chip(33, "CHIP_LIGHTNING", 4, 35, 47, 2, 5, true, 7, 2, 0);
    public static final Chip ROCKFALL = new Chip(32, "CHIP_ROCKFALL", 6, 48, 56, 5, 7, false, 8, 2, 1);
    public static final Chip ICEBERG = new Chip(31, "CHIP_ICEBERG", 7, 72, 80, 3, 5, true, 5, 2, 3);
    public static final Chip METEORITE = new Chip(36, "CHIP_METEORITE", 8, 70, 80, 4, 8, false, 4, 2, 3);
    
    private final int cooldown;

    private Chip(int id, String name, int tpCost, int minDamage, int maxDamage, int minRange, int maxRange, boolean inLine,
            int failRate, int aoeRange, int cooldown) {
        super(id, name, tpCost, minDamage, maxDamage, minRange, maxRange, inLine, failRate, aoeRange);
        this.cooldown = cooldown;
    }

    @Override
    public boolean isChip() {
        return true;
    }

    public int getCooldown() {
        return cooldown;
    }

}
