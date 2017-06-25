package com.tkjngine.gametable.monsterwounds;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

public class WoundsDialogue extends JDialog {

    private static final int BORDER = 5;
    private JButton applyButton;
    private JTextField woundsField;
    private JPanel contentPanel;
    private int value = 0;
    private int maxValue = 0;

    public WoundsDialogue(int currentValue, int maxWounds) {
        super();
        if (currentValue > 0 && currentValue <= maxWounds) {
            value = currentValue;
        }
        if (maxWounds > 0) {
            maxValue = maxWounds;
        }
        init();
    }

    private void init() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setTitle("Wounds");
        setContentPane(getContentPanel());
        pack();
    }

    private JButton getApplyButton() {
        if (applyButton == null) {
            applyButton = new JButton();
            applyButton.setText("Save");
            applyButton.addActionListener(getSubmitListener());
        }
        return applyButton;
    }

    private ActionListener getSubmitListener() {
        return new ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (!validateWounds()) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Error: Please enter an integer for max wounds",
                        "Error Message",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                int newValue = Integer.parseInt(getField().getText());

                if (maxValue > 0 && newValue > maxValue) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Error: Wounds cannot exceed Max Wounds (" + maxValue + ")",
                        "Error Message",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                value = newValue;
                dispose();
            }
        };
    }

    private JTextField getField() {
        if (woundsField == null) {
            woundsField = new JTextField(String.valueOf(value), 8);
            woundsField.addActionListener(getSubmitListener());
        }
        return woundsField;
    }

    private JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setLayout(new GridLayout(3, 1));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(0, BORDER, BORDER, BORDER));
            contentPanel.add(new JLabel("Set Wounds"));
            contentPanel.add(getField());
            contentPanel.add(getApplyButton());
        }
        return contentPanel;
    }

    public int getValue() {
        return value;
    }

    private boolean validateWounds() {
        String enteredValue = getField().getText();
        try {
            return Integer.parseInt(enteredValue) > 0;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
