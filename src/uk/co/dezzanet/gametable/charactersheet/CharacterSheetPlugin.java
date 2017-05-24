package uk.co.dezzanet.gametable.charactersheet;

import com.galactanet.gametable.GametableFrame;
import com.galactanet.gametable.IGametablePlugin;

public class CharacterSheetPlugin implements IGametablePlugin {

    private static final String PLUGIN_NAME = "Character sheet";
    private static final String PANEL_TITLE = "Character Sheet";

    @Override
    public void initialise(GametableFrame gametable) {
        CharacterSheetPanel panel = new CharacterSheetPanel();
        gametable.addPanelToLeftPane(panel, PANEL_TITLE);
        gametable.registerAutoSaveListener(new AutoSaveListener(panel.getStorage()));
        gametable.getEventDispatcher().listenForPogMenuRender(new PogMenuRenderListener(panel.getPogAdapter()));
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }
}
