package uk.co.dezzanet.gametable.charactersheet;

import java.util.HashMap;

import com.galactanet.gametable.GametableFrame;
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
		String weapon_skill = String.valueOf(character_data.getWeaponSkill());
		
		HashMap<String, String> map = new HashMap<String, String>();
		if ( ! wounds.equals(current_pog.getAttribute("Wounds"))) {
			map.put("Wounds", wounds);
		}
		if ( ! weapon_skill.equals(current_pog.getAttribute("Weapon Skill"))) {
			map.put("Weapon Skill", weapon_skill);
		}
		if ( ! gold.equals(current_pog.getAttribute("Gold"))) {
			map.put("Gold", gold);
		}
		
		GametableFrame.getGametableFrame().getGametableCanvas().setPogData(current_pog.getId(), null, map, null);
		
	}

	public void dataChanged() {
		if (current_pog != null) {
			refreshPog();
		}
	}
	
}
