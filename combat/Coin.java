package combat;
import java.util.*;
import java.util.function.BiConsumer;
import effect.*;
public class Coin{
        Random ra = new Random();
        private BiConsumer<clashResult, Unit> onHitEffect;
        int atkPoints; // attack points
        boolean unbreakable=false;
        
        String description;
        
        public Coin(int atkPoints, String description){
            this(atkPoints, description, rst ->{});
        }
        
        public Coin (int atkPoints, boolean unbreakable, String description, Consumer<clashResult> onHitEffect){
            this.atkPoints = atkPoints;
            this.description = description;
            this.onHitEffect = onHitEffect;
                this.unbreakable = unbreakable;
        }

        public Coin (int atkPoints, String description, Consumer<clashResult> onHitEffect){
            this.atkPoints = atkPoints;
            this.description = description;
            this.onHitEffect = onHitEffect;
                this.unbreakable = false;
        }

        public boolean isUnbreakable(){return unbreakable;}

        
        
        
        public void triggerOnHit(clashResult result, Unit un){
            if(onHitEffect !=null){
                onHitEffect.accept(result, un);
            }
        }
        
        public String getDesc(){ return description;}
        public int getAtkPoints() { return atkPoints;}
        
        public boolean coinToss(int morale){
            int tresh = 50 + morale;
            return ra.nextInt(100)< tresh;
        }
        
        public int getCoinPower(int morale){
            int tresh = 50+morale;
            if(ra.nextInt(100)< tresh){
                return atkPoints;
            }else{
                return 0;
            }
        }
    }
