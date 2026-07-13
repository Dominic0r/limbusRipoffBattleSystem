package content;

import java.util.*;

import combat.*;
import effect.*;
import registry.*;

public class BaseContent implements ContentPackage{
    @Override
    public String getPackageId(){
        return "base";
    }
    
    @Override
    public void registerContent(){
        statusEffect bleed = Registry.getStatus("lim:bleed");
        if(bleed == null){
            System.out.println("Bleed not found!");
        }
        List<Move> playerMoveSet = new ArrayList<>();
        
        Move multiPunch = new Move("Multi-Punch", 5, "Punches the Enemy 3 times");
        multiPunch.addCoin(new Coin(2, "Punch!", rst ->{
            rst.getLoser().takeHPDamage(5, rst.getWinner(), damageType.BLUNT);
        }));
        
        multiPunch.addCoin(new Coin(1, "Punch Again!", rst ->{
            rst.getLoser().takeHPDamage(3,rst.getWinner(), damageType.BLUNT);
        }));
        
        multiPunch.addCoin(new Coin(3, "Upper Cut!", rst ->{
            rst.getLoser().takeHPDamage(7,rst.getWinner(),damageType.BLUNT);
        }));
        
        Move roundhouse = new Move("Roundhouse Kick", 3, "Kicks the enemy hard");
        roundhouse.addCoin(new Coin(10, "Kick!", rst ->{
            rst.getLoser().takeHPDamage(10,rst.getWinner(),damageType.PIERCE);
        }));
        
        Move stab = new Move("Stab", 4, "Stabs the enemy twice");
        stab.addCoin(new Coin(3, "Swish!", rst ->{
            rst.getLoser().takeHPDamage(4,rst.getWinner(),damageType.SLASH);
        }));
        
        stab.addCoin(new Coin(3, "Slash! - Inflicts 3 bleed potency", rst ->{
            rst.getLoser().takeHPDamage(4,rst.getWinner(),damageType.SLASH);
            if(!rst.getLoser().getEffectList().isEmpty()){
                for(appliedEffect app: rst.getLoser().getEffectList()){
                    if(app.stat()==bleed){
                        mutation mut = new mutation(Type.MOD_POTENCY, 3,bleed, rst.getWinner());
                        rst.getLoser().queueMutation(mut);
                        mutation mut = new mutation(Type.MOD_STACK, 1,bleed, rst.getWinner());
                        rst.getLoser().queueMutation(mut);
                    }else{
                        mutation mut = new mutation(Type.ADD, 3, bleed,rst.getWinner());
                        rst.getLoser().queueMutation(mut);
                    }
                }
            }else{
                mutation mut = new mutation(Type.ADD, 3, bleed,rst.getWinner());
                        rst.getLoser().queueMutation(mut);
            }
        }));

        Move nuke = new Move("Nuke", 10, "Nukes the Enemy. Requires at least one enemy to be on or less than 20% of their hp")
            .setCondition((field,user) -> {
                for(Unit un: field.getEnemies()){
                    if(un.getHP() <= un.getMaxHP()/5){
                        return true;
                    }
                }
                return false;
            });

        nuke.addCoin(new Coin(10, "NUKE!", rst->{
            rst.getLoser().takeHPDamage(100,rst.getWinner(),damageType.BLUNT);
        }));
        
        playerMoveSet.add(multiPunch);
        playerMoveSet.add(roundhouse);
        playerMoveSet.add(stab);
        playerMoveSet.add(nuke);
        //public Unit(int hp, int morale, int speed, int staggerTresh, int critChance, int baseAtk, int baseDef, float critmodifier, float slash, float pierce, float blunt, String name, String description, List<Move> moveSet){
        Unit playerUnit = new Unit(100, 0, 5, 30, 2, 10, 10,0.2f,0.5f,0.5f,1.0f, "Player", "Description", playerMoveSet);
        
        Registry.registerUnit("base:player", playerUnit);
        
        //Default Enemy
        
        List<Move> defEnemyMoveset = new ArrayList<>();
        Move punch = new Move("Punch", 3, "Two weak punches");
        
        punch.addCoin(new Coin(1, "punch-", rst ->{
            
            rst.getLoser().takeHPDamage(3,rst.getWinner(),damageType.BLUNT);
        }));
        punch.addCoin(new Coin(1, "punch again-", rst ->{
            rst.getLoser().takeHPDamage(3,rst.getWinner(),damageType.BLUNT);
        }));
        
        defEnemyMoveset.add(punch);
        
        Unit defEnemy = new Unit(50, 0, 2, 30, 2,8,8,0.2f,1.5f,1.0f,0.5f,  "Enemy", "Default Enemy", defEnemyMoveset);
        
        Registry.registerUnit("base:enemy", defEnemy);
        
        
        // Default Ally 
        List<Move> defAllyMoveSet = new ArrayList<>();
        
        defAllyMoveSet.add(punch);
        
        Unit defAlly = new Unit(50, 0, 2, 15, 2,8,8,0.2f,1.5f,1.0f,0.5f,  "Ally", "Default Ally", defAllyMoveSet);
        Registry.registerUnit("base:ally", defAlly);
    }
}
