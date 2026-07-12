package combat;
import java.util.*;
import effect.*;

public class Move{ 
        String name;
        String description;
        List <Coin> coinSet = new ArrayList<>();
        int baseatk;

        private MoveCondition condition = (field, user, target) -> true;
        
        public Move(String name, int baseatk, String description){
            this.name = name;
            this.description = description;
            this.baseatk = baseatk;
        }

        public Move setCondition(MoveCondition condition) {
                if (condition != null) {
                    this.condition = condition;
                }
                return this; 
        }

        public boolean isUsable(Battlefield field, Unit user, Unit target) {
                return condition.canUse(field, user, target);
        }
        
        public String getName(){return name;}
        public String getDesc(){ return description;}
        public int getBaseAtk(){ return baseatk;}
        
        public int getTotalPoints(){
            int tot = baseatk;
            for(Coin co: coinSet){
                tot+= co.getAtkPoints();
            }
            return tot;
        }
        
        public void addCoin(Coin toAdd){
            coinSet.add(toAdd);
        }
        
        public List<Coin> getCoinSet(){ return coinSet;}
        
        
        public List<Coin> getClashCoins(){ // USE THIS FOR BATTLES
            return new ArrayList<>(this.coinSet);
        }
}
