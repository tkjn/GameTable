package uk.co.dezzanet.gametable.charactersheet;

import java.awt.*;

import javax.swing.*;

public class ToHitTableView extends JPanel implements ICharacterDataChangedListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterData character_data;
	private GridBagLayout layout = new GridBagLayout();
	private JLabel[] toHitFoeLabels = new JLabel[10];
	private JLabel[] enemyRequiresLabels = new JLabel[10];
	private ToHitModel toHitModel = new ToHitModel();
	
	public ToHitTableView(CharacterData data) {
		character_data = data;
		data.addListener(this);
		init();
	}
	
	public void init() {
		setLayout(layout);
		
		// Enemy WS line - static
		JLabel enemy_ws = new JLabel("Enemy's WS");
		GridBagConstraints enemy_ws_con = new GridBagConstraints();
		enemy_ws_con.gridx = 0;
		enemy_ws_con.gridy = 0;
		enemy_ws_con.gridwidth = 2;
		enemy_ws_con.fill = GridBagConstraints.HORIZONTAL; // Force it to fill the grid square
		enemy_ws.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		add(enemy_ws, enemy_ws_con);
		
		for (int i = 1; i <= 10; ++i) {
			JLabel label = new JLabel(String.valueOf(i));
			GridBagConstraints con = new GridBagConstraints();
			con.gridx = GridBagConstraints.RELATIVE;
			con.gridy = 0;
			con.ipadx = 15;
			label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
			add(label, con);
		}
		
		// To Hit Foe line
		JLabel to_hit_foe = new JLabel("To hit foe:");
		GridBagConstraints to_hit_foe_con = new GridBagConstraints();
		to_hit_foe_con.gridx = 0;
		to_hit_foe_con.gridy = 1;
		to_hit_foe_con.gridwidth = 2;
		to_hit_foe_con.fill = GridBagConstraints.HORIZONTAL;
		add(to_hit_foe, to_hit_foe_con);
		for (int i = 0; i < 10; ++i) {
			toHitFoeLabels[i] = new JLabel("?");
			Font f = toHitFoeLabels[i].getFont(); 
			toHitFoeLabels[i].setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
			GridBagConstraints con = new GridBagConstraints();
			con.gridx = GridBagConstraints.RELATIVE;
			con.gridy = 1;
			con.ipadx = 15;
			add(toHitFoeLabels[i], con);
		}
		
		// Enemy Requires line
		JLabel enemy_requires = new JLabel("Enemy requires:");
		GridBagConstraints enemy_requires_con = new GridBagConstraints();
		enemy_requires_con.gridx = 0;
		enemy_requires_con.gridy = 2;
		enemy_requires_con.gridwidth = 2;
		enemy_requires_con.ipadx = 5;
		add(enemy_requires, enemy_requires_con);
		for (int i = 0; i < 10; ++i) {
			enemyRequiresLabels[i] = new JLabel("?");
			Font f = enemyRequiresLabels[i].getFont(); 
			enemyRequiresLabels[i].setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
			GridBagConstraints con = new GridBagConstraints();
			con.gridx = GridBagConstraints.RELATIVE;
			con.gridy = 2;
			con.ipadx = 15;
			add(enemyRequiresLabels[i], con);
		}
		
		dataChanged();
	}
	
	public void buildToHitFoeLine() {
    	try {
    		int[] toHitData = toHitModel.getToHitForWS(character_data.getWeaponSkill());
    		for (int i = 0; i < 10; ++i) {
    			toHitFoeLabels[i].setText(String.valueOf(toHitData[i]));
    		}
    	}
    	catch (IllegalArgumentException e) {
    		for (int i = 0; i < 10; ++i) {
    			toHitFoeLabels[i].setText("?");
    		}
    	}
	}
	
	public void buildEnemyRequiresLine() {
    	try {
    		int[] defendData = toHitModel.getToHitForAttacker(character_data.getWeaponSkill());
    		for (int i = 0; i < 10; ++i) {
    			enemyRequiresLabels[i].setText(String.valueOf(defendData[i]));
    		}
    	}
    	catch (IllegalArgumentException e) {
    		for (int i = 0; i < 10; ++i) {
    			enemyRequiresLabels[i].setText("?");
    		}
    	}
	}

	public void dataChanged() {
		buildToHitFoeLine();
		buildEnemyRequiresLine();
	}
}
