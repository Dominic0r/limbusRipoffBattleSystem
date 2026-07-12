package combat;
import java.util.*;
import java.util.function.Consumer;
impor effect.*;
public class Coin{
        Random ra = new Random();
        private Consumer<clashResult> onHitEffect;
        int atkPoints; // attack points
        
        String description;
        
        public Coin(int atkPoints, String description){
            this(atkPoints, description, rst ->{});
        }
        
        public Coin (int atkPoints, String description, Consumer<clashResult> onHitEffect){
            this.atkPoints = atkPoints;
            this.description = description;
            this.onHitEffect = onHitEffect;
        }
        
        public void triggerOnHit(clashResult result){
            if(onHitEffect !=null){
                onHitEffect.accept(result);
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
