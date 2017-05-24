package com.galactanet.gametable.events;

import com.galactanet.gametable.Pog;

import javax.swing.JPopupMenu;

public class PogMenuRenderEvent {
    private final String menuName;
    private final JPopupMenu menu;
    private final Pog activePog;

    public PogMenuRenderEvent(String menuName, JPopupMenu menu, Pog activePog)
    {
        this.menuName = menuName;
        this.menu = menu;
        this.activePog = activePog;
    }

    public String getMenuName() {
        return menuName;
    }

    public JPopupMenu getMenu() {
        return menu;
    }

    public Pog getActivePog() {
        return activePog;
    }
}
