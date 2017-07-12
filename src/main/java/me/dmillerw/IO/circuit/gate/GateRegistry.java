package me.dmillerw.io.circuit.gate;

import com.google.common.collect.Maps;
import me.dmillerw.io.circuit.gate.arithmatic.GateAdd;
import me.dmillerw.io.circuit.gate.arithmatic.GateFloor;
import me.dmillerw.io.circuit.gate.entity.GateEntityPosition;
import me.dmillerw.io.circuit.gate.logic.GateAnd;
import me.dmillerw.io.circuit.gate.time.GateTimer;
import me.dmillerw.io.circuit.gate.util.GateConstantValue;
import me.dmillerw.io.circuit.gate.util.GateNonNullCount;
import me.dmillerw.io.circuit.gate.vector.GateDecompose;

import java.util.Collection;
import java.util.Map;

/**
 * @author dmillerw
 */
public class GateRegistry {

    public static final GateRegistry INSTANCE = new GateRegistry();
    static {
        INSTANCE.register(new GateAdd());
        INSTANCE.register(new GateFloor());
        INSTANCE.register(new GateAnd());
        INSTANCE.register(new GateTimer());
        INSTANCE.register(new GateConstantValue());
        INSTANCE.register(new GateNonNullCount());
        INSTANCE.register(new GateEntityPosition());
        INSTANCE.register(new GateDecompose());
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
