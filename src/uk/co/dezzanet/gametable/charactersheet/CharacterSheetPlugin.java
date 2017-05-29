package uk.co.dezzanet.gametable.charactersheet;

import com.galactanet.gametable.GametableFrame;
import com.galactanet.gametable.IGametablePlugin;
import com.galactanet.gametable.ILeftPanel;

import javax.swing.*;

public class CharacterSheetPlugin implements IGametablePlugin, ILeftPanel {

    private static final String PLUGIN_NAME = "Character sheet";
    private static final String PANEL_TITLE = "Character Sheet";
    private CharacterSheetPanel panel;

    @Override
    public void initialise(GametableFrame gametable) {
        panel = new CharacterSheetPanel();
        gametable.addPanelToLeftPane(panel, PANEL_TITLE);
        gametable.registerAutoSaveListener(new AutoSaveListener(panel.getStorage()));
        gametable.getEventDispatcher().listenForPogMenuRender(new PogMenuRenderListener(panel.getPogAdapter()));
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public JPanel getLeftPanel() {
        return panel;
    }
}
