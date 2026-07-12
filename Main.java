import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import combat.*;
import effect.*;
import content.*;
import registry.*;
public class Main
{
    public static Random ra = new Random();
    public static Scanner sc = new Scanner(System.in);
    
    public static clashResult clashFunction(Battlefield field, combatContext comctx){
        List<Coin> attackerCoinSet = comctx.getAttackerMove().getClashCoins();
        List<Coin> defenderCoinSet = comctx.getDefenderMove().getClashCoins();
        
        int attackerCoinCount = attackerCoinSet.size();
        int defenderCoinCount = defenderCoinSet.size();
        
        int currentAttackerPoints = comctx.getAttackerMove().getBaseAtk();
        int currentDefenderPoints = comctx.getDefenderMove().getBaseAtk();
        
        // Added UI for clash initialization
        System.out.println("\n--- CLASH START --- ️");
        System.out.println(comctx.getAttacker().getName() + " [" + comctx.getAttackerMove().getName() + "] vs " 
                         + comctx.getDefender().getName() + " [" + comctx.getDefenderMove().getName() + "]");
        
        boolean bothStillHaveCoins = true;
        int clashRound = 1; // Added UI
        do{
            currentAttackerPoints = comctx.getAttackerMove().getBaseAtk();
            currentDefenderPoints = comctx.getDefenderMove().getBaseAtk();
            
            for(Coin co: attackerCoinSet){
                currentAttackerPoints += co.getCoinPower(comctx.getAttacker().getMorale());
            }
            
            for(Coin co: defenderCoinSet){
                currentDefenderPoints += co.getCoinPower(comctx.getDefender().getMorale());
            }
            
            // Added UI for Clash Results per round
            System.out.println("  [Round " + clashRound + "] " 
                               + comctx.getAttacker().getName() + " rolled: " + currentAttackerPoints + " | " 
                               + comctx.getDefender().getName() + " rolled: " + currentDefenderPoints);
            
            if(currentAttackerPoints == currentDefenderPoints){
                System.out.println("    -> Tie! No coins lost."); // Added UI
            }else{
                if(currentAttackerPoints > currentDefenderPoints){
                    defenderCoinSet.remove(defenderCoinSet.size()-1);
                    defenderCoinCount--;
                    System.out.println("    -> " + comctx.getDefender().getName() + " lost a coin!"); // Added UI
                }else{
                    attackerCoinSet.remove(attackerCoinSet.size()-1);
                    attackerCoinCount--;
                    System.out.println("    -> " + comctx.getAttacker().getName() + " lost a coin!"); // Added UI
                }
            }
            
            
            for(appliedEffect app : comctx.getAttacker().getEffectList()){
                app.stat().triggerOnClash(field, comctx.getAttacker());
            }
            
            for(appliedEffect app : comctx.getDefender().getEffectList()){
                app.stat().triggerOnClash(field, comctx.getDefender());
            }
            
            checkStacks(field);
            bothStillHaveCoins = (attackerCoinCount> 0) && (defenderCoinCount > 0);
            clashRound++;
        }while(bothStillHaveCoins);
        
        Unit winner, loser;
        int remainingCoins;
        List<Coin> winnerCoinSet = new ArrayList<>();
        boolean unopposed = false;
        if(attackerCoinCount >0){
            winner = comctx.getAttacker();
            loser = comctx.getDefender();
            remainingCoins = attackerCoinCount;
            winnerCoinSet = attackerCoinSet;
            
            unopposed = comctx.getDefenderMove() == comctx.getDefender().unop();
            
        }else{
            winner = comctx.getDefender();
            loser = comctx.getAttacker();
            remainingCoins = defenderCoinCount;
            winnerCoinSet = defenderCoinSet;
            unopposed = comctx.getAttackerMove() == comctx.getAttacker().unop();
        }
        
        // Added UI for winner
        System.out.println("\n" + winner.getName() + " won the clash with " + remainingCoins + " coin(s) remaining!");
        
        if(!unopposed){
            winner.modifyMorale(5);
            
            loser.modifyMorale(-5);
        }
        
        clashResult finalResult = new clashResult(winner, loser, remainingCoins, winnerCoinSet);
        return finalResult;
    }
    
