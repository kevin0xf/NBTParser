package de.kevin.minecraft.nbt.type;

public abstract class NBTElement {
    private final String name;

    public NBTElement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String toString();
}
