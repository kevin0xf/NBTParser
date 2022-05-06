package de.kevin.minecraft.nbt.type;

import java.util.List;

public class NBTList extends NBTElement {
    private final List<NBTElement> elements;

    public NBTList(String name, List<NBTElement> elements) {
        super(name);
        this.elements = elements;
    }

    public List<?> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("TAG_List(\"").append(getName()).append("\"): \n{\n");
        elements.forEach(e -> result.append('\t').append(e).append('\n'));
        result.append("}\n");

        return result.toString();
    }
}
