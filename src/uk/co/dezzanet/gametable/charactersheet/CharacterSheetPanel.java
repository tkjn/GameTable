package uk.co.dezzanet.gametable.charactersheet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CharacterSheetPanel extends JPanel implements ICharacterDataChangedListener {

	private static final long serialVersionUID = -8857859513490491696L;
	
	private CharacterData characterData = new CharacterData();

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
		wounds.getDocument().addDocumentListener(new WoundsDocumentListener());
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
		plus_wounds.setMargin(new Insets(1,1,1,1));
		plus_wounds.addActionListener(new PlusWoundsActionListener());
		add(plus_wounds);
		
		final JLabel max_wounds_label = new JLabel("Max");
		add(max_wounds_label);
		
		max_wounds = new JTextField("0", 2);
		max_wounds.getDocument().addDocumentListener(new MaxWoundsDocumentListener());
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
	
	private class WoundsDocumentListener implements DocumentListener {
		public void checkValue() {
			String text_value = wounds.getText();
			if (doing_update || text_value.equals("")) {
				return;
			}
			updating_wounds = true;
			try {
				int value = Integer.parseInt(wounds.getText());
				if (value < 0) {
					throw new NumberFormatException();
				}
				if (value != characterData.getWounds()) {
					characterData.setWounds(value);
				}
			}
			catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Error: Please enter a positive or 0 number for wounds", "Error Massage", JOptionPane.ERROR_MESSAGE);
			}
			updating_wounds = false;
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
	
	private class MaxWoundsDocumentListener implements DocumentListener {
		public void checkValue() {
			String text_value = max_wounds.getText();
			if (doing_update || text_value.equals("")) {
				return;
			}
			updating_max_wounds = true;
			try {
				int value = Integer.parseInt(text_value);
				if (value < 0) {
					throw new NumberFormatException();
				}
				if (value != characterData.getMaxWounds()) {
					characterData.setMaxWounds(value);
				}
			}
			catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Error: Please enter a positive or 0 number for max wounds", "Error Massage", JOptionPane.ERROR_MESSAGE);
			}
			updating_max_wounds = false;
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
}
