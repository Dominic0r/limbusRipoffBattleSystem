package combat;
import java.util.*;
import java.lang.Math;
import effect.*;

public class Unit{
        public int maxHP, hp, morale, speed, staggerTresh, critChance;
        public String name, description;

        public float critmodifier = 0.02f; // damage modifier
        
        List<appliedEffect> effectsOnUnit = new ArrayList<>();
        List<mutation> pendingMutations = new ArrayList<>();

        Map<damageType,Float> damageAffect= new HashMap<>();
        Map<damageType,Float> damageAffectModifiers= new HashMap<>();
        
        
        
        List<Move> moveSet = new ArrayList<>();
        
        boolean isStaggered = false;
        boolean justGotStaggered = false;
        boolean alreadyStaggered = false;

        int speedMod = 0; // speed modifier

        int critChanceMod = 0;

        float critMod = 0; // modifies additional damage dealt by a critical hit

        int attackMod = 0, defendMod=0;

        int baseAtk,baseDef;

        int coinPowerMod = 0;

        int finalCoinPowerMod = 0; // modifies coin power after all coins have been flipped
        
        Move unopposed;
        Move staggered;
        public Unit(int hp, int morale, int speed, int staggerTresh, int critChance, int baseAtk, int baseDef, float critmodifier, float slash, float pierce, float blunt, String name, String description, List<Move> moveSet){
            this.hp = hp;
            this.maxHP = hp;
            this.morale = morale;
            this.speed = speed;
            this.name = name;
            this.description = description;
            this.moveSet = moveSet;
            this.staggerTresh = staggerTresh;
            unopposed = new Move("defenseless",0,"...");
            unopposed.addCoin(new Coin(0,"..."));
                staggered = new Move("defenseless",0,"...");
            this.critChance = critChance;
                this.critmodifier = critmodifier;
                this.baseAtk = baseAtk;
                this.baseDef = baseDef;

                damageAffect.put(damageType.SLASH, slash); // percentage guide: 1.5 = fatal, 1.0 = neutral, 0.5 = ineffective
                damageAffect.put(damageType.PIERCE, pierce);
                damageAffect.put(damageType.BLUNT, blunt);

                damageAffectModifiers.put(damageType.SLASH,0.0f);
                damageAffectModifiers.put(damageType.PIERCE,0.0f);
                damageAffectModifiers.put(damageType.BLUNT,0.0f);
                
        }

        
        public void setUnopposedMove(Move newMove){
            unopposed = newMove;
        }
        
        public int getHP(){ return hp;}
        public int getMaxHP(){ return maxHP;}
        public int getMorale(){return morale;}
        public int getSpeed(){return Math.max(1,speed+speedMod);}
        public int getStaggerTresh(){ return staggerTresh;}
        public String getName(){return name;}
        public String getDesc(){return description;}
        public List<Move> getMoveSet(){ return moveSet;}
        public List<appliedEffect> getEffectList(){ return effectsOnUnit;}
        public boolean staggered(){ return isStaggered;}
        public int getCritChance(){return Math.max(1,critChance+critChanceMod);}
        public float getCritmodifier(){return Math.max(0.01f,critmodifier+critMod);}
        public int getAtk(){ return Math.max(0, baseAtk+attackMod);}
        public int getDef(){return Math.max(0,baseDef+defendMod);}
        public int getCoinPowerMod(){return coinPowerMod;}
        public int getFinalCoinPowerMod(){return finalCoinPowerMod;}
        public float getDamageTypeEffect(damageType damType){
                if(damageAffect.containsKey(damType) && damageAffectModifiers.containsKey(damType)){
                        return Math.max(damageAffect.get(damType) + damageAffectModifiers.get(damType), 0.0f);
                }else{
                        System.out.println("Damage Type not found");
                        return 0.0f;
                }
        }

        public void modifyDamageTypeModifier(damageType damType, float toAdd){
                if(damageAffect.containsKey(damType) && damageAffectModifiers.containsKey(damType)){
                        damageAffectModifiers.put(damType, damageAffectModifiers.get(damType)+toAdd);
                }else{
                        System.out.println("Damage Type not found");
                }
        }

        public void resetAllDamageTypeModifiers(){
                for(damageType damType: damageAffectModifiers.keySet()){
                        damageAffectModifiers.put(damType, 0.0f);
                }
        }

        
        
        public void addFinalCoinPowerMod(int toAdd){
                finalCoinPowerMod+=toAdd;
        }
        
        public void resetFinalCoinPowerMod(){
                finalCoinPowerMod = 0;
        }

        public void addCoinPowerMod(int toAdd){
                coinPowerMod += toAdd;
        }

        public void resetCoinPowerMod(){
                coinPowerMod=0;
        }

        public void addDefendMod(int toAdd){
                defendMod += toAdd;
        }

        public void resetDefendMod(){
                defendMod = 0;
        }

        public void addAttackMod(int toAdd){
                attackMod +=toAdd;
        }

        public void resetAttackMod(){
                attackMod=0;
        }

        public void addCritModifier(float toAdd){
                critMod+=toAdd;
        }

        public void resetCritMod(){
                critMod = 0;
        }

        public void addSpeedModifier(int toAdd){
                speedMod += toAdd;
        }

        public void resetSpeedMod(){
                speedMod=0;
        }

        public void addCritChanceModifier(int toAdd){
                critChanceMod+= toAdd;
        }

        public void resetCritChance(){
                critChanceMod = 0;
        }

        
                
        
        public Move unop(){return unopposed;}
        public Move whenStaggered(){ return staggered;}
        
        public void checkStagger(){
            if(!isStaggered && !alreadyStaggered){
                if(hp <= staggerTresh){
                    isStaggered = true;
                        justGotStaggered = true;
                    System.out.println("!!! " + name + " is STAGGERED !!!"); 
                        alreadyStaggered = true;
                }
            }
        }

        public void checkStaggerRecover(){
                if(isStaggered){
                        if(!justGotStaggered){
                                isStaggered = false;
                                System.out.println(name + " has recovered from stagger."); 
                        }else{
                                justGotStaggered = false;
                        }
                        
                }
        }
        
        public void setStaggerOn(){ isStaggered = true;}

        public void modifyStaggerTresh(int changeBy){
                staggerTresh += changeBy;
        }
        
        
        
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
        
        public void takeHPDamage(float (float) dam, Unit source, damageType damType){
                Random ra = new Random();

                dam*= this.getDamageTypeEffect(damType);
                
                if(ra.nextInt(100) < source.getCritChance()){
                        System.out.println("Critical Hit (+"+ ((int)(source.getCritmodifier()*100))+"%)");
                        dam += dam*source.getCritmodifier();
                }

                float totalDamageModifier = 0.0f;
                float dif = source.getAtk() - this.getDef();
                float divider = (source.getAtk() - this.getDef())+25;
                totalDamageModifier = (dif/divider);
                dam += dam*totalDamageModifier;
                
            hp -= (int) dam;
            
            System.out.println("  > " + name + " took " + dam +" (+"+ ((int)(totalDamageModifier*100))+ "%) "  + " damage! (HP: " + hp + "/" + maxHP + ")");
        }

        public void takeHPDamage(int dam){
                
            hp -= dam;
            
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
