package de.kevin.minecraft.nbt;

import de.kevin.minecraft.binary.ByteReader;
import de.kevin.minecraft.nbt.type.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NBTReader extends ByteReader {

    public NBTReader(byte[] data) {
        super(Endian.BIG, data);
    }

    private NBTList readList() {
        List<NBTElement> elements = new ArrayList<>();

        String name = readName();
        byte typeID = readByte();
        int length = readInt();

        Supplier<NBTElement> value = switch (typeID) {
            case 0xA -> () -> readCompound("");
            case 0x1 -> () -> new NBTPrimitive<Byte>(readByte(), 0x1);
            case 0x2 -> () -> new NBTPrimitive<Short>(readShort(), 0x2);
            case 0x3 -> () -> new NBTPrimitive<Integer>(readInt(), 0x3);
            case 0x4 -> () -> new NBTPrimitive<Long>(readLong(), 0x4);
            case 0x5 -> () -> new NBTPrimitive<Float>(readFloat(), 0x5);
            case 0x6 -> () -> new NBTPrimitive<Double>(readDouble(), 0x6);
            case 0x8 -> () -> new NBTPrimitive<String>(new String(readBytes(readUnsignedShort())), 0x8); // readShort -> String length
            case 0x9 -> this::readList;
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

        return new NBTPrimitive<>(name, value, type);
    }

    public NBTCompound parse() {
        byte type = readByte();
        String name = readName();
        assert type == 0xA;

        return readCompound(name);
    }

    private NBTArray readArray(int type) {
        String name = readName();
        int length = readInt();
        if (length < 1)
            return new NBTArray(name, new byte[]{}, type);

        Object result = null;
        switch (type) {
            case 0x7 -> result = readBytes(length);
            case 0xB -> result = readIntArray(length);
            case 0xC -> result = readLongArray(length);
        }

        return new NBTArray(name, result, type);
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

            // Read array
            switch (nextType) {
                case 0x07, 0xB, 0xC -> {
                    result.add(readArray(nextType));
                    continue;
                }
            }

            // Read tag
            NBTPrimitive<Object> objectNBTPrimitive = readTag(readName(), nextType);
            result.add(objectNBTPrimitive);
        }

        return new NBTCompound(name, result);
    }
}