    public static void afterClash(clashResult result, Battlefield field, combatContext comctx){
        System.out.println("\n --- CLASH RESOLUTION --- "); // Added UI

        for(appliedEffect app : result.getWinner().getEffectList()){
                    app.stat().triggerOnClashWin(field, result.getWinner());
        }
        for(appliedEffect app : result.getLoser().getEffectList()){
                    app.stat().triggerOnClashLose(field, result.getLoser());
        }
        for(Coin co: result.getCoinSet()){
            System.out.print(result.getWinner().getName() + " activates: " + co.getDesc()); // Added UI
            
            if(co.getCoinPower(result.getWinner().getMorale()) >0){
                System.out.print(" - HEADS!");
                co.triggerOnHit(result);
                
                for(appliedEffect app : result.getWinner().getEffectList()){
                    app.stat().triggerOnHitGive(field, result.getWinner());
                }
                
                for(appliedEffect app : result.getLoser().getEffectList()){
                    app.stat().triggerOnHitReceived(field, result.getLoser());
                }
            }else{
                System.out.println(" - TAILS!");
            }
            
            checkStacks(field);
        }
    }
    
    public static void turnStart(Battlefield field){
        // Added UI for Turn Info

        
        System.out.println("\n===============================================");
        System.out.println("                TURN " + field.getTurnCount() + " START");
        System.out.println("===============================================");

        for(Unit un : field.getAllies()){
            for(appliedEffect app : un.getEffectList()){
                app.stat().triggerTurnStart(field, un);
            }
        }
        for(Unit un : field.getEnemies()){
            for(appliedEffect app : un.getEffectList()){
                app.stat().triggerTurnStart(field, un);
            }
        }
        for(appliedEffect app: playerUnit.getEffectList()){
            app.stat().triggerTurnStart(field,playerUnit);
        }

        
        System.out.println("[ Player Info ]");
        System.out.println(playerUnit.getName() + " | HP: " + playerUnit.getHP() + "/" + playerUnit.maxHP + " | Morale: " + playerUnit.getMorale());
        if(playerUnit.getEffectList().size()>0){
            for(appliedEffect app: playerUnit.getEffectList()){
                System.out.println(app.stat().getName()+ " "+ app.getPotency()+" potency, "+ app.getStack()+ " stack");
            }
        }
        if(playerUnit.staggered()){
            System.out.println("Staggered!");
        }
        
        System.out.println("\n[ Enemies ]");
        for(int i = 0; i < field.getEnemies().size(); i++){
            Unit en = field.getEnemies().get(i);
            System.out.println((i+1) + ". " + en.getName() + " | HP: " + en.getHP() + "/" + en.maxHP + " | Morale: "+ en.getMorale());
            if(en.getEffectList().size()>0){
                for(appliedEffect app: en.getEffectList()){
                    System.out.println(app.stat().getName()+ " "+ app.getPotency()+" potency, "+ app.getStack()+ " stack");
                }
            }
            if(en.staggered()){
                System.out.println("Staggered!");
            }
        }
        
        if(!allAllies.isEmpty()){
            System.out.println("\n[ Allies ]");
            for(int i = 0; i < field.getAllies().size(); i++){
                Unit en = field.getAllies().get(i);
                System.out.println((i+1) + ". " + en.getName() + " | HP: " + en.getHP() + "/" + en.maxHP + " | Morale: "+ en.getMorale());
                if(en.getEffectList().size()>0){
                    for(appliedEffect app: en.getEffectList()){
                        System.out.println(app.stat().getName()+ " "+ app.getPotency()+" potency, "+ app.getStack()+ " stack");
                    }
                }
                if(en.staggered()){
                    System.out.println("Staggered!");
                }
            }
        }
        
        System.out.println("===============================================\n");

        
    }
    
    public static void turnEnd(Battlefield field){
        System.out.println("\n===============================================");
        System.out.println("                 TURN " + field.getTurnCount() + " END");   
        System.out.println("===============================================\n");
        
        for(Unit un : field.getAllies()){
            for(appliedEffect app : un.getEffectList()){
                app.stat().triggerTurnEnd(field, un);
            }
            un.resetCritChance();
        }
        for(Unit un : field.getEnemies()){
            for(appliedEffect app : un.getEffectList()){
                app.stat().triggerTurnEnd(field, un);
            }
            un.resetCritChance();
        }

        for(appliedEffect app: playerUnit.getEffectList()){
            app.stat().triggerTurnEnd(field,playerUnit);
        }
        playerUnit.resetCritChance();

        
        field.incrementTurnCount(); 
    }

