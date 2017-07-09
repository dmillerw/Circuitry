package me.dmillerw.io.circuit.data;

import net.minecraft.util.math.Vec3d;

/**
 * @author dmillerw
 */
public class NullValue extends Value {

    public static final Value NULL = new NullValue();

    protected NullValue() {
        super(DataType.NULL, null);
    }

    @Override
    public boolean isType(DataType checkType) {
        return false;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public Number getNumber() {
        return 0;
    }

    @Override
    public String getString() {
        return "";
    }

    @Override
    public Vec3d getVector() {
        return Vec3d.ZERO;
    }
}
