package de.kevin.minecraft.nbt.type;

import java.util.List;

public class NBTList extends NBTElement {
    private final List<?> elements;

    public NBTList(String name, List<?> elements) {
        super(name);
        this.elements = elements;
    }

    public List<?> getElements() {
        return elements;
    }
}
