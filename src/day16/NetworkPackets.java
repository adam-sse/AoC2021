package day16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NetworkPackets {
    
    private static int sumVersions(Packet packet) {
        int versionSum = packet.getVersion();
        
        for (Packet subPacket : packet.getSubpackets()) {
            versionSum += sumVersions(subPacket);
        }
        
        return versionSum;
    }
    
    public static void main(String[] args) throws IOException {
        Path input = Path.of("src/day16/input.txt");
        
        String hex = Files.lines(input).findFirst().orElseThrow();
        byte[] data = new byte[hex.length() / 2];
        for (int i = 0; i < data.length; i++) {
            data[i] =  (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        
//        for (byte b : data) {
//            System.out.print(String.format("%8s", Integer.toBinaryString(Byte.toUnsignedInt(b))).replace(' ', '0'));
//        }
//        System.out.println();

        Packet packet = new PacketDeserializer(data).readPacket();

//        System.out.println(packet);
        System.out.println(sumVersions(packet));
        System.out.println(packet.evaluate());
    }
    
}
