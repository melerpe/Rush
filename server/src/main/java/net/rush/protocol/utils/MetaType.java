package net.rush.protocol.utils;

public enum MetaType {
    BYTE(0),
    SHORT(1),
    INT(2),
    FLOAT(3),
    STRING(4),
    ITEM(5),
    POSITION(6);

    private static final MetaType[] by_id;

    private final int id;

    MetaType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MetaType fromId(int id) {
        return by_id[id];
    }

    static {
        int highest = 0;
        for (MetaType t : MetaType.values()) {
            if (t.getId() > highest)
                highest = t.getId();
        }
        by_id = new MetaType[highest + 1];
        for (MetaType t : MetaType.values())
            by_id[t.getId()] = t;
    }
}
