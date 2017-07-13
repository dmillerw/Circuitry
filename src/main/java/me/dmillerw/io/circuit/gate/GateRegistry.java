package me.dmillerw.io.circuit.gate;

import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;
import net.minecraftforge.fml.common.FMLLog;

import java.util.Collection;
import java.util.Map;

/**
 * @author dmillerw
 */
public class GateRegistry {

    public static final GateRegistry INSTANCE = new GateRegistry();

    static {
        try {
            // Loads all Gates found in 'me.dmillerw.io.circuit.gate'
            // Why? Because I kept forgetting to register new Gates, and I'm lazy
            ClassPath path = ClassPath.from(GateRegistry.class.getClassLoader());
            for (ClassPath.ClassInfo classInfo : path.getTopLevelClassesRecursive("me.dmillerw.io.circuit.gate")) {
                if (classInfo.getName().contains("BaseGate") || classInfo.getName().contains("GateRegistry"))
                    continue;

                Class<?> clazz = classInfo.load();
                if (clazz.getSuperclass() != BaseGate.class)
                    continue;

                INSTANCE.register((BaseGate) clazz.newInstance());
            }
        } catch (Exception e) {
            FMLLog.bigWarning("Failed to load I/O Gates!!");
        }
    }

    private Map<String, BaseGate> gates = Maps.newHashMap();

    public void register(BaseGate gate) {
        this.gates.put(gate.getKey(), gate);
    }

    public Collection<BaseGate> getAllGates() { return gates.values(); };
    public BaseGate getGate(String key) {
        return gates.get(key);
    }
}
