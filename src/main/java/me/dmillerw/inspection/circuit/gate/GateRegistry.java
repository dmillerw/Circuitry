package me.dmillerw.inspection.circuit.gate;

import com.google.common.collect.Maps;
import me.dmillerw.inspection.circuit.gate.arithmatic.GateAdd;
import me.dmillerw.inspection.circuit.gate.util.GateConstantValue;

import java.util.Collection;
import java.util.Map;

/**
 * @author dmillerw
 */
public class GateRegistry {

    public static final GateRegistry INSTANCE = new GateRegistry();
    static {
        INSTANCE.register(new GateAdd());
        INSTANCE.register(new GateConstantValue());
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
