package de.kevin.minecraft.binary;

public class ByteReader {
    public enum Endian {
        BIG, SMALL;
    }

    private Endian endian;
    private final byte[] data;
    private int index = 0;

    public ByteReader(Endian endian, byte[] data) {
        this.endian = endian;
        this.data = data;
    }

    public byte readByte() {
        return data[index++];
    }

    public byte[] readBytes(int count) {
        byte[] bytes = new byte[count];
        for (int i = 0; i < count; i++) {
            bytes[i] = data[index++];
        }

        return bytes;
    }

    public long readLong() {
        long result = 0;
        byte[] bytes = readBytes(8);

        if (endian == Endian.BIG) {
            for (byte aByte : bytes) result |= aByte;
        } else {
            for (int i = 0; i < bytes.length; i++)
                result |= (long) bytes[i] << 56 - 8 * i;
        }

        return result;
    }

    private short toUnsigned(byte b) {
        short result = 0;
        result |= (b & 0x7F);
        if (((b & (1 << 7)) >> 7) == 1) {
            result += 128;
        }

        return result;
    }

    public int readUnsignedShort() {
        short a = toUnsigned(readByte());
        short b = toUnsigned(readByte());

        int result = 0;
        if (endian == Endian.BIG) {
            result = (a << 8) | b;
        } else result = a | (b << 8);

        return (a << 8) | b;
    }

    public int readInt() {
        int result = 0;
        byte[] bytes = readBytes(4);

        if (endian == Endian.BIG) {
            for (int i = 0; i < bytes.length; i++)
                result |= bytes[i];
        } else {
            int len = bytes.length - 1;
            for (int i = len; i >= 0; i--)
                result |= bytes[i] << 8 * (len - i);
        }

        return result;
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public short readShort() {
        short result = 0;

        if (endian == Endian.BIG) {
            result = (short) ((readByte() << 8) | readByte());
        } else result = (short) (readByte() | (readByte() << 8));

        return result;
    }

    public void setEndian(Endian endian) {
        this.endian = endian;
    }
}
