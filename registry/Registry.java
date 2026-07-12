package registry;

import java.util.HashMap;
import java.util.Map;
import combat.*;
import effect.*;
public class Registry {

    // Storage for status effects (e.g., "base:bleed" -> statusEffect object)
    public static Map<String, statusEffect> STATUS_EFFECTS = new HashMap<>();

    // Storage for preset unit templates (e.g., "base:player" -> Unit object)
    public static Map<String, Unit> PRESET_UNITS = new HashMap<>();

    // Helper method to register status effects cleanly
    public static void registerStatus(String id, statusEffect effect) {
        STATUS_EFFECTS.put(id.toLowerCase(), effect);
    }

    // Helper method to retrieve status effects
    public static statusEffect getStatus(String id) {
        return STATUS_EFFECTS.get(id.toLowerCase());
    }

    // Helper method to register unit templates
    public static void registerUnit(String id, Unit unit) {
        PRESET_UNITS.put(id.toLowerCase(), unit);
    }

    // Helper method to retrieve unit templates
    public static Unit getUnitTemplate(String id) {
        return PRESET_UNITS.get(id.toLowerCase());
    }
}
