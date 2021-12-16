package day16;


public class LiteralPacket extends Packet {

    public static final int TYPE_LITERAL = 4;
    
    private long value;
    
    public LiteralPacket(int version, int type, long value) {
        super(version, type);
        this.value = value;
    }
    
    public long getValue() {
        return value;
    }
    
    @Override
    public long evaluate() {
        return value;
    }
    
    @Override
    public String toString() {
        return "Literal(ver=" + getVersion() + ",type=" + getType() + ",value=" + value + ")";
    }

}
