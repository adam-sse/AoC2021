package day16;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OperatorPacket extends Packet {

    public static final int TYPE_SUM = 0;
    public static final int TYPE_PRODUCT = 1;
    public static final int TYPE_MINIMUM = 2;
    public static final int TYPE_MAXIMUM = 3;
    public static final int TYPE_GREATER_THAN = 5;
    public static final int TYPE_LESS_THAN = 6;
    public static final int TYPE_EQUAL_TO = 7;
    
    private static Map<Integer, String> TYPE_STR = Map.of(
        TYPE_SUM, "+",
        TYPE_PRODUCT, "*",
        TYPE_MINIMUM, "min",
        TYPE_MAXIMUM, "max",
        TYPE_GREATER_THAN, ">",
        TYPE_LESS_THAN, "<",
        TYPE_EQUAL_TO, "="
    );
    
    private List<Packet> subpackets;
    
    public OperatorPacket(int version, int type) {
        super(version, type);
        subpackets = new LinkedList<>();
    }
    
    @Override
    public void addSubPacket(Packet packet) {
        subpackets.add(packet);
    }
    
    @Override
    public List<Packet> getSubpackets() {
        return Collections.unmodifiableList(subpackets);
    }
    
    @Override
    public long evaluate() {
        switch (getType()) {
        case TYPE_SUM:
            return subpackets.stream().mapToLong(Packet::evaluate).sum();
        case TYPE_PRODUCT:
            return subpackets.stream().mapToLong(Packet::evaluate).reduce(1, (a, b) -> a * b);
        case TYPE_MINIMUM:
            return subpackets.stream().mapToLong(Packet::evaluate).min().orElseThrow();
        case TYPE_MAXIMUM:
            return subpackets.stream().mapToLong(Packet::evaluate).max().orElseThrow();
            
        case TYPE_GREATER_THAN:
            return subpackets.get(0).evaluate() > subpackets.get(1).evaluate() ? 1 : 0;
        case TYPE_LESS_THAN:
            return subpackets.get(0).evaluate() < subpackets.get(1).evaluate() ? 1 : 0;
        case TYPE_EQUAL_TO:
            return subpackets.get(0).evaluate() == subpackets.get(1).evaluate() ? 1 : 0;
        }
        
        throw new IllegalStateException("type: " + getType());
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Operator(ver=").append(getVersion()).append(",type=").append(TYPE_STR.get(getType())).append(")\n");
        
        for (Packet subPacket : subpackets) {
            String[] lines = subPacket.toString().split("\n");
            for (String line : lines) {
                sb.append("    ").append(line).append('\n');
            }
        }
        
        return sb.toString();
    }

}
