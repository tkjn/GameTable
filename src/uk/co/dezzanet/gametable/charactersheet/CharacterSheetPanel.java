package uk.co.dezzanet.gametable.charactersheet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.galactanet.gametable.GametableFrame;

public class CharacterSheetPanel extends JPanel implements ICharacterDataChangedListener {

	private static final long serialVersionUID = -8857859513490491696L;
	
	private static CharacterData characterData = new CharacterData();
	
	private CharacterDataStorage storage = new CharacterDataStorage();

	private JSpinner wounds;

	private JSpinner max_wounds;
	
	private boolean doing_update = false;
	private boolean updating_wounds = false;
	private boolean updating_max_wounds = false;
	private boolean updating_notes = false;
	private boolean updating_gold = false;
	private boolean updating_ws = false;

	private JTextArea notes;

	private PogAdapter pog_adapter;

	private JSpinner gold;

	private GametableFrame frame;

	private JSpinner weapon_skill;
	
	private JLabel toHitTable;
	
	/**
	 * This is the default constructor
	 */
	public CharacterSheetPanel() {
		initialise();
		frame = GametableFrame.getGametableFrame();
		JMenuBar menubar = frame.getJMenuBar();
		menubar.add(getMenu());
	}
	
	/**
	 * This method initialises this
	 * 
	 * @return void
	 */
	private void initialise() {
		
		pog_adapter = new PogAdapter(characterData);
		
		setMaximumSize(new Dimension(32768, 31));
		setMinimumSize(new Dimension(100, 31));

		final SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		characterData.addListener(this);
		
		final JLabel wounds_label = new JLabel("Wounds");
		add(wounds_label);
		
		SpinnerModel model = new SpinnerNumberModel(0, 0, 200, 1);
		wounds = new JSpinner(model);
		wounds.addChangeListener(new WoundsChangeListener());
		add(wounds);
		
		Dimension wounds_size = wounds.getPreferredSize();
		
		final JLabel max_wounds_label = new JLabel("Max");
		add(max_wounds_label);
		
		SpinnerModel max_wounds_model = new SpinnerNumberModel(0, 0, 200, 1);
		max_wounds = new JSpinner(max_wounds_model);
		max_wounds.addChangeListener(new MaxWoundsChangeListener());
		add(max_wounds);
		
		// Weapon Skill
		JLabel ws_label = new JLabel("WS");
		add(ws_label);
		
		SpinnerModel ws_model = new SpinnerNumberModel(1, 1, 10, 1);
		weapon_skill = new JSpinner(ws_model);
		weapon_skill.addChangeListener(new WeaponSkillChangeListener());
		add(weapon_skill);
		
		// Gold
		final JLabel gold_label = new JLabel("Gold");
		add(gold_label);
		
		SpinnerModel gold_model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
		gold = new JSpinner(gold_model);
		gold.addChangeListener(new GoldChangeListener());
		Dimension gold_size = gold.getPreferredSize();
		gold_size.width = 70;
		gold.setPreferredSize(gold_size);
		add(gold);
		
		final JButton sub_gold = new JButton("-");
		sub_gold.setPreferredSize(wounds_size);
		sub_gold.setFont(new Font("Ariel", Font.PLAIN, 8));
		sub_gold.setMargin(new Insets(1,1,1,1));
		sub_gold.addActionListener(new SubGoldActionListener());
		add(sub_gold);
		
		final JButton plus_gold = new JButton("+");
		plus_gold.setPreferredSize(wounds_size);
		plus_gold.setFont(new Font("Ariel", Font.PLAIN, 8));
		plus_gold.addActionListener(new PlusGoldActionListener());
		plus_gold.setMargin(new Insets(1,1,1,1));
		add(plus_gold);
		
		toHitTable = new JLabel(getToHitString());
		add(toHitTable);
		
		// Notes
		final JLabel note_label = new JLabel("Notes");
		add(note_label);
		
		notes = new JTextArea(10, 30);
		notes.setLineWrap(true);
		notes.setWrapStyleWord(true);
		notes.getDocument().addDocumentListener(new NotesDocumentListener());
		
		JScrollPane notes_scroller = new JScrollPane(notes, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		add(notes_scroller, BorderLayout.CENTER);		
		
		//layout.getConstraints(panel).setX(Spring.constant(2));
		//layout.getConstraints(panel).setY(Spring.constant(2));
		
		// Wounds label should start at 5x5 from the top left
		layout.putConstraint(SpringLayout.WEST, wounds_label, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, wounds_label, 5, SpringLayout.NORTH, this);
		
		// Wounds field should be just below the wounds label and 5px from the edge
		layout.putConstraint(SpringLayout.NORTH, wounds, 5, SpringLayout.SOUTH, wounds_label);
		layout.putConstraint(SpringLayout.WEST, wounds, 5, SpringLayout.WEST, this);
		
		// Max Wounds Label should be 5 from the top and 5 to the left of wounds label
		layout.putConstraint(SpringLayout.WEST, max_wounds_label, 5, SpringLayout.EAST, wounds_label);
		layout.putConstraint(SpringLayout.NORTH, max_wounds_label, 5, SpringLayout.NORTH, this);
		
		// Max Wounds field should be and same horz. as wounds label and 5 below max wounds label
		layout.putConstraint(SpringLayout.WEST, max_wounds, 0, SpringLayout.WEST, max_wounds_label);
		layout.putConstraint(SpringLayout.NORTH, max_wounds, 5, SpringLayout.SOUTH, max_wounds_label);
		
		// WS Label should be 5 from the top and 5 to the left of max wounds
		layout.putConstraint(SpringLayout.WEST, ws_label, 5, SpringLayout.EAST, max_wounds);
		layout.putConstraint(SpringLayout.NORTH, ws_label, 5, SpringLayout.NORTH, this);
		
		// WS field should be and same horz. as ws label and 5 below ws label
		layout.putConstraint(SpringLayout.WEST, weapon_skill, 0, SpringLayout.WEST, ws_label);
		layout.putConstraint(SpringLayout.NORTH, weapon_skill, 5, SpringLayout.SOUTH, ws_label);
		
		// Gold line
		// Gold label should be below wounds
		layout.putConstraint(SpringLayout.WEST, gold_label, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, gold_label, 5, SpringLayout.SOUTH, wounds);
		
		// Gold - should be just below the gold label and 5px from the box edge
		layout.putConstraint(SpringLayout.NORTH, sub_gold, 5, SpringLayout.SOUTH, gold_label);
		layout.putConstraint(SpringLayout.WEST, sub_gold, 5, SpringLayout.WEST, this);
		
		// Gold field should be just below the gold label and 5px from Gold-
		layout.putConstraint(SpringLayout.NORTH, gold, 5, SpringLayout.SOUTH, gold_label);
		layout.putConstraint(SpringLayout.WEST, gold, 5, SpringLayout.EAST, sub_gold);
		
		// Gold + should be just below the gold label and 5px from Gold field
		layout.putConstraint(SpringLayout.NORTH, plus_gold, 5, SpringLayout.SOUTH, gold_label);
		layout.putConstraint(SpringLayout.WEST, plus_gold, 5, SpringLayout.EAST, gold);
		
		// To Hit should be below gold
		layout.putConstraint(SpringLayout.NORTH, toHitTable, 5, SpringLayout.SOUTH, gold);
		layout.putConstraint(SpringLayout.WEST, toHitTable, 5, SpringLayout.WEST, this);
		
		// NOTES
		
		// top of the notes label should be just below to hit
		layout.putConstraint(SpringLayout.NORTH, note_label, 5, SpringLayout.SOUTH, toHitTable);
		layout.putConstraint(SpringLayout.WEST, note_label, 5, SpringLayout.WEST, this);
		
		// top of the notes should be just below the label
		layout.putConstraint(SpringLayout.NORTH, notes_scroller, 5, SpringLayout.SOUTH, note_label);
		layout.putConstraint(SpringLayout.NORTH, notes, 0, SpringLayout.NORTH, notes_scroller);		
		
		// bottom of the notes should be 5px from the bottom and left/right
		layout.putConstraint(SpringLayout.SOUTH, notes, 0, SpringLayout.SOUTH, notes_scroller);
		layout.putConstraint(SpringLayout.SOUTH, notes_scroller, -5, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.WEST, notes_scroller, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, notes_scroller, -5, SpringLayout.EAST, this);

		setBorder(new CompoundBorder(new MatteBorder(2, 2, 2, 2, Color.WHITE), new MatteBorder(1, 1, 1, 1, Color.BLACK)));
	}
	
	private class PlusGoldActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			GoldDialogue dialogue = new GoldDialogue("Add to total");
			dialogue.setLocationRelativeTo(frame.getGametableCanvas());
			dialogue.setVisible(true);
			characterData.setGold(characterData.getGold() + dialogue.getValue());
		}
	}
	
	private class SubGoldActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			GoldDialogue dialogue = new GoldDialogue("Remove from total");
			dialogue.setLocationRelativeTo(frame.getGametableCanvas());
			dialogue.setVisible(true);
			characterData.setGold(characterData.getGold() - dialogue.getValue());
		}
	}
	
	private class WoundsChangeListener implements ChangeListener {

		public void stateChanged(ChangeEvent arg0) {
			updating_wounds = true;
			SpinnerNumberModel model = (SpinnerNumberModel) wounds.getModel();
			int value = model.getNumber().intValue();
			characterData.setWounds(value);
			int real_value = characterData.getWounds(); 
			
			if (real_value != value) {
				model.setValue(real_value);
		    }
			
			updating_wounds = false;
		}
		
	}
	
	private class MaxWoundsChangeListener implements ChangeListener {

		public void stateChanged(ChangeEvent arg0) {
			updating_max_wounds = true;
			SpinnerNumberModel model = (SpinnerNumberModel) max_wounds.getModel();
			int value = model.getNumber().intValue();
			characterData.setMaxWounds(value);
			int real_value = characterData.getMaxWounds(); 
			
			if (real_value != value) {
				model.setValue(real_value);
		    }
			
			updating_max_wounds = false;
		}
		
	}
	
	private class WeaponSkillChangeListener implements ChangeListener {
		
		public void stateChanged(ChangeEvent arg0) {
			updating_ws = true;
			SpinnerNumberModel model = (SpinnerNumberModel) weapon_skill.getModel();
			int value = model.getNumber().intValue();
			characterData.setWeaponSkill(value);
			int real_value = characterData.getWeaponSkill(); 
			
			if (real_value != value) {
				model.setValue(real_value);
		    }
			
			updating_ws = false;
		}
		
	}
	
	private class GoldChangeListener implements ChangeListener {

		public void stateChanged(ChangeEvent arg0) {
			updating_gold = true;
			SpinnerNumberModel model = (SpinnerNumberModel) gold.getModel();
			int value = model.getNumber().intValue();
			characterData.setGold(value);
			updating_gold = false;
		}
		
	}
	
	private class NotesDocumentListener implements DocumentListener {
		public void checkValue() {
			String text_value = notes.getText();
			if (doing_update) {
				return;
			}
			updating_notes = true;
			characterData.setNotes(text_value);
			updating_notes = false;
		}
		public void changedUpdate(DocumentEvent arg0) {
			checkValue();
		}
		public void insertUpdate(DocumentEvent arg0) {
			checkValue();
		}
		public void removeUpdate(DocumentEvent arg0) {
			checkValue();
		}
	}

	public void dataChanged() {
		doing_update = true;
		if ( ! updating_wounds) {
			wounds.setValue(characterData.getWounds());
		}
		if ( ! updating_max_wounds) {
			max_wounds.setValue(characterData.getMaxWounds());
		}
		if ( ! updating_ws) {
			weapon_skill.setValue(characterData.getWeaponSkill());
		}
		if ( ! updating_gold) {
			gold.setValue(characterData.getGold());
		}
		if ( ! updating_notes) {
			notes.setText(characterData.getNotes());
		}
		toHitTable.setText(getToHitString());
		doing_update = false;
	}
	
	public static CharacterData getCharacterData() {
		return characterData;
	}
	
	/** 
     * creates the "Character Sheet" menu 
     * @return the new menu
     */
    public JMenu getMenu() {
        final JMenu menu = new JMenu("Character Sheet");
        menu.add(getSaveMenuItem());
        menu.add(getSaveAsMenuItem());
        menu.add(getOpenMenuItem());
        return menu;
    }
    
    /**
     * creates the "Save" menu item
     * @return the menu item
     */
    private JMenuItem getSaveMenuItem() {
        final JMenuItem item = new JMenuItem("Save");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                storage.userSave();
            }
        });
        return item;
    }
    
    /**
     * creates the "Save As" menu item
     * @return the menu item
     */
    private JMenuItem getSaveAsMenuItem() {
        final JMenuItem item = new JMenuItem("Save As...");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                storage.userSaveAs();
            }
        });
        return item;
    }
    
    /**
     * creates the "Open" menu item
     * @return the menu item
     */
    private JMenuItem getOpenMenuItem() {
        final JMenuItem item = new JMenuItem("Open...");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                storage.open();
            }
        });
        return item;
    }
    
    public CharacterDataStorage getStorage() {
    	return storage;
    }
    
    public PogAdapter getPogAdapter() {
    	return pog_adapter;
    }
    
    public String getToHitString() {
    	StringBuilder buff = new StringBuilder();
    	
    	// Header row etc
    	buff.append("<html><table>");
    	buff.append("<tr><th>Enemy's WS</th>");
    	for (int i = 1; i <= 10; ++i) {
    		buff.append(String.format("<td>%d</td>", i));
    	}
    	buff.append("</tr>");
    	buff.append("<tr><th>To hit foe:</th>");
    	
    	ToHitModel toHitModel = new ToHitModel();
    	try {
    		int[] toHitData = toHitModel.getToHitForWS(characterData.getWeaponSkill());
    		int[] defendData = toHitModel.getToHitForAttacker(characterData.getWeaponSkill());
    		for (int toHit : toHitData) {
    			buff.append(String.format("<td>%d</td>", toHit));
    		}
    		buff.append("</tr>");
        	buff.append("<tr><th>Enemy requires:</th>");
        	for (int toHit : defendData) {
    			buff.append(String.format("<td>%d</td>", toHit));
    		}
    	}
    	catch (IllegalArgumentException e) {
    		buff.append("<td colspan='10'>?</td>");
    		buff.append("</tr>");
        	buff.append("<tr><th>Enemy requires:</th>");
        	buff.append("<td colspan='10'>?</td>");
    	}
    	buff.append("</tr>");
    	buff.append("</table></html>");
    	
    	return buff.toString();
    }
}
