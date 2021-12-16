package day16;


public class PacketDeserializer {

    private byte[] data;
    
    private int ptr;
    
    public PacketDeserializer(byte[] data) {
        this.data = data;
        this.ptr = 0;
    }
    
    public Packet readPacket() {
        int version = readNBits(3);
        int type = readNBits(3);
        
        Packet result;
        if (type == LiteralPacket.TYPE_LITERAL) {
            result = readLiteralPacket(version, type);
        } else {
            result = readOperatorPacket(version, type);
        }
        return result;
    }
    
    private LiteralPacket readLiteralPacket(int version, int type) {
        int firstBit;
        long value = 0;
        do {
            firstBit = readNBits(1);
            value = (value << 4) | readNBits(4);
        } while (firstBit == 1);
        
        return new LiteralPacket(version, type, value);
    }
    
    private OperatorPacket readOperatorPacket(int version, int type) {
        OperatorPacket packet = new OperatorPacket(version, type);
        
        int lengthTypeId = readNBits(1);
        
        if (lengthTypeId == 0) {
            int subPacketLength = readNBits(15);
            int subPacketEnd = ptr + subPacketLength;
            while (ptr < subPacketEnd) {
                packet.addSubPacket(readPacket());
            }
            
        } else {
            int numberSubPackets = readNBits(11);
            
            for (int i = 0; i < numberSubPackets; i++) {
                packet.addSubPacket(readPacket());
            }
        }
        
        return packet;
    }
    
    private int readNBits(int n) {
        int result = 0;
        for (int i = 0; i < n; i++, ptr++) {
            result |= getBit() << (n - i - 1);
        }
        return result;
    }
    
    private int getBit() {
        int index = ptr / 8;
        int offset = 7 - (ptr % 8);
        return (data[index] & (1 << offset)) != 0 ? 1 : 0;
    }
    
}
