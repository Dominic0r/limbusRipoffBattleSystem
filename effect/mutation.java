package effect;
import combat.*;
public class mutation{
        
        final Type type;
        final int amount;
        final statusEffect effect;
        final Unit source;
        
        public mutation(Type type, int amount, statusEffect effect, Unit source){
            this.type = type;
            this.amount = amount;
            this.effect = effect;
            this.source = source;
        }
        
        public Type getType(){return type;}
        public int getAmount(){ return amount;}
        public statusEffect getEffect(){ return effect;}
        public Unit getSource(){ return source;}
        
}
