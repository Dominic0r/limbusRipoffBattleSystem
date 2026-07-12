package combat;
impor effect.*;
public class clashResult{
        Unit winner;
        Unit loser;
        int remainingCoins;
        List<Coin> winnerCoinSet = new ArrayList<>();
        
        public clashResult(Unit winner, Unit loser, int remainingCoins, List<Coin> winnerCoinSet){
            this.winner = winner;
            this.loser = loser;
            this.remainingCoins = remainingCoins;
            this.winnerCoinSet = winnerCoinSet;
        }
        
        public Unit getWinner(){ return winner;}
        public Unit getLoser(){ return loser;}
        public int getRemCoins() { return remainingCoins;}
        public List<Coin> getCoinSet(){ return winnerCoinSet;}
    }
