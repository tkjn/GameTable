package uk.co.dezzanet.gametable.charactersheet;

import com.galactanet.gametable.Pog;

public class PogAdapter implements ICharacterDataChangedListener {
	
	private Pog current_pog;
	private CharacterData character_data;
	
	public PogAdapter(CharacterData data) {
		character_data = data;
		character_data.addListener(this);
	}
	
	public void setCurrentPog(Pog target_pog) {
		current_pog = target_pog;
		refreshPog();
	}
	
	public void refreshPog() {
		if (current_pog == null) {
			return;
		}
		String wounds = String.valueOf(character_data.getWounds()) + " / " + String.valueOf(character_data.getMaxWounds());
		String gold = String.valueOf(character_data.getGold());
		
		current_pog.setAttribute("Wounds", wounds);
		current_pog.setAttribute("Gold", gold);
	}

	public void dataChanged() {
		if (current_pog != null) {
			refreshPog();
		}
	}
	
}
