// Not 100% accurate but I'll try!
package content;

import java.util.*;

import combat.*;
import effect.*;
import registry.*;


public class limbusPassives implements ContentPackage{
    @Override
    public String getPackageId(){
        return "limbus_passives";
    }

    @Override
  public void registerContent(){
    statusEffect bleed = new statusEffect("Bleed", false,99, 99, "Take fixed damage every coin toss");
        bleed.setOnClash((field, un)->{
            int damagetaken=0;
            for(appliedEffect app : un.getEffectList()){
                if(app.stat() == bleed){
                    damagetaken = app.getPotency();
                    un.takeHPDamage(damagetaken);
                    app.decrementStack();
                    System.out.println("  > " + un.getName() + " took " + damagetaken + " bleed damage!");
                }
            }
        })
          .setOnHitGive((field,un)->{
            int damagetaken=0;
            for(appliedEffect app : un.getEffectList()){
                if(app.stat() == bleed){
                    damagetaken = app.getPotency();
                    un.takeHPDamage(damagetaken);
                    app.decrementStack();
                    System.out.println("  > " + un.getName() + " took " + damagetaken + " bleed damage!"); 
                }
            }
          });
    Registry.registerStatus("lim:bleed", bleed);

    statusEffect rupture = new statusEffect("Rupture", false, 99,99, "Take fixed damage every hit");
        rupture.setOnHitReceived((field, un)->{
            int damagetaken=0;
            for(appliedEffect app : un.getEffectList()){
                if(app.stat() == rupture){
                    damagetaken = app.getPotency();
                    un.takeHPDamage(damagetaken);
                    app.decrementStack();
                    System.out.println("  > " + un.getName() + " took " + damagetaken + " rupture damage!"); 
                }
            }
        });
    Registry.registerStatus("lim:rupture", rupture);

    statusEffect burn = new statusEffect("Burn", false, 99,99, "Take fixed damage every end of turn");
        burn.setOnTurnEnd((field, un)->{
            int damagetaken=0;
            for(appliedEffect app : un.getEffectList()){
                if(app.stat() == burn){
                    damagetaken = app.getPotency();
                    un.takeHPDamage(damagetaken);
                    app.decrementStack();
                    System.out.println("  > " + un.getName() + " took " + damagetaken + " burn damage!"); 
                }
            }
        });
    Registry.registerStatus("lim:burn", burn);

    statusEffect sinking = new statusEffect("Sinking", false, 99,99, "Take fixed Morale damage every hit. If morale cannot be changed, inflict half of the damage on HP");
        sinking.setOnHitReceived((field, un)->{
            int damagetaken=0;
            for(appliedEffect app : un.getEffectList()){
                if(app.stat() == sinking){
                    damagetaken = app.getPotency();
                    if(un.canChangeMorale()){
                        un.takeMoraleDamage(damagetaken);
                    }else{
                        un.takeHPDamage(damagetaken/2);
                    }
                    
                    app.decrementStack();
                    System.out.println("  > " + un.getName() + " took " + damagetaken + " sinking damage!"); 
                }
            }
        });
    Registry.registerStatus("lim:sinking", sinking);

    statusEffect poise = new statusEffect("Poise", false, 99,99, "+5% chance of critical hit on target");
        poise.setOnTurnStart((field, un)->{
            int critChanceModify=0;
            for(appliedEffect app : un.getEffectList()){
                if(app.stat() == poise){
                    critChanceModify += app.getPotency()*5;
                    un.addCritChanceModifier(critChanceModify);
                    app.decrementStack();
                    System.out.println("  > " + un.getName() + "'s crit chance is increased by " + critChanceModify + "%!"); 
                }
            }
        });
    Registry.registerStatus("lim:poise", poise);

    statusEffect charge = new statusEffect("Charge", false, 99,99, "Can be used by skills");
        
    Registry.registerStatus("lim:charge", charge);

    statusEffect tremor = new statusEffect("Tremor", false, 99,99, "Increases stagger threshold upon tremor burst");
    tremor.setOnTurnEnd((field,un)->{
      for(appliedEffect app : un.getEffectList()){
                if(app.stat() == tremor){
                    app.decrementStack();
                }
            }
    });
    Registry.registerStatus("lim:tremor", tremor);

    statusEffect tremorBurst = new statusEffect("Tremor Burst", false, 1,1, "increases stagger treshold by the amount of tremor potency");
    tremorBurst.setOnTurnEnd((field,un)->{
      for(appliedEffect app : un.getEffectList()){
        if(app.stat()==tremorBurst){
          int tremorAmt = 0;
          for(appliedEffect findTrem: un.getEffectList()){
            if(findTrem.stat() == tremor){
              tremorAmt = findTrem.getPotency();
            }
          }
          un.modifyStaggerTresh(tremorAmt);
            app.decrementStack();
            System.out.println("  > " + un.getName() + " got hit with Tremor Burst!"); 
        }
      }
    });
      Registry.registerStatus("lim:tremorburst",tremorBurst);

      statusEffect haste = new statusEffect("Haste", false, 99,1, "Increases this unit's speed by the count for 1 turn");
        haste.setOnTurnStart((field, un)->{
            int speedModify=0;
            for(appliedEffect app : un.getEffectList()){
                if(app.stat() == haste){
                    speedModify += app.getPotency();
                    un.addSpeedModifier(speedModify);
                    app.decrementStack();
                    System.out.println("  > " + un.getName() + "'s speed is increased by " + speedModify + " for this turn!"); 
                }
            }
        });
      Registry.registerStatus("lim:haste",haste);

      statusEffect binding = new statusEffect("Binding", false, 99,1, "Decreases this unit's speed by the count for 1 turn");
        binding.setOnTurnStart((field, un)->{
            int speedModify=0;
            for(appliedEffect app : un.getEffectList()){
                if(app.stat() == binding){
                    speedModify += app.getPotency();
                    un.addSpeedModifier(speedModify*-1);
                    app.decrementStack();
                    System.out.println("  > " + un.getName() + "'s speed is decreased by " + speedModify + "!"); 
                }
            }
        });
      Registry.registerStatus("lim:binding",binding);

      statusEffect offenseLevelUp = new statusEffect("Offense Level Up", false, 99,1, "Increases this unit's offense level by the count for 1 turn");
        binding.setOnTurnStart((field, un)->{
            int atkModify=0;
            for(appliedEffect app : un.getEffectList()){
                if(app.stat() == offenseLevelUp){
                    atkModify += app.getPotency();
                    un.addAttackMod(atkModify);
                    app.decrementStack();
                    System.out.println("  > " + un.getName() + "'s attack is increased by " + atkModify + "!"); 
                }
            }
        });
      Registry.registerStatus("lim:offenseLevelUp",offenseLevelUp);

      statusEffect offenseLevelDown = new statusEffect("Offense Level Down", false, 99,1, "Decreases this unit's offense level by the count for 1 turn");
        binding.setOnTurnStart((field, un)->{
            int atkModify=0;
            for(appliedEffect app : un.getEffectList()){
                if(app.stat() == offenseLevelDown){
                    atkModify += app.getPotency();
                    un.addAttackMod(atkModify*-1);
                    app.decrementStack();
                    System.out.println("  > " + un.getName() + "'s attack is decreased by " + atkModify + "!"); 
                }
            }
        });
      Registry.registerStatus("lim:offenseLevelDown",offenseLevelDown);
      
    
  }

}
