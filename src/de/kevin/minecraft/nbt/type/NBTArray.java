package de.kevin.minecraft.nbt.type;

public class NBTArray<T> extends NBTElement {
    private T value;
    private int type;

    public NBTArray(String name, Object value, int type) {
        super(name);
        this.type = type;
        this.value = (T) value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        switch (type) {
            case 0x7 -> {
                result.append("TAG_Byte_Array(").append(getName()).append("\"): ");
                for (byte b : ((byte[]) value))
                    result.append(b).append('\n');
            }

            case 0xB -> {
                result.append("TAG_Int_Array(").append(getName()).append("\"): ");
                for (int b : ((int[]) value))
                    result.append(b).append(' ');
            }

            case 0xC -> {
                result.append("TAG_Long_Array(").append(getName()).append("\"): ");
                for (long b : ((long[]) value))
                    result.append(b).append(' ');
            }
        }

        return result.toString();
    }
}
