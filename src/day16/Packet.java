package day16;

import java.util.Collections;
import java.util.List;

public abstract class Packet {

    private int version;
    
    private int type;
    
    public Packet(int version, int type) {
        this.version = version;
        this.type = type;
    }
    
    public void addSubPacket(Packet packet) {
        throw new UnsupportedOperationException();
    }
    
    public List<Packet> getSubpackets() {
        return Collections.emptyList();
    }
    
    public int getVersion() {
        return version;
    }
    
    public int getType() {
        return type;
    }
    
    public abstract long evaluate();
    
}
