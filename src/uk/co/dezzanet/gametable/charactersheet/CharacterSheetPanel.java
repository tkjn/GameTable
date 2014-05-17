package uk.co.dezzanet.gametable.charactersheet;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;

public class CharacterSheetPanel extends JPanel {

	private static final long serialVersionUID = -8857859513490491696L;
	
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

		//final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 3));
		//add(panel);
		
		final JLabel wounds_label = new JLabel("Wounds");
		add(wounds_label);
		
		final JTextField wounds = new JTextField("0", 2);
		add(wounds);
		
		Dimension wounds_size = wounds.getPreferredSize();
		
		final JButton sub_wounds = new JButton("-");
		sub_wounds.setPreferredSize(wounds_size);
		sub_wounds.setFont(new Font("Ariel", Font.PLAIN, 8));
		sub_wounds.setMargin(new Insets(1,1,1,1));
		add(sub_wounds);
		
		final JButton plus_wounds = new JButton("+");
		plus_wounds.setPreferredSize(wounds_size);
		plus_wounds.setFont(new Font("Ariel", Font.PLAIN, 8));
		plus_wounds.setMargin(new Insets(1,1,1,1));
		add(plus_wounds);
		
		final JLabel note_label = new JLabel("Notes");
		add(note_label);
		
		final JTextArea notes = new JTextArea(10, 30);
		notes.setLineWrap(true);
		notes.setWrapStyleWord(true);
		
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

}
