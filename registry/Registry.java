package registry;

import java.util.HashMap;
import java.util.Map;
import combat.*;
import effect.*;
public class Registry {

    // Storage for status effects
    public static Map<String, statusEffect> STATUS_EFFECTS = new HashMap<>();

    // Storage for units
    public static Map<String, Unit> PRESET_UNITS = new HashMap<>();

    //Storage for moves
    public static Map<String, Unit> MOVES = new HashMap<>();

    public static void registerStatus(String id, statusEffect effect) {
        STATUS_EFFECTS.put(id.toLowerCase(), effect);
    }

    public static statusEffect getStatus(String id) {
        return STATUS_EFFECTS.get(id.toLowerCase());
    }

    public static void registerUnit(String id, Unit unit) {
        PRESET_UNITS.put(id.toLowerCase(), unit);
    }

    public static Unit getUnitTemplate(String id) {
        return PRESET_UNITS.get(id.toLowerCase());
    }

    public static void registerMove(String id, Move move) {
        MOVES.put(id.toLowerCase(), move);
    }

    public static Move getMove(String id) {
        return MOVES.get(id.toLowerCase());
    }
}
