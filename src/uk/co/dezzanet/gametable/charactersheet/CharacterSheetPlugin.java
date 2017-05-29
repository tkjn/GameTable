package uk.co.dezzanet.gametable.charactersheet;

import com.galactanet.gametable.GametableFrame;
import com.galactanet.gametable.IGametablePlugin;
import com.galactanet.gametable.ILeftPanelProvider;

import javax.swing.*;

public class CharacterSheetPlugin implements IGametablePlugin, ILeftPanelProvider {

    private static final String PLUGIN_NAME = "Character sheet";
    private static final String PANEL_TITLE = "Character Sheet";
    private CharacterSheetPanel panel;

    @Override
    public void initialise(GametableFrame gametable) {
        panel = getCharacterSheetPanel();
        gametable.registerLeftPanelProvider(this);
        gametable.registerAutoSaveListener(new AutoSaveListener(panel.getStorage()));
        gametable.getEventDispatcher().listenForPogMenuRender(new PogMenuRenderListener(panel.getPogAdapter()));
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public JPanel getLeftPanel() {
        return getCharacterSheetPanel();
    }

    @Override
    public String getPanelTitle() {
        return PANEL_TITLE;
    }

    private CharacterSheetPanel getCharacterSheetPanel() {
        if (panel == null) {
            panel = new CharacterSheetPanel();
        }
        return panel;
    }
}
