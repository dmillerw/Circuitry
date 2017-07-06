package me.dmillerw.inspection.circuit.data;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;

/**
 * @author dmillerw
 */
public enum DataType {

    NUMBER(Number.class, 0);

    public final Class<?> type;
    public final Object zero;

    private <T> DataType(Class<T> type, T zero) {
        this.type = type;
        this.zero = zero;
    }

    public static <T> T cast(DataType type, Object value) {
        return cast((Class<T>) type.type, value);
    }
    private static <T> T cast(Class<T> type, Object value) {
        return (T) value;
    }

    public static NBTBase getNbtTagFromValue(DataType dataType, Object value) {
        switch (dataType) {
            case NUMBER: return new NBTTagDouble(((Number)value).doubleValue());
            default: return null;
        }
    }

    public static Object getValueFromNbtTag(DataType dataType, NBTBase value) {
        switch (dataType) {
            case NUMBER: return ((NBTTagDouble)value).getDouble();
            default: return null;
        }
    }
}
