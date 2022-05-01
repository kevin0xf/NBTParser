package de.kevin.minecraft.nbt.type;

import java.util.List;

public class NBTCompound extends NBTElement {

    private final List<NBTElement> elements;

    public NBTCompound(String name, List<NBTElement> elements) {
        super(name);
        this.elements = elements;
    }

    public List<NBTElement> getElements() {
        return elements;
    }
}
