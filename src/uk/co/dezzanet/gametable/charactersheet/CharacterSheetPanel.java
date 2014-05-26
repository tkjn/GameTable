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

	private JTextArea notes;

	private PogAdapter pog_adapter;

	private JSpinner gold;

	private GametableFrame frame;
	
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
		
		// Gold
		final JLabel gold_label = new JLabel("Gold");
		add(gold_label);
		
		SpinnerModel gold_model = new SpinnerNumberModel(0, 0, 1000000, 1);
		gold = new JSpinner(gold_model);
		gold.addChangeListener(new GoldChangeListener());
		add(gold);
		
		Dimension gold_size = gold.getPreferredSize(); 
		
		Dimension button_size = new Dimension();
		button_size.setSize(30, gold_size.getHeight());
		
		final JButton sub_gold = new JButton("-");
		sub_gold.setPreferredSize(button_size);
		sub_gold.setFont(new Font("Ariel", Font.PLAIN, 8));
		sub_gold.setMargin(new Insets(1,1,1,1));
		sub_gold.addActionListener(new SubGoldActionListener());
		add(sub_gold);
		
		final JButton plus_gold = new JButton("+");
		plus_gold.setPreferredSize(button_size);
		plus_gold.setFont(new Font("Ariel", Font.PLAIN, 8));
		plus_gold.addActionListener(new PlusGoldActionListener());
		plus_gold.setMargin(new Insets(1,1,1,1));
		add(plus_gold);
		
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
		
		// NOTES
		
		// top of the notes label should be just below gold
		layout.putConstraint(SpringLayout.NORTH, note_label, 5, SpringLayout.SOUTH, sub_gold);
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
		if ( ! updating_gold) {
			gold.setValue(characterData.getGold());
		}
		if ( ! updating_notes) {
			notes.setText(characterData.getNotes());
		}
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
}
