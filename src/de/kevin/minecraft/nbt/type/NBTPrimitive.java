package de.kevin.minecraft.nbt.type;

public class NBTPrimitive<T> extends NBTElement {
    private final T value;

    public NBTPrimitive(String name, Object value) {
        super(name);
        this.value = (T) value;
    }

    public T getValue() {
        return value;
    }
}
