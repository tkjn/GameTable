package uk.co.dezzanet.gametable.charactersheet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import com.galactanet.gametable.GametableFrame;
import com.galactanet.gametable.ui.chat.ChatPanel;

public class CharacterSheetPanel extends JPanel implements ICharacterDataChangedListener {

	private static final long serialVersionUID = -8857859513490491696L;
	
	private static CharacterData characterData = new CharacterData();
	
	private CharacterDataStorage storage = new CharacterDataStorage();

	private JTextField wounds;

	private JTextField max_wounds;
	
	private boolean doing_update = false;
	private boolean updating_wounds = false;
	private boolean updating_max_wounds = false;
	private boolean updating_notes;

	private JTextArea notes;
	
	/**
	 * This is the default constructor
	 */
	public CharacterSheetPanel() {
		initialise();
		GametableFrame frame = GametableFrame.getGametableFrame();
		JMenuBar menubar = frame.getJMenuBar();
		menubar.add(getMenu());
	}
	
	/**
	 * This method initialises this
	 * 
	 * @return void
	 */
	private void initialise() {
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
		
		// bottom of the notes should be 5px from the bottom and left/right
		layout.putConstraint(SpringLayout.SOUTH, notes, 0, SpringLayout.SOUTH, notes_scroller);
		layout.putConstraint(SpringLayout.SOUTH, notes_scroller, 5, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.WEST, notes_scroller, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, notes_scroller, 5, SpringLayout.EAST, this);
		
		// bottom of the notes label should be just above the notes field and 5px from the left
		layout.putConstraint(SpringLayout.SOUTH, note_label, -5, SpringLayout.NORTH, notes_scroller);
		layout.putConstraint(SpringLayout.WEST, note_label, 5, SpringLayout.WEST, this);
		
		
		

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
}