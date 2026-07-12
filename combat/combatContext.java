package combat;
impor effect.*;
public class combatContext{
        Unit attacker;
        Move attackerMove;
        Unit defender;
        Move defenderMove;
        int totalDamage;
        Battlefield field;
        
        public combatContext (Unit attacker, Move attackerMove, Unit defender, Move defenderMove, Battlefield field){
            this.attacker = attacker;
            this.attackerMove = attackerMove;
            this.defender = defender;
            this.defenderMove = defenderMove;
            this.field = field;
        }
        public Unit getAttacker(){ return attacker;}
        public Move getAttackerMove(){ return attackerMove;}
        public Unit getDefender(){ return defender;}
        public Move getDefenderMove(){ return defenderMove;}
        public int totalDamage(){ return totalDamage;}
        public Battlefield field() {return field;}
        
    }
