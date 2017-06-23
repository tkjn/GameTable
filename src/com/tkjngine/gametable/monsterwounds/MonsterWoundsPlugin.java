package com.tkjngine.gametable.monsterwounds;

import com.galactanet.gametable.GametableFrame;
import com.galactanet.gametable.IGametablePlugin;

import javax.swing.*;

public class MonsterWoundsPlugin implements IGametablePlugin {

    private static final String PLUGIN_NAME = "Monster Wounds";

    @Override
    public void initialise(GametableFrame gametable) {
        gametable.getEventDispatcher().listenForPogMenuRender(new PogMenuRenderListener());
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }
}
