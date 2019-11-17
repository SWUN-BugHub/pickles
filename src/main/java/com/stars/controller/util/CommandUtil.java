package com.stars.controller.util;

import com.stars.controller.protocol.inf.PicklesGameEvent;

public class CommandUtil {
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public PicklesGameEvent getEvent() {
        return event;
    }

    public void setEvent(PicklesGameEvent event) {
        this.event = event;
    }

    private long time;
    private PicklesGameEvent event;

    public boolean isExplode() {
        return isExplode;
    }

    public void setExplode(boolean explode) {
        isExplode = explode;
    }

    private boolean isExplode;
}
