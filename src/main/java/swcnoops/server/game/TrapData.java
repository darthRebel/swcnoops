package swcnoops.server.game;

public class TrapData implements BuildableData {
    final String uid;
    private Long rearmTime;
    private TrapEventType eventType;
    private String eventData;

    public TrapData(String uid) {
        this.uid = uid;
    }

    @Override
    public String getUid() {
        return uid;
    }

    public Long getRearmTime() {
        return rearmTime;
    }

    public void setRearmTime(Long rearmTime) {
        this.rearmTime = rearmTime;
    }

    public TrapEventType getEventType() {
        return eventType;
    }

    public void setEventType(TrapEventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public long getBuildingTime() {
        if (getRearmTime() != null)
            return getRearmTime().longValue();

        return 0;
    }

    @Override
    public int getSize() {
        return 0;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }
}
