package me.dmillerw.io.circuit.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
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

        return new Port(name, type, value);
    }

    public static Port fromByteBuf(ByteBuf buf) {
        String name = ByteBufUtils.readUTF8String(buf);
        DataType type = DataType.values()[buf.readByte()];
        Value value = DataType.readValueFromByteBuf(type, buf);

        return new Port(name, type, value);
    }

    public final String name;
    public final DataType type;
    public Value value;

    private Port(String name, DataType type, Value value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public void setValue(Value value) {
        if (!value.isNull())
            if (!value.isType(type))
                throw new IllegalArgumentException(value + " is not " + type.toString());

        this.value = value;
    }

    public void writeToTag(NBTTagCompound tagCompound) {
        tagCompound.setString("Name", name);
        tagCompound.setByte("Type", (byte) type.ordinal());
        tagCompound.setTag("Value", DataType.getNbtTagFromValue(type, value));
    }

    public void writeToByteBuf(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, name);
        buf.writeByte(type.ordinal());
        DataType.writeValueToByteBuf(type, value, buf);
    }
}
