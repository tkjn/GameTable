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
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import com.galactanet.gametable.GametableFrame;

public class CharacterSheetPanel extends JPanel implements ICharacterDataChangedListener {

	private static final long serialVersionUID = -8857859513490491696L;
	
	private static CharacterData characterData = new CharacterData();
	
	private CharacterDataStorage storage = new CharacterDataStorage();

	private JTextField wounds;

	private JTextField max_wounds;
	
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

		//final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 3));
		//add(panel);
		
		final JLabel wounds_label = new JLabel("Wounds");
		add(wounds_label);
		
		wounds = new JTextField("0", 2);
		PlainDocument doc = (PlainDocument) wounds.getDocument();
		doc.setDocumentFilter(new WoundsDocumentFilter());
		add(wounds);
		
		Dimension wounds_size = wounds.getPreferredSize();
		
		final JButton sub_wounds = new JButton("-");
		sub_wounds.setPreferredSize(wounds_size);
		sub_wounds.setFont(new Font("Ariel", Font.PLAIN, 8));
		sub_wounds.setMargin(new Insets(1,1,1,1));
		sub_wounds.addActionListener(new SubWoundsActionListener());
		add(sub_wounds);
		
		final JButton plus_wounds = new JButton("+");
		plus_wounds.setPreferredSize(wounds_size);
		plus_wounds.setFont(new Font("Ariel", Font.PLAIN, 8));
		plus_wounds.addActionListener(new PlusWoundsActionListener());
		plus_wounds.setMargin(new Insets(1,1,1,1));
		add(plus_wounds);
		
		final JLabel max_wounds_label = new JLabel("Max");
		add(max_wounds_label);
		
		max_wounds = new JTextField("0", 2);
		doc = (PlainDocument) max_wounds.getDocument();
		doc.setDocumentFilter(new MaxWoundsDocumentFilter());
		add(max_wounds);
		
		// Gold
		final JLabel gold_label = new JLabel("Gold");
		add(gold_label);
		
		SpinnerModel model = new SpinnerNumberModel(0, 0, 1000000, 1);
		gold = new JSpinner(model);
		gold.addChangeListener(new GoldChangeListener());
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
		
		// Wounds - should be just below the wounds label and 5px from the box edge
		layout.putConstraint(SpringLayout.NORTH, sub_wounds, 5, SpringLayout.SOUTH, wounds_label);
		layout.putConstraint(SpringLayout.WEST, sub_wounds, 5, SpringLayout.WEST, this);
		
		// Wounds field should be just below the wounds label and 5px from Wounds-
		layout.putConstraint(SpringLayout.NORTH, wounds, 5, SpringLayout.SOUTH, wounds_label);
		layout.putConstraint(SpringLayout.WEST, wounds, 5, SpringLayout.EAST, sub_wounds);
		
		// Wounds + should be just below the wounds label and 5px from Wounds field
		layout.putConstraint(SpringLayout.NORTH, plus_wounds, 5, SpringLayout.SOUTH, wounds_label);
		layout.putConstraint(SpringLayout.WEST, plus_wounds, 5, SpringLayout.EAST, wounds);
		
		// Max Wounds Label should be 5 from the top and 5 to the left of wounds + button
		layout.putConstraint(SpringLayout.WEST, max_wounds_label, 5, SpringLayout.EAST, plus_wounds);
		layout.putConstraint(SpringLayout.NORTH, max_wounds_label, 5, SpringLayout.NORTH, this);
		
		// Max Wounds field should be just to the left of the Wounds + button and 5 below max wounds label
		layout.putConstraint(SpringLayout.WEST, max_wounds, 5, SpringLayout.EAST, plus_wounds);
		layout.putConstraint(SpringLayout.NORTH, max_wounds, 5, SpringLayout.SOUTH, max_wounds_label);
		
		// Gold line
		// Gold label should be below wounds
		layout.putConstraint(SpringLayout.WEST, gold_label, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, gold_label, 5, SpringLayout.SOUTH, sub_wounds);
		
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
	
