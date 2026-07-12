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
        statusEffect bleed = new statusEffect("Bleed", false, 99, "Take fixed damage every coin toss");
        
        bleed.setOnClash((field, un)->{
            int damagetaken=0;
            for(appliedEffect app : un.getEffectList()){
                if(app.stat() == bleed){
                    damagetaken = app.getPotency();
                    un.takeHPDamage(damagetaken);
                    app.decrementStack();
                    System.out.println("  > " + un.getName() + " took " + damagetaken + " bleed damage!"); // Added UI
                }
            }
        });
        
        Registry.registerStatus("base:bleed", bleed);
        
        
        List<Move> playerMoveSet = new ArrayList<>();
        
        Move multiPunch = new Move("Multi-Punch", 5, "Punches the Enemy 3 times");
        multiPunch.addCoin(new Coin(2, "Punch!", rst ->{
            rst.getLoser().takeHPDamage(2);
        }));
        
        multiPunch.addCoin(new Coin(1, "Punch Again!", rst ->{
            rst.getLoser().takeHPDamage(1);
        }));
        
        multiPunch.addCoin(new Coin(3, "Upper Cut!", rst ->{
            rst.getLoser().takeHPDamage(3);
        }));
        
        Move roundhouse = new Move("Roundhouse Kick", 3, "Kicks the enemy hard");
        roundhouse.addCoin(new Coin(10, "Kick!", rst ->{
            rst.getLoser().takeHPDamage(10);
        }));
        
        Move stab = new Move("Stab", 4, "Stabs the enemy twice");
        stab.addCoin(new Coin(3, "Swish!", rst ->{
            rst.getLoser().takeHPDamage(3);
        }));
        
        stab.addCoin(new Coin(3, "Slash! - Inflicts 3 bleed potency", rst ->{
            rst.getLoser().takeHPDamage(3);
            if(!rst.getLoser().getEffectList().isEmpty()){
                for(appliedEffect app: rst.getLoser().getEffectList()){
                    if(app.stat()==bleed){
                        mutation mut = new mutation(Type.MOD_POTENCY, 3,bleed, rst.getWinner());
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
            rst.getLoser().takeHPamage(100);
        }));
        
        playerMoveSet.add(multiPunch);
        playerMoveSet.add(roundhouse);
        playerMoveSet.add(stab);
        playerMoveSet.add(nuke);
        
        Unit playerUnit = new Unit(100, 0, 5, 30, "Player", "Description", playerMoveSet);
        
        Registry.registerUnit("base:player", playerUnit);
        
        //Default Enemy
        
        List<Move> defEnemyMoveset = new ArrayList<>();
        Move punch = new Move("Punch", 1, "Two weak punches");
        
        punch.addCoin(new Coin(1, "punch-", rst ->{
            
            rst.getLoser().takeHPDamage(1);
        }));
        punch.addCoin(new Coin(1, "punch again-", rst ->{
            rst.getLoser().takeHPDamage(1);
        }));
        
        defEnemyMoveset.add(punch);
        
        Unit defEnemy = new Unit(50, 0, 2, 15, "Enemy", "Default Enemy", defEnemyMoveset);
        
        Registry.registerUnit("base:enemy", defEnemy);
        
        
        // Default Ally 
        List<Move> defAllyMoveSet = new ArrayList<>();
        
        defAllyMoveSet.add(punch);
        
        Unit defAlly = new Unit(50, 0, 2, 15, "Ally", "Default Ally", defAllyMoveSet);
        Registry.registerUnit("base:ally", defAlly);
    }
}
