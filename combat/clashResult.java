package combat;
import java.util.*;
import effect.*;
public class clashResult{
        Unit winner;
        Unit loser;
        int remainingCoins;
        List<Coin> winnerCoinSet = new ArrayList<>();
        
        List<Coin> loserUnbreakables = new ArrayList<>();
        
        public clashResult(Unit winner, Unit loser, int remainingCoins, List<Coin> winnerCoinSet, List<Coin> loserUnbreakables, List<Coin> winnerUnbreakables){
            this.winner = winner;
            this.loser = loser;
            this.remainingCoins = remainingCoins;
            this.winnerCoinSet = winnerCoinSet;
                this.loserUnbreakables = loserUnbreakables;
                for(Coin co: winnerUnbreakables){
                        if(!winnerCoinSet.contains(co)){
                                winnerCoinSet.add(co);
                        }
                }
        }
        
        public Unit getWinner(){ return winner;}
        public Unit getLoser(){ return loser;}
        public int getRemCoins() { return remainingCoins;}
        public List<Coin> getCoinSet(){ return winnerCoinSet;}
        public List<Coin> getLoserUnbreakables(){return loserUnbreakables;}
    }
