package com.tkjngine.gametable.monsterwounds;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MaxWoundsDialogue extends JDialog {

    private static final int BORDER = 5;
    private JButton applyButton;
    private JTextField maxWoundsField;
    private JPanel contentPanel;
    private JPanel centerPanel;
    private int value = 0;

    public MaxWoundsDialogue(int currentValue) {
        super();
        if (currentValue > 0) {
            value = currentValue;
        }
        init();
    }

    private void init() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setTitle("Max Wounds");
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
                if (validateMaxWounds()) {
                    value = Integer.parseInt(getField().getText());
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(
                        null,
                        "Error: Please enter an integer for max wounds",
                        "Error Message",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
    }

    private JTextField getField() {
        if (maxWoundsField == null) {
            maxWoundsField = new JTextField(String.valueOf(value), 10);
            maxWoundsField.addActionListener(getSubmitListener());
        }
        return maxWoundsField;
    }

    private JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setLayout(new GridLayout(3, 1));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(0, BORDER, BORDER, BORDER));
            contentPanel.add(new JLabel("Set Max Wounds"));
            contentPanel.add(getField());
            contentPanel.add(getApplyButton());
        }
        return contentPanel;
    }

    public int getValue() {
        return value;
    }

    private boolean validateMaxWounds() {
        String enteredValue = getField().getText();
        try {
            return Integer.parseInt(enteredValue) > 0;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
