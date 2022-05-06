import de.kevin.minecraft.nbt.NBTReader;
import de.kevin.minecraft.nbt.type.*;

import java.io.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class Main {

    public static void main(String[] args) {
        File file = new File("resources/bigtest.nbt");

        byte[] bytes = null;
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(file));
            bytes = gzipInputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        StringBuilder sb = new StringBuilder();
//        for(int i=0; i<bytes.length;i++) {
//            sb.append(String.format("0x%02x",bytes[i])).append(" ").append((char) bytes[i]).append('\n');
//        }
//        System.out.println(sb);

        NBTReader nbtReader = new NBTReader(bytes);
        NBTCompound nbtCompound = nbtReader.parse();
        HashMap<String, NBTElement> collect = nbtCompound.getElements().stream().collect(Collector.of(HashMap::new, (h, r) -> h.put(r.getName(), r), (h, r) -> h));
        System.out.println(nbtCompound);
    }
}
