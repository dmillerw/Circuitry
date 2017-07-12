package me.dmillerw.io.circuit.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.*;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * @author dmillerw
 */
public enum DataType {

    NULL(Value.class, NullValue.NULL),
    NUMBER(Number.class, 0),
    STRING(String.class, ""),
    VECTOR(Vec3d.class, Vec3d.ZERO),
    ENTITY(Integer.class, 0);

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
            case ENTITY:
                return value instanceof Number;
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
            case VECTOR: {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setDouble("X", value.getVector().x);
                tag.setDouble("Y", value.getVector().y);
                tag.setDouble("Z", value.getVector().z);
                return tag;
            }
            case ENTITY:
                return new NBTTagInt(value.getContainedEntityId());
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
            case VECTOR: {
                NBTTagCompound tag = (NBTTagCompound) value;
                return Value.of(dataType, new Vec3d(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z")));
            }
            case ENTITY:
                return Value.of(dataType, ((NBTTagInt) value).getInt());
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
            case VECTOR: {
                Vec3d vec = value.getVector();
                buf.writeDouble(vec.x);
                buf.writeDouble(vec.y);
                buf.writeDouble(vec.z);
            }
            case ENTITY:
                buf.writeInt(value.getContainedEntityId());
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
            case VECTOR:
                return Value.of(dataType, new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()));
            case ENTITY:
                return Value.of(dataType, buf.readInt());
            case NULL:
                return NullValue.NULL;
            default: return null;
        }
    }
}