    public static void battleStart(Battlefield field){

        System.out.println("\n===============================================");
        System.out.println("                 BATTLE START!");   
        System.out.println("===============================================\n");
        
        for(Unit un : field.getAllies()){
            for(appliedEffect app : un.getEffectList()){
                app.stat().triggerOnBattleStart(field, un);
            }
        }
        for(Unit un : field.getEnemies()){
            for(appliedEffect app : un.getEffectList()){
                app.stat().triggerOnBattleStart(field, un);
            }
        }
        for(appliedEffect app: playerUnit.getEffectList()){
            app.stat().triggerOnBattleStart(field,playerUnit);
        }
    }


    
    public static void keepAllAppliedEffectsInBounds(Battlefield field){
        for(Unit un : field.getAllies()){
            for(appliedEffect app : un.getEffectList()){
                app.keepInBounds();
            }
            un.statLimiter();
        }
        
        for(Unit un : field.getEnemies()){
            for(appliedEffect app : un.getEffectList()){
                app.keepInBounds();
            }
            un.statLimiter();
        }
        for(appliedEffect app: playerUnit.getEffectList()){
            app.keepInBounds();
        }
        playerUnit.statLimiter();
    }
    
    public static combatContext playerChoose(Battlefield field){
        int counter = 1;
        int coincounter=1;
        System.out.println("Your moves: ");

        List<Move> validMoves = new ArrayList<>();
        for(Move mov: playerUnit.getMoveSet()){
            if(mov.isUsable(field, playerUnit)){
                validMoves.add(mov);
            }
        }


        
        for(Move mov : validMoves){
            System.out.println(counter+ ": "+ mov.getName()+ "\n"+mov.getDesc());
            for(Coin co: mov.getCoinSet()){
                System.out.println("Coin "+ coincounter+ ": "+ co.getDesc());
                coincounter++;
            }
            counter++;
            coincounter = 1;
        }

        
        System.out.print("\nChoose move (number): ");
        
        boolean validInput = false;
        int choice;
        do{
            choice = Integer.parseInt(sc.nextLine());
            
            if(choice > counter){
                System.out.println("Invalid input. Try again:");
                validInput = false;
            }else{
                validInput = true;
            }
            
        }while(!validInput);
        
        Move plyrMv = validMoves.get(choice-1);
        counter = 0;
        System.out.println("\nChoose a target: ");
        for(Unit un: field.getEnemies()){
            System.out.println(counter+": "+ un.getName()+ "\n"+un.getDesc());
            counter++;
        }
        System.out.print("Target (number): "); 
        
        do{
            choice = Integer.parseInt(sc.nextLine());
            
            if(choice > counter){
                System.out.println("Invalid input. Try again:");
                validInput = false;
            }else{
                validInput = true;
            }
            
        }while(!validInput);
        
        Unit targetEnemy = field.getEnemies().get(choice-1);
        
        Move targetMove;
        
        int maxnum=Integer.MIN_VALUE;
        Move maxMove=null;
        for(Move mov: targetEnemy.getMoveSet()){
            if(mov.getTotalPoints()> maxnum){
                maxnum = mov.getTotalPoints();
                maxMove = mov;
            }
        }
        
        targetMove = maxMove;
        if(targetEnemy.staggered()){
            targetMove = targetEnemy.unop();
        }
        
        System.out.println("\n>>> You selected " + plyrMv.getName() + " targeting " + targetEnemy.getName() + " <<<");
        
        combatContext finalCC = new combatContext(playerUnit, plyrMv, targetEnemy, targetMove, field);
        return finalCC;
        
    }
    //public combatContext (Unit attacker, Move attackerMove, Unit defender, Move defenderMove, Battlefield field){
    
    public static combatContext allyMove(Battlefield field, Unit un, List<combatContext> attackQueue){
        int maxnum=Integer.MIN_VALUE; // pick their move
        Move maxMove=null;

        List<Move> validMoves = new ArrayList<>();
        for(Move mov: un.getMoveSet()){
            if(mov.isUsable(field, un)){
                validMoves.add(mov);
            }
        }


        
        for(Move mov: validMoves){
            if(mov.getTotalPoints()> maxnum){
                maxnum = mov.getTotalPoints();
                maxMove = mov;
            }
        }
        Move aMov = maxMove; 
        
        Unit targetUn=null;
        
        //pick their target
        boolean alreadyTargeted = false;
        for(Unit enun : field.getEnemies()){
            alreadyTargeted = false;
            for(combatContext comctx : attackQueue){
                if(comctx.getDefender() == enun){
                    alreadyTargeted = true;
                }
            }
            if(!alreadyTargeted){
                targetUn = enun;
            }
        }
        
        boolean secondary = false;
        if(targetUn == null){
            targetUn = field.getEnemies().get(ra.nextInt(field.getEnemies().size()));
            secondary = true;
        }

        validMoves.clear();
        for(Move mov: targetUn.getMoveSet()){
            if(mov.isUsable(field, targetUn)){
                validMoves.add(mov);
            }
        }
        
        maxnum = Integer.MIN_VALUE;
        for(Move mov: validMoves){
            if(mov.getTotalPoints()> maxnum){
                maxnum = mov.getTotalPoints();
                maxMove = mov;
            }
        }
        Move eMov = maxMove; 
        if(secondary || targetUn.staggered()){
            eMov = targetUn.unop();
        }
        
        
        combatContext finalCC = new combatContext(un, aMov, targetUn, eMov, field);
        return finalCC;
        
        
    }
    
