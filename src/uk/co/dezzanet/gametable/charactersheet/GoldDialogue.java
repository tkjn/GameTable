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
                	value = Integer.parseInt(getField().getText());
                    dispose();
                }
            });
        }
        return applyButton;
	}
	
	private JTextField getField() {
		if (goldField == null) {
			goldField = new JTextField("0", 5);
			PlainDocument gold_doc = (PlainDocument) goldField.getDocument();
			gold_doc.setDocumentFilter(new GoldDocumentFilter());
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
    
    private class GoldDocumentFilter extends DocumentFilter {
		@Override
	 	public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
			if (string.equals("")) {
				return;
			}
			Document doc = fb.getDocument();
		    StringBuilder sb = new StringBuilder();
		    sb.append(doc.getText(0, doc.getLength()));
		    sb.insert(offset, string);
		    if ( ! testInt(sb.toString())) {
		    	JOptionPane.showMessageDialog(null, "Error: Please enter an integer for gold", "Error Massage", JOptionPane.ERROR_MESSAGE);
		    	return;
		    }
		    super.insertString(fb, offset, string, attr);
		}
		
		@Override
	 	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			if (text.equals("")) {
				return;
			}
			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.replace(offset, offset + length, text);
		    if ( ! testInt(sb.toString())) {
		    	JOptionPane.showMessageDialog(null, "Error: Please enter an integer for gold", "Error Massage", JOptionPane.ERROR_MESSAGE);
		    	return;
		    }
		    super.replace(fb, offset, length, text, attrs);
		}
		
		@Override
		public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
			Document doc = fb.getDocument();
		    StringBuilder sb = new StringBuilder();
		    sb.append(doc.getText(0, doc.getLength()));
		    sb.delete(offset, offset + length);
		    if ( ! testInt(sb.toString())) {
		    	JOptionPane.showMessageDialog(null, "Error: Please enter an integer for gold", "Error Massage", JOptionPane.ERROR_MESSAGE);
		    	return;
		    }
		    super.remove(fb, offset, length);
		}
		
		private boolean testInt(String text) {
			try {
				int value = Integer.parseInt(text);
				return true;
			}
			catch (NumberFormatException e) {
				return false;
			}
		}
	}
}
