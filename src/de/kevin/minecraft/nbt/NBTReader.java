package de.kevin.minecraft.nbt;

import de.kevin.minecraft.binary.ByteReader;
import de.kevin.minecraft.nbt.type.NBTCompound;
import de.kevin.minecraft.nbt.type.NBTElement;
import de.kevin.minecraft.nbt.type.NBTList;
import de.kevin.minecraft.nbt.type.NBTPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NBTReader extends ByteReader {

    public NBTReader(byte[] data) {
        super(Endian.BIG, data);
    }

    private NBTList readList() {
        List<Object> elements = new ArrayList<>();

        String name = readName();
        byte typeID = readByte();
        int length = readInt();

        Supplier<Object> value = switch (typeID) {
            case 0xA -> () -> readCompound("");
            case 0x01 -> this::readByte;
            case 0x02 -> this::readShort;
            case 0x03 -> this::readInt;
            case 0x04 -> this::readLong;
            case 0x05 -> this::readFloat;
            case 0x06 -> this::readDouble;
            case 0x08 -> () -> new String(readBytes(readUnsignedShort())); // readShort -> String length
            case 0x09 -> this::readList;
            default -> throw new IllegalStateException("Unexpected value: " + typeID);
        };

        for (int i = 0; i < length; i++) {
            elements.add(value.get());
        }

        return new NBTList(name, elements);
    }

    private String readName() {
        return new String(readBytes(readShort()));
    }

    private <T> NBTPrimitive<T> readTag(String name, byte type) {
        Object value = switch (type) {
            case 0x01 -> readByte();
            case 0x02 -> readShort();
            case 0x03 -> readInt();
            case 0x04 -> readLong();
            case 0x05 -> readFloat();
            case 0x06 -> readDouble();
            case 0x08 -> new String(readBytes(readUnsignedShort())); // readShort -> String length
            default -> -1;
        };

        return new NBTPrimitive<>(name, value);
    }

    public NBTCompound parse() {
        byte type = readByte();
        String name = readName();
        assert type == 0xA;
        assert name.isEmpty();

        return readCompound(name);
    }

    private NBTCompound readCompound(String name) {
        List<NBTElement> result = new ArrayList<>();

        byte nextType;
        while ((nextType = readByte()) != 0x00) {

            // Read compound
            if (nextType == 0xA) {
                result.add(readCompound(readName()));
                continue;
            }

            // Read list
            if (nextType == 0x09) {
                result.add(readList());
                continue;
            }

            switch (nextType) {
                case 0x07 -> {}
                case 0xB -> {}
                case 0xC -> {}
            }
            // Read tag
            NBTPrimitive<Object> objectNBTPrimitive = readTag(readName(), nextType);
            result.add(objectNBTPrimitive);
        }

        return new NBTCompound(name, result);
    }
}
