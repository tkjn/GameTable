package uk.co.dezzanet.gametable.charactersheet;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.text.DocumentFilter.FilterBypass;

public class GoldDialogue extends JDialog {
	
	private String description;
	private JButton applyButton;
	private JTextField goldField;
	private JPanel contentPanel;
	private JPanel centerPanel;
	private int value = 0;

	public GoldDialogue(String passed_description) {
		super();
		description = passed_description;
		init();
	}

	private void init() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setTitle("Adjust Gold By...");
        setContentPane(getContentPanel());
        pack();
	}
	
	private JButton getApplyButton() {
		if (applyButton == null) {
            applyButton = new JButton();
            applyButton.setText(description);
            applyButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                	if (validateGold()) {
	                	value = Integer.parseInt(getField().getText());
	                    dispose();
                	}
                	else {
                		JOptionPane.showMessageDialog(null, "Error: Please enter an integer for gold", "Error Massage", JOptionPane.ERROR_MESSAGE);
                	}
                }
            });
        }
        return applyButton;
	}
	
	private JTextField getField() {
		if (goldField == null) {
			goldField = new JTextField("0", 5);
        }
        return goldField;
	}
	
	private JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(getCenterPanel(), java.awt.BorderLayout.CENTER);
        }
        return contentPanel;
    }
	
	/**
     * This method initializes centerPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getCenterPanel()
    {
        if (centerPanel == null) {
            final GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints21.gridy = 3;
            gridBagConstraints21.weightx = 1.0;
            gridBagConstraints21.gridx = 2;
            final GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.gridx = 2;
            centerPanel = new JPanel();
            centerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 5));
            centerPanel.setLayout(new GridBagLayout());
            centerPanel.add(getField(), gridBagConstraints1);
            centerPanel.add(getApplyButton(), gridBagConstraints21);
        }
        return centerPanel;
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
