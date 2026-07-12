package effect;
import combat.*;
public class appliedEffect{ // created on unit
        statusEffect effect;
        int potency, // the amount of the effect per stack 
        stack; // the total amount of stack 
        Unit appliedBy;
        
        public appliedEffect(statusEffect effect, int potency, int stack, Unit appliedBy){
            this.effect = effect;
            this.potency = potency;
            this.stack = stack;
            this.appliedBy = appliedBy;
        }
        
        public statusEffect stat(){ return effect;}
        public int getPotency(){ return potency;}
        public int getStack(){ return stack;}
        public Unit effectSource(){return appliedBy;}
        
        public void decayStack(){
            if(effect.Decays()){
                stack--;
            }
        }
        
        public boolean stackIsEmpty(){ return stack <=0;}
        
        public void changePotency(int changeBy){
            potency += changeBy;
        }
        
        public void incrementPotency(){ potency++;}
        public void decrementPotency(){ potency--;}
        
        public void changeStack(int changeBy){
            stack += changeBy;
        }
        
        public void incrementStack(){ stack++;}
        public void decrementStack(){ stack--;}
        
        public void keepInBounds(){
            if(stack < 0){
                stack = 0;
            }
            
            if(potency < 1){
                potency = 1;
            }
            
            if(potency > effect.getLimit()){
                potency = effect.getLimit();
            }
            
        }
        
    }
