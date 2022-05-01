import de.kevin.minecraft.nbt.NBTReader;
import de.kevin.minecraft.nbt.type.NBTCompound;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        File file = new File("resources/servers.dat");
        FileInputStream fileInputStream = null;
        byte[] bytes = null;
        try {
            fileInputStream = new FileInputStream(file);
            bytes = fileInputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        NBTReader nbtReader = new NBTReader(bytes);
        NBTCompound nbtCompound = nbtReader.parse();

    }
}
