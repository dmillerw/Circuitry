package me.dmillerw.io.circuit.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * @author dmillerw
 */
public enum DataType {

    NULL(Value.class, NullValue.NULL),
    NUMBER(Number.class, 0),
    STRING(String.class, ""),
    VECTOR(Vec3d.class, Vec3d.ZERO);

    public final Class<?> type;
    public final Object zero;

    private <T> DataType(Class<T> type, T zero) {
        this.type = type;
        this.zero = zero;
    }

    public boolean isValid(Object value) {
        if (value == null)
            return false;

        Class<?> clazz = value.getClass();

        switch (this) {
            case NUMBER: {
                return clazz == int.class || clazz == float.class || clazz == double.class ||
                        clazz == Integer.class || clazz == Float.class || clazz == Double.class ||
                        clazz == Number.class;
            }
            case STRING:
                return value instanceof String;
            case VECTOR:
                return value instanceof Vec3d;
            default:
                return false;
        }
    }

    public static NBTBase getNbtTagFromValue(DataType dataType, Value value) {
        switch (dataType) {
            case NUMBER:
                return new NBTTagDouble(value.getNumber().doubleValue());
            case STRING:
                return new NBTTagString(value.getString());
            case NULL:
                return new NBTTagByte((byte) 0);
            default: return null;
        }
    }

    public static Value getValueFromNbtTag(DataType dataType, NBTBase value) {
        switch (dataType) {
            case NUMBER:
                return Value.of(dataType, ((NBTTagDouble) value).getDouble());
            case STRING:
                return Value.of(dataType, ((NBTTagString) value).getString());
            case NULL:
                return NullValue.NULL;
            default: return null;
        }
    }

    public static void writeValueToByteBuf(DataType dataType, Value value, ByteBuf buf) {
        switch (dataType) {
            case NUMBER:
                buf.writeDouble(value.getNumber().doubleValue());
            case STRING:
                ByteBufUtils.writeUTF8String(buf, value.getString());
            case NULL:
                buf.writeBoolean(false);
            default: return;
        }
    }

    public static Value readValueFromByteBuf(DataType dataType, ByteBuf buf) {
        switch (dataType) {
            case NUMBER:
                return Value.of(dataType, buf.readDouble());
            case STRING:
                return Value.of(dataType, ByteBufUtils.readUTF8String(buf));
            case NULL:
                return NullValue.NULL;
            default: return null;
        }
    }
}
