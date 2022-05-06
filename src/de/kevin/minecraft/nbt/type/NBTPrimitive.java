package de.kevin.minecraft.nbt.type;

public class NBTPrimitive<T> extends NBTElement {
    private final int type;
    private final T value;

    public NBTPrimitive(String name, Object value, int type) {
        super(name);
        this.type = type;
        this.value = (T) value;
    }

    public NBTPrimitive(Object value, int type) {
        this(null, value, type);
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String value = switch (type) {
            case 0x01 -> "Byte";
            case 0x02 -> "Short";
            case 0x03 -> "Int";
            case 0x04 -> "Long";
            case 0x05 -> "Float";
            case 0x06 -> "Double";
            case 0x08 -> "String";
            default -> "";
        };

        result.append("TAG_").append(value).append("(\"").append(getName()).append("\"): ").append(this.value);
        return result.toString();
    }
}
