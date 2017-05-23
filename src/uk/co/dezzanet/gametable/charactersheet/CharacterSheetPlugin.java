package uk.co.dezzanet.gametable.charactersheet;

import com.galactanet.gametable.GametableFrame;
import com.galactanet.gametable.IGametablePlugin;

public class CharacterSheetPlugin implements IGametablePlugin {

    private static final String PLUGIN_NAME = "Character sheet";

    @Override
    public void initialise(GametableFrame gametable) {
        throw new RuntimeException("loading failed");
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }
}