    public static combatContext enemMove(Battlefield field, Unit un, List<combatContext> attackQueue){
        int maxnum=Integer.MIN_VALUE; // pick their move
        Move maxMove=null;
        for(Move mov: un.getMoveSet()){
            if(mov.getTotalPoints()> maxnum){
                maxnum = mov.getTotalPoints();
                maxMove = mov;
            }
        }
        Move aMov = maxMove; 
        Move eMov= null;
        Unit targetUn=null;
        boolean secondary=false;
        //pick their target
        if(field.getAllies().size() >0){
            boolean alreadyTargeted = false;
            for(Unit enun : field.getAllies()){
                alreadyTargeted = false;
                for(combatContext comctx : attackQueue){
                    if(comctx.getDefender() == enun){
                        alreadyTargeted = true;
                    }
                }
                if(!alreadyTargeted){
                    targetUn = enun;
                }
            }
            
            secondary = false;
            if(targetUn == null){
                targetUn = field.getAllies().get(ra.nextInt(field.getAllies().size()));
                secondary = true;
            }
            
            maxnum = Integer.MIN_VALUE;
            for(Move mov: targetUn.getMoveSet()){
                if(mov.getTotalPoints()> maxnum){
                    maxnum = mov.getTotalPoints();
                    maxMove = mov;
                }
            }
            eMov = maxMove; 
        }else{
            targetUn = playerUnit;
        }
        if(secondary || targetUn.staggered()){
            eMov = targetUn.unop();
        }
        combatContext finalCC = new combatContext(un, aMov, targetUn, eMov, field);
        return finalCC;
        
        
    }
    
    //public Battlefield(List<Unit> allies, List<Unit> enemies, int turnCount){
    public static Battlefield genBatContext(){
        batContext = new Battlefield(allAllies, allEnemies, 1);
        return batContext;
    }
    
    public static Battlefield batContext = genBatContext();
    
    public static List<Unit> allAllies = new ArrayList<>();
    public static List<Unit> allEnemies = new ArrayList<>();
    public static int turnCount = 1;
    
    public static void checkStacks(Battlefield field){
        List<appliedEffect> toRemove = new ArrayList<>();
        for(Unit un: field.getAllies()){
            
            for(appliedEffect app : un.getEffectList()){
                if(app.stackIsEmpty()){
                    toRemove.add(app);
                }
            }
            
            un.getEffectList().removeAll(toRemove);
            toRemove.clear();
        }
        
        for(Unit un: field.getEnemies()){
            for(appliedEffect app : un.getEffectList()){
                if(app.stackIsEmpty()){
                    toRemove.add(app);
                }
            }
            
            un.getEffectList().removeAll(toRemove);
            toRemove.clear();
        }
        
        for(appliedEffect app : playerUnit.getEffectList()){
                if(app.stackIsEmpty()){
                    toRemove.add(app);
                }
            }
            
            playerUnit.getEffectList().removeAll(toRemove);
            toRemove.clear();
    }
    
