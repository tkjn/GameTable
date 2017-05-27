package com.galactanet.gametable.events;

import java.util.ArrayList;
import java.util.List;

public class EventDispatcher {
    private List<IPogMenuRenderEventListener> pogMenuRenderListeners = new ArrayList<>();

    public void listenForPogMenuRender(IPogMenuRenderEventListener listener) {
        pogMenuRenderListeners.add(listener);
    }

    public void emitPogMenuRender(PogMenuRenderEvent event) {
        for (IPogMenuRenderEventListener listener : pogMenuRenderListeners) {
            listener.renderMenu(event);
        }
    }
}
