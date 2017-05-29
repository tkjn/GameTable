package uk.co.dezzanet.gametable.charactersheet;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GoldDialogue extends JDialog {

    private static final int BORDER = 5;
    private String description;
	private JButton applyButton;
	private JTextField goldField;
	private JPanel contentPanel;
	private JPanel centerPanel;
	private int value = 0;

	GoldDialogue(String passed_description) {
		super();
		description = passed_description;
		init();
	}

	private void init() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setTitle("Gold");
        setContentPane(getContentPanel());
        pack();
	}
	
	private JButton getApplyButton() {
		if (applyButton == null) {
            applyButton = new JButton();
            applyButton.setText(description);
            applyButton.addActionListener(getSubmitListener());
        }
        return applyButton;
	}

    private ActionListener getSubmitListener() {
        return new ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (validateGold()) {
                    value = Integer.parseInt(getField().getText());
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(null, "Error: Please enter an integer for gold", "Error Massage", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }

    private JTextField getField() {
		if (goldField == null) {
			goldField = new JTextField("0", 5);
            goldField.addActionListener(getSubmitListener());
        }
        return goldField;
	}
	
	private JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setLayout(new GridLayout(3, 1));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(0, BORDER, BORDER, BORDER));
            contentPanel.add(new JLabel("Adjust Gold by..."));
            contentPanel.add(getField());
            contentPanel.add(getApplyButton());
        }
        return contentPanel;
    }
    
    public int getValue() {
    	return value;
    }
    
    private boolean validateGold() {
    	String entered_value = getField().getText();
		try {
			Integer.parseInt(entered_value);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}    
}