    public static void battleFlow(Battlefield field){
        List<combatContext> attackQueue = new ArrayList<>();
        turnStart(field);
        keepAllAppliedEffectsInBounds(field);
        
        
        
        if(!playerUnit.staggered()){
            combatContext playerMove = playerChoose(field);// have player and all NPCs pick move and target. generates combatContext 
            attackQueue.add(playerMove);
        }
        if(field.getAllies().size()+1 > field.getEnemies().size()){
            if(field.getAllies().size()>0){
                for(Unit un: field.getAllies()){
                    if(!un.staggered() && attackQueue.get(0).getDefender() != un){
                    attackQueue.add(allyMove(field, un, attackQueue));
                    }
                }
            }
        }else{
            if(field.getEnemies().size()>0){
                for(Unit un: field.getEnemies()){
                    if(!un.staggered()&& attackQueue.get(0).getDefender() != un){
                    attackQueue.add(enemMove(field, un, attackQueue));
                    }
                }
            }
        }
        
        attackQueue.sort(Comparator.comparingInt((combatContext cc) -> cc.getAttacker().getSpeed()).reversed()); // sorts by fastest attack speed
        
        for(combatContext cctx : attackQueue){
            if((cctx.getAttacker().getHP() >0 && cctx.getDefender().getHP() > 0) && (!cctx.getAttacker().staggered())){
                clashResult finalResult = clashFunction(field, cctx);
                
                keepAllAppliedEffectsInBounds(field);
                
                if(finalResult.getWinner().getHP() >0 && !finalResult.getWinner().staggered()){
                    afterClash(finalResult, field, cctx);
                }
                
                keepAllAppliedEffectsInBounds(field);
            }
            checkHP(field);
            checkAllStagger(field);
        }
        
        turnEnd(field);
        haveAllPendingMutationsApplied(field);
        keepAllAppliedEffectsInBounds(field);
        checkHP(field);
        checkWin(field);
        checkAllStaggerRecover(field);
        
    }
    
    public static void checkHP(Battlefield field){
        List<Unit> toRemove = new ArrayList<>();
        
        for(Unit un: field.getAllies()){
            if(un.getHP()<=0){
                toRemove.add(un);
                System.out.println("An Ally: "+un.getName()+ " Has Died!");
            }
        }
        
        allAllies.removeAll(toRemove);
        toRemove.clear();
        
        for(Unit un: field.getEnemies()){
            if(un.getHP()<=0){
                toRemove.add(un);
                System.out.println("An Enemy: "+un.getName()+ " Has Died!");
            }
        }
        
        allEnemies.removeAll(toRemove);
        toRemove.clear();
        
        if(playerUnit.getHP() <= 0){
            // lose scenario
        }
    }
    
    public static void checkWin(Battlefield field){
        if(field.getEnemies().size() <=0){
            System.out.println("YOU WIN!");
            System.exit(0);
        }
    }
    
    public static void youlose(){
        System.out.println("YOU LOSE!");
        System.exit(0);
    }
    
    public static void haveAllPendingMutationsApplied(Battlefield field){
        for(Unit un: field.getAllies()){
            un.applyPendingMutations();
        }
        
        for(Unit un: field.getEnemies()){
            un.applyPendingMutations();
        }
        
        playerUnit.applyPendingMutations();
    }
    
    public static void checkAllStagger(Battlefield field){
        for(Unit un: field.getAllies()){
            un.checkStagger();
        }
        
        for(Unit un: field.getEnemies()){
            un.checkStagger();
        }
        playerUnit.checkStagger();
    }

    public static void checkAllStaggerRecover(Battlefield field){
        for(Unit un: field.getAllies()){
            un.checkStaggerRecover();
        }
        
        for(Unit un: field.getEnemies()){
            un.checkStaggerRecover();
        }
        playerUnit.checkStaggerRecover();
    }
    
    public static Unit playerUnit;
    //===============================================================================================
    //MOD ZONE touch anything outside this with precaution
    //this is where you load your mod packages
    public static void registerAllPackages() {

        //Note: packages that declare passives have HIGHER priority over packages that contain moves that use those passives
        loadPackage(new limbusPassives());
        loadPackage(new BaseContent()); 
        

        playerUnit = Registry.getUnitTemplate("base:player");

        allAllies.add(Registry.getUnitTemplate("base:ally"));

        allEnemies.add(Registry.getUnitTemplate("base:enemy"));
    }
    //===============================================================================================

    private static void loadPackage(ContentPackage pkg) {
        System.out.println("Loading package: " + pkg.getPackageId());
        pkg.registerContent();
    }
    
    
    
    public static void main(String[] args) {
        System.out.println("--- INITIALIZING GAME ---\n"); 
        /*defPlayerUnit(); // Legacy Code
        defDefaultStats();
        defineDefaultEnemy();
        defineDefaultAlly();*/
        
        registerAllPackages();
        genBatContext();
        
        System.out.println("--- YOUR MOVESET ---");
        for(Move mov : playerUnit.getMoveSet()){
            System.out.println("\n"+mov.getName()+ ": "+ mov.getBaseAtk()+ " (Base) | "+ mov.getTotalPoints()+ " (total)\n"+ mov.getDesc());
            for(Coin co: mov.getCoinSet()){
                System.out.println(co.getDesc()+" - " + co.getAtkPoints());
            }
        }
        
        battleStart(batContext);
        while(true){
        battleFlow(batContext);
        
        }
        
        
    }
}
