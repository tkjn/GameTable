package uk.co.dezzanet.gametable.charactersheet;

import com.galactanet.gametable.IAutoSaveListener;

public class AutoSaveListener implements IAutoSaveListener {

    private CharacterDataStorage storage;

    public AutoSaveListener(CharacterDataStorage storage) {
        this.storage = storage;
    }

    @Override
    public void autoSave() {
        storage.autoSave();
    }
}
