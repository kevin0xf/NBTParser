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

        if (endian == Endian.SMALL) {
            byte[] bytesChan = new byte[bytes.length];
            for (int i = bytes.length - 1; i >= 0; i--)
                bytesChan[bytes.length - 1 - i] = bytes[i];
            return bytesChan;
        }

        return bytes;
    }

    private short toUnsigned(byte b) {
        short result = 0;
        result |= (b & 0x7F);
        if (((b & (1 << 7)) >> 7) == 1) {
            result += 128;
        }

        return result;
    }

    public int readInt() {
        int result = 0;
        byte[] bytes = readBytes(4);

        for (int i = 0; i < bytes.length; i++)
            result |= bytes[i] << 8 * 3 - 8 * i;

        return result;
    }

    public long readLong() {
        long result = 0;
        byte[] bytes = readBytes(8);
        for (int i = 0; i < bytes.length; i++) {
            result |= (long) bytes[i] << 8 * 7 - 8 * i;
        }

        return result;
    }

    public short readShort() {
        byte[] bytes = readBytes(2);
        short result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result |= bytes[i] << 8 - 8 * i;
        }

        return result;
    }

    public int readUnsignedShort() {
        short a = toUnsigned(readByte());
        short b = toUnsigned(readByte());

        return (a << 8) | b;
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public void setEndian(Endian endian) {
        this.endian = endian;
    }
}
