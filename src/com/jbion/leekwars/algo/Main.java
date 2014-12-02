package com.jbion.leekwars.algo;

import java.util.ArrayList;
import java.util.List;

import com.jbion.leekwars.model.Chip;
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
        
        Optimizer opt = new Optimizer();
        opt.optimize(maxTP, weapons, chips);
        
    }

}
