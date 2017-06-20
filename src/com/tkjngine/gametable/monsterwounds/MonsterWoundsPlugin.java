package com.tkjngine.gametable.monsterwounds;

import com.galactanet.gametable.GametableFrame;
import com.galactanet.gametable.IGametablePlugin;
import com.galactanet.gametable.ILeftPanelProvider;

import javax.swing.*;

public class MonsterWoundsPlugin implements IGametablePlugin, ILeftPanelProvider {

    private static final String PLUGIN_NAME = "Monster Wounds";
    private static final String PANEL_TITLE = "Monster Wounds";
    //private MonsterWoundsPanel panel;

    @Override
    public void initialise(GametableFrame gametable) {
        //panel = getCharacterSheetPanel();
        //gametable.registerLeftPanelProvider(this);
        //gametable.registerAutoSaveListener(new AutoSaveListener(panel.getStorage()));
        gametable.getEventDispatcher().listenForPogMenuRender(new PogMenuRenderListener());
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public JPanel getLeftPanel() {
        //return getCharacterSheetPanel();
        return new JPanel();
    }

    @Override
    public String getPanelTitle() {
        return PANEL_TITLE;
    }

    /*private CharacterSheetPanel getCharacterSheetPanel() {
        if (panel == null) {
            panel = new CharacterSheetPanel();
        }
        return panel;
    }*/
}
