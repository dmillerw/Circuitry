package me.dmillerw.io.circuit.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * @author dmillerw
 */
public class Port {

    public static Port create(String name, DataType type) {
        return new Port(name, type, NullValue.NULL);
    }

    public static Port fromNbt(NBTTagCompound tagCompound) {
        String name = tagCompound.getString("Name");
        DataType type = DataType.values()[tagCompound.getByte("Type")];
        Value value = DataType.getValueFromNbtTag(type, tagCompound.getTag("Value"));
        Value previousValue = DataType.getValueFromNbtTag(type, tagCompound.getTag("PreviousValue"));

        Port port = new Port(name, type, value);
        port.previousValue = previousValue;

        return port;
    }

    public static Port fromByteBuf(ByteBuf buf) {
        String name = ByteBufUtils.readUTF8String(buf);
        DataType type = DataType.values()[buf.readByte()];
        Value value = DataType.readValueFromByteBuf(type, buf);
        Value previousValue = DataType.readValueFromByteBuf(type, buf);

        Port port = new Port(name, type, value);
        port.previousValue = previousValue;

        return port;
    }

    private final String name;
    private final DataType type;
    private Value value;
    private Value previousValue = NullValue.NULL;

    private Port(String name, DataType type, Value value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }

    public void setValue(Value value) {
        if (!value.isNull())
            if (!value.isType(type))
                throw new IllegalArgumentException(value + " is not " + type.toString());

        this.previousValue = this.value;
        this.value = value;
    }

    public Value getValue() {
        return value;
    }

    public Value getPreviousValue() {
        return previousValue;
    }

    public boolean hasValueChanged() {
        return !value.equals(previousValue);
    }

    public void writeToTag(NBTTagCompound tagCompound) {
        tagCompound.setString("Name", name);
        tagCompound.setByte("Type", (byte) type.ordinal());
        tagCompound.setTag("Value", DataType.getNbtTagFromValue(type, value));
        tagCompound.setTag("PreviousValue", DataType.getNbtTagFromValue(type, previousValue));
    }

    public void writeToByteBuf(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, name);
        buf.writeByte(type.ordinal());
        DataType.writeValueToByteBuf(type, value, buf);
        DataType.writeValueToByteBuf(type, previousValue, buf);
    }

    // Passthrough methods to Value, for convenience
    public Number getNumber() {
        return value.getNumber();
    }

    public int getInt() {
        return getNumber().intValue();
    }

    public double getDouble() {
        return getNumber().doubleValue();
    }

    public String getString() {
        return value.getString();
    }

    public Vec3d getVector() {
        return value.getVector();
    }

    public EntityId getEntity() {
        return value.getEntity();
    }
}
