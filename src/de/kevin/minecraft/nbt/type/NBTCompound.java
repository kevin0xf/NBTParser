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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("TAG_Compound(\"").append(getName()).append("\"): \n{\n");
        elements.forEach(e -> {
            StringBuilder res = new StringBuilder();
            for (String s : e.toString().split("\n")) {
                res.append('\t').append(s).append("\n");
            }

            result.append(res);
        });
        result.append("}\n");

        return result.toString();
    }
}
