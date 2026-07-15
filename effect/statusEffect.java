package effect;
import java.util.*;
import java.util.function.BiConsumer;
import combat.*;
public class statusEffect{
        String name, description;
        
        boolean decays; // whether or not it decays every turn 
        int potencyLimit, stackLimit;
        
        private BiConsumer<Battlefield, Unit> onTurnStart;
        private BiConsumer<Battlefield, Unit> onHitReceived;
        private BiConsumer<Battlefield, Unit> onTurnEnd;
        private BiConsumer<Battlefield, Unit> onBeforeClash; // to activate before the comparison of the final values
        private BiConsumer<Battlefield, Unit> onClash;
        private BiConsumer<Battlefield, Unit> onHitGive;
        private BiConsumer<Battlefield, Unit> onBattleStart;
        private BiConsumer<Battlefield, Unit> onClashLose;
        private BiConsumer<Battlefield, Unit> onClashWin;
        private BiConsumer<Unit, Unit> onEnemyDeath; // first unit is the unit that dies, second is the unit holder
        private BiConsumer<Unit, Unit> onAllyDeath;
        private BiConsumer<Battlefield, Unit> onDeath;
        private BiConsumer<Unit, Unit> onCriticalGet; // first unit is source, second unit is holder
        private BiConsumer<Unit, Unit> onCriticalInflict; // first unit is target, second unit is holder
        
        public statusEffect(String name, boolean decays, int potencyLimit, int stackLimit, String description){
            this.name = name;
            this.description = description;
            this.decays = decays;
            this.potencyLimit = potencyLimit;
            this.stackLimit = stackLimit;
        }
        
        public statusEffect setOnTurnStart(BiConsumer<Battlefield, Unit> hook) {
            this.onTurnStart = hook;
            return this;
        }

        public statusEffect setOnHitReceived(BiConsumer<Battlefield, Unit> hook) {
            this.onHitReceived = hook;
            return this;
        }
    
        public statusEffect setOnTurnEnd(BiConsumer<Battlefield, Unit> hook) {
            this.onTurnEnd = hook;
            return this;
        }
        
        public statusEffect setOnClash(BiConsumer<Battlefield, Unit> hook) {
            this.onClash = hook;
            return this;
        }
        
        public statusEffect setOnHitGive(BiConsumer<Battlefield, Unit> hook) {
            this.onHitGive = hook;
            return this;
        }

        public statusEffect setOnBattleStart(BiConsumer<Battlefield, Unit> hook) {
            this.onBattleStart = hook;
            return this;
        }

        public statusEffect setOnClashLose(BiConsumer<Battlefield, Unit> hook) {
            this.onClashLose = hook;
            return this;
        }

        public statusEffect setOnClashWin(BiConsumer<Battlefield, Unit> hook) {
            this.onClashWin = hook;
            return this;
        }

        public statusEffect setOnBeforeClash(BiConsumer<Battlefield, Unit> hook) {
            this.onBeforeClash = hook;
            return this;
        }
        
        public statusEffect setOnAllyDeath(BiConsumer<Unit, Unit> hook) {
            this.onAllyDeath = hook;
            return this;
        }
        
        public statusEffect setOnEnemyDeath(BiConsumer<Unit, Unit> hook) {
            this.onEnemyDeath = hook;
            return this;
        }

        public statusEffect setOnDeath(BiConsumer<Battlefield, Unit> hook) {
            this.onDeath = hook;
            return this;
        }

        public statusEffect setOnCriticalGet(BiConsumer<Unit, Unit> hook) {
            this.onCriticalGet = hook;
            return this;
        }

        public statusEffect setOnCriticalInflict(BiConsumer<Unit, Unit> hook) {
            this.onCriticalInflict = hook;
            return this;
        }
        
        public void triggerTurnStart(Battlefield field, Unit un) {
            if (onTurnStart != null) onTurnStart.accept(field, un);
        }
    
        public void triggerOnHitReceived(Battlefield field, Unit un) {
            if (onHitReceived != null) onHitReceived.accept(field, un);
        }
    
        public void triggerTurnEnd(Battlefield field, Unit un) {
            if (onTurnEnd != null) onTurnEnd.accept(field, un);
        }
        
        public void triggerOnClash(Battlefield field, Unit un){
            if(onClash != null) onClash.accept(field, un);
        }
        
        public void triggerOnHitGive(Battlefield field, Unit un){
            if(onHitGive != null) onHitGive.accept(field, un);
        }

        public void triggerOnBattleStart(Battlefield field, Unit un){
            if(onBattleStart != null) onBattleStart.accept(field, un);
        }

        public void triggerOnClashLose(Battlefield field, Unit un){
            if(onClashLose != null) onClashLose.accept(field, un);
        }
        
        public void triggerOnClashWin(Battlefield field, Unit un){
            if(onClashWin != null) onClashWin.accept(field, un);
        }

        public void triggerOnBeforeClash(Battlefield field, Unit un){
            if(onBeforeClash != null) onBeforeClash.accept(field, un);
        }

        public void triggerOnEnemyDeath(Unit deadUn, Unit un){
            if(onEnemyDeath != null) onEnemyDeath.accept(deadUn, un);
        }

        public void triggerOnAllyDeath(Unit deadUn, Unit un){
            if(onAllyDeath != null) onAllyDeath.accept(deadUn, un);
        }

        public void triggerOnDeath(Battlefield field, Unit un){
            if(onDeath != null) onDeath.accept(field, un);
        }

        public void triggerOnCriticalGet(Unit source, Unit un){
            if(onCriticalGet != null) onCriticalGet.accept(source, un);
        }

        public void triggerOnCriticalInflict(Unit target, Unit un){
            if(onCriticalInflict != null) onCriticalInfict.accept(source, un);
        }
        
        public String getName(){ return name;}
        public String getDesc(){ return description;}
        public boolean Decays() { return decays;}
        public int getPotencyLimit() { return potencyLimit;}
        public int getStackLimit() { return stackLimit;}
    }