	private class PlusWoundsActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			characterData.setWounds(characterData.getWounds() + 1);
		}
	}
	
	private class SubWoundsActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			characterData.setWounds(characterData.getWounds() - 1);
		}
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
	
	private class WoundsDocumentFilter extends DocumentFilter {
		@Override
	 	public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
			Document doc = fb.getDocument();
		    StringBuilder sb = new StringBuilder();
		    sb.append(doc.getText(0, doc.getLength()));
		    sb.insert(offset, string);
		    if ( ! testInt(sb.toString())) {
		    	JOptionPane.showMessageDialog(null, "Error: Please enter a positive or 0 number for wounds", "Error Massage", JOptionPane.ERROR_MESSAGE);
		    	return;
		    }
		    updating_wounds = true;
		    int value = Integer.parseInt(sb.toString());
		    if (characterData.getWounds() != value) {
		    	characterData.setWounds(value);
		    }
		    if (characterData.getWounds() != value) {
		    	return;
		    }
		    super.insertString(fb, offset, string, attr);
		    updating_wounds = false;
		}
		
		@Override
	 	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.replace(offset, offset + length, text);
		    if ( ! testInt(sb.toString())) {
		    	JOptionPane.showMessageDialog(null, "Error: Please enter a positive or 0 number for wounds", "Error Massage", JOptionPane.ERROR_MESSAGE);
		    	return;
		    }
		    updating_wounds = true;
		    int value = Integer.parseInt(sb.toString());
		    if (characterData.getWounds() != value) {
		    	characterData.setWounds(value);
		    }
		    if (characterData.getWounds() != value) {
		    	return;
		    }
		    super.replace(fb, offset, length, text, attrs);
		    updating_wounds = false;
		}
		
		@Override
		public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
			Document doc = fb.getDocument();
		    StringBuilder sb = new StringBuilder();
		    sb.append(doc.getText(0, doc.getLength()));
		    sb.delete(offset, offset + length);
		    if ( ! testInt(sb.toString())) {
		    	JOptionPane.showMessageDialog(null, "Error: Please enter a positive or 0 number for wounds", "Error Massage", JOptionPane.ERROR_MESSAGE);
		    	return;
		    }
		    updating_wounds = true;
		    int value = Integer.parseInt(sb.toString());
		    if (characterData.getWounds() != value) {
		    	characterData.setWounds(value);
		    }
		    if (characterData.getWounds() != value) {
		    	return;
		    }
		    super.remove(fb, offset, length);
		    updating_wounds = false;
		}
		
		private boolean testInt(String text) {
			try {
				int value = Integer.parseInt(text);
				if (value < 0) {
					throw new NumberFormatException();
				}
				return true;
			}
			catch (NumberFormatException e) {
				return false;
			}
		}
	}
	
	private class MaxWoundsDocumentFilter extends DocumentFilter {
		@Override
	 	public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
			Document doc = fb.getDocument();
		    StringBuilder sb = new StringBuilder();
		    sb.append(doc.getText(0, doc.getLength()));
		    sb.insert(offset, string);
		    if ( ! testInt(sb.toString())) {
		    	JOptionPane.showMessageDialog(null, "Error: Please enter a positive or 0 number for max wounds", "Error Massage", JOptionPane.ERROR_MESSAGE);
		    	return;
		    }
		    updating_max_wounds = true;
		    int value = Integer.parseInt(sb.toString());
		    if (characterData.getMaxWounds() != value) {
		    	characterData.setMaxWounds(value);
		    }
		    if (characterData.getMaxWounds() != value) {
		    	return;
		    }
		    super.insertString(fb, offset, string, attr);
		    updating_max_wounds = false;
		}
		
		@Override
	 	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.replace(offset, offset + length, text);
		    if ( ! testInt(sb.toString())) {
		    	JOptionPane.showMessageDialog(null, "Error: Please enter a positive or 0 number for max wounds", "Error Massage", JOptionPane.ERROR_MESSAGE);
		    	return;
		    }
		    updating_max_wounds = true;
		    int value = Integer.parseInt(sb.toString());
		    if (characterData.getMaxWounds() != value) {
		    	characterData.setMaxWounds(value);
		    }
		    if (characterData.getMaxWounds() != value) {
		    	return;
		    }
		    super.replace(fb, offset, length, text, attrs);
		    updating_max_wounds = false;
		}
		
		@Override
		public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
			Document doc = fb.getDocument();
		    StringBuilder sb = new StringBuilder();
		    sb.append(doc.getText(0, doc.getLength()));
		    sb.delete(offset, offset + length);
		    if ( ! testInt(sb.toString())) {
		    	JOptionPane.showMessageDialog(null, "Error: Please enter a positive or 0 number for max wounds", "Error Massage", JOptionPane.ERROR_MESSAGE);
		    	return;
		    }
		    updating_max_wounds = true;
		    int value = Integer.parseInt(sb.toString());
		    if (characterData.getMaxWounds() != value) {
		    	characterData.setMaxWounds(value);
		    }
		    if (characterData.getMaxWounds() != value) {
		    	return;
		    }
		    super.remove(fb, offset, length);
		    updating_max_wounds = false;
		}
		
		private boolean testInt(String text) {
			try {
				int value = Integer.parseInt(text);
				if (value < 0) {
					throw new NumberFormatException();
				}
				return true;
			}
			catch (NumberFormatException e) {
				return false;
			}
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
			wounds.setText(String.valueOf(characterData.getWounds()));
		}
		if ( ! updating_max_wounds) {
			max_wounds.setText(String.valueOf(characterData.getMaxWounds()));
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
