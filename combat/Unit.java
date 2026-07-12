package com.dominic0r.limbusripoff;
import java.util.*;

public class Unit{
        int maxHP, hp, morale, speed, staggerTresh;
        String name, description;
        
        List<appliedEffect> effectsOnUnit = new ArrayList<>();
        List<mutation> pendingMutations = new ArrayList<>();
        
        
        
        List<Move> moveSet = new ArrayList<>();
        
        boolean isStaggered = false;
        
        
        Move unopposed;
        public Unit(int hp, int morale, int speed, int staggerTresh, String name, String description, List<Move> moveSet){
            this.hp = hp;
            this.maxHP = hp;
            this.morale = morale;
            this.speed = speed;
            this.name = name;
            this.description = description;
            this.moveSet = moveSet;
            this.staggerTresh = staggerTresh;
            unopposed = new Move("...",0,"...");
            unopposed.addCoin(new Coin(0,"..."));
        }
        
        public int getHP(){ return hp;}
        public int getMorale(){return morale;}
        public int getSpeed(){return speed;}
        public int getStaggerTresh(){ return staggerTresh;}
        public String getName(){return name;}
        public String getDesc(){return description;}
        public List<Move> getMoveSet(){ return moveSet;}
        public List<appliedEffect> getEffectList(){ return effectsOnUnit;}
        public boolean staggered(){ return isStaggered;}
        
        public Move unop(){return unopposed;}
        
        public void checkStagger(){
            if(!isStaggered){
                if(hp < staggerTresh){
                    isStaggered = true;
                    System.out.println("!!! " + name + " is STAGGERED !!!"); // Added UI
                }
            }else{
                isStaggered = false;
                System.out.println(name + " has recovered from stagger."); // Added UI
            }
        }
        
        public void setStaggerOn(){ isStaggered = true;}
        
        
        
        public void queueMutation(mutation newMut){
            pendingMutations.add(newMut);
        }
        
        public void applyPendingMutations(){
            for(mutation mut: pendingMutations){
                switch(mut.getType()){
                    case ADD:
                        addNewMut(mut);
                        break;
                    case REMOVE:
                        removeMut(mut);
                        break;
                    case MOD_STACK:
                        modifyStack(mut);
                        break;
                    case MOD_POTENCY:
                        modifyPotency(mut);
                        break;
                }
            }
            pendingMutations.clear();
        }
        
        public void addNewMut(mutation mut){
            applyEffect(mut.getEffect(), mut.getAmount(),1,mut.getSource());
        }
        
        public void removeMut(mutation mut){
            appliedEffect toRemove = null;
            for(appliedEffect app : effectsOnUnit){
                if(app.stat() == mut.getEffect()){
                    toRemove = app;
                }
            }
            effectsOnUnit.remove(toRemove);
        }
        
        public void modifyStack(mutation mut){
            for(appliedEffect app : effectsOnUnit){
                if(app.stat() == mut.getEffect()){
                    app.changeStack(mut.getAmount());
                }
            }
        }
        
        public void modifyPotency(mutation mut){
            for(appliedEffect app : effectsOnUnit){
                if(app.stat() == mut.getEffect()){
                    app.changePotency(mut.getAmount());
                }
            }
        }
        
        public void applyEffect(statusEffect effect, int potency, int stack, Unit source){
            boolean isAlreadyApplied = false;
            for(appliedEffect AE : effectsOnUnit){
                if(AE.stat() == effect){
                    isAlreadyApplied = true;
                    AE.changeStack(stack);
                    AE.changePotency(potency);
                    
                }
            }
            if(!isAlreadyApplied){
            effectsOnUnit.add(new appliedEffect(effect, potency, stack, source));
            }
        }
        
        public void takeHPDamage(int dam){
            hp -= dam;
            // Added UI
            System.out.println("  > " + name + " took " + dam + " damage! (HP: " + hp + "/" + maxHP + ")");
        }
        public void takeMoraleDamage(int dam){
            morale -= dam;
            // Added UI
            System.out.println("  > " + name + " lost " + dam + " morale!");
        }
        
        public void modifyMorale(int changeBy){
            morale+=changeBy;
            System.out.println(name+ " +"+changeBy+ " morale" );
        }
        
        public void statLimiter(){
            if(morale < -45){
                morale = -45;
            }
            if(morale > 45){
                morale = 45;
            }
            
            if(hp > maxHP){
                hp = maxHP;
            }
        }
    }
