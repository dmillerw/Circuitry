package me.dmillerw.io.circuit.data;

import net.minecraft.util.math.Vec3d;

/**
 * @author dmillerw
 */
public class Value {

    public static Value of(DataType dataType) {
        return new Value(dataType, dataType.zero);
    }

    public static Value of(DataType dataType, Object value) {
        if (value == null)
            return NullValue.NULL;

        if (value instanceof Value) {
            if (((Value) value).isNull()) {
                return NullValue.NULL;
            }

            value = ((Value) value).value;
        }

        if (!dataType.isValid(value))
            throw new IllegalArgumentException(value + " can not be assigned to a value type of " + dataType);

        return new Value(dataType, value);
    }

    private final DataType dataType;
    private final Object value;

    public Value(DataType dataType, Object value) {
        this.dataType = dataType;
        this.value = value;
    }

    public DataType getType() {
        return dataType;
    }

    public boolean isType(DataType checkType) {
        return dataType == checkType;
    }

    public boolean isNull() {
        return false;
    }

    public Number getNumber() {
        return (Number) (isType(DataType.NUMBER) ? value : DataType.NUMBER.zero);
    }

    public String getString() {
        return (String) (isType(DataType.STRING) ? value : DataType.STRING.zero);
    }

    public Vec3d getVector() {
        return (Vec3d) (isType(DataType.VECTOR) ? value : DataType.VECTOR.zero);
    }

    public Integer getContainedEntityId() {
        return (Integer) (isType(DataType.ENTITY) ? value : DataType.ENTITY.zero);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Value value1 = (Value) object;

        if (dataType != value1.dataType) return false;
        return value.equals(value1.value);
    }

    @Override
    public int hashCode() {
        int result = dataType.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        if (value == null || isType(DataType.NULL))
            return "NULL";
        else if (isType(DataType.NUMBER))
            return getNumber().toString();
        else if (isType(DataType.STRING))
            return getString();
        else if (isType(DataType.VECTOR)) {
            Vec3d vector = getVector();
            return "(" + vector.x + ", " + vector.y + ", " + vector.z + ")";
        } else {
            return value.toString();
        }
    }
}
