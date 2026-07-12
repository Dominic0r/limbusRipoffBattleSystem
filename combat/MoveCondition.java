package combat;

@FunctionalInterface
public interface MoveCondition {
    /**
     * @param field The active battlefield state.
     * @param user The unit attempting to use this move.
     * @param target The unit being targeted (can be null if checking general availability).
     * @return true if the move can be selected/executed; false otherwise.
     */
    boolean canUse(Battlefield field, Unit user);
}
