package org.hildan.leekwars.model;

public class Weapon extends Item {
    
    public static final Weapon PISTOL = new Weapon(37, "WEAPON_PISTOL", 3, 15, 20, 1, 7, false, 5, 0);
    public static final Weapon MACHINE_GUN = new Weapon(38, "WEAPON_MACHINE_GUN", 4, 20, 24, 1, 6, true, 7, 0);
    public static final Weapon DOUBLE_GUN = new Weapon(39, "WEAPON_DOUBLE_GUN", 4, 23, 33, 2, 7, false, 6, 0);
    public static final Weapon SHOTGUN = new Weapon(41, "WEAPON_SHOTGUN", 5, 33, 43, 1, 5, true, 10, 0);
    public static final Weapon MAGNUM = new Weapon(45, "WEAPON_MAGNUM", 5, 25, 40, 1, 8, false, 4, 0);
    public static final Weapon LASER = new Weapon(42, "WEAPON_LASER", 6, 43, 59, 2, 7, true, 9, 0);
    public static final Weapon GRENADE_LAUNCHER = new Weapon(43, "WEAPON_GRENADE_LAUNCHER", 6, 45, 53, 4, 7, false, 7, 2);
    public static final Weapon FLAME_THROWER = new Weapon(46, "WEAPON_FLAME_THROWER", 7, 63, 68, 2, 7, true, 10, 0);
    public static final Weapon DESTROYER = new Weapon(40, "WEAPON_DESTROYER", 6, 40, 60, 1, 6, false, 6, 0);
    public static final Weapon GAZOR = new Weapon(48, "WEAPON_GAZOR", 7, 60, 80, 2, 7, true, 9, 3);
    public static final Weapon B_LASER = new Weapon(60, "WEAPON_B_LASER", 5, 50, 60, 2, 8, true, 3, 0);
    public static final Weapon ELECTRISOR = new Weapon(44, "WEAPON_ELECTRISOR", 6, 70, 90, 7, 7, false, 12, 1);
    public static final Weapon M_LASER = new Weapon(47, "WEAPON_M_LASER", 8, 90, 100, 2, 15, true, 8, 0);

    private Weapon(int id, String name, int tpCost, int minDamage, int maxDamage, int minRange, int maxRange,
            boolean inLine, int failRate, int aoeRange) {
        super(id, name, tpCost, minDamage, maxDamage, minRange, maxRange, inLine, failRate, aoeRange);
    }

    @Override
    public boolean isWeapon() {
        return true;
    }
}
