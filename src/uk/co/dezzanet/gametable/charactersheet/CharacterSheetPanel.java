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
    private boolean updating_ws = false;

    private JTextArea notes;

    private PogAdapter pog_adapter;

    private JSpinner gold;

    private GametableFrame frame;

    private JSpinner weapon_skill;

    /**
     * This is the default constructor
     */
    public CharacterSheetPanel() {
        initialise();
        frame = GametableFrame.getGametableFrame();
        JMenuBar menubar = frame.getJMenuBar();
        menubar.add(getMenu());
    }

    private void initialise() {

        pog_adapter = new PogAdapter(characterData);

        setMaximumSize(new Dimension(32768, 31));
        setMinimumSize(new Dimension(100, 31));

        final SpringLayout layout = new SpringLayout();
        setLayout(layout);

        characterData.addListener(this);

        JPanel woundsPanel = getWoundsPanel();
        add(woundsPanel);

        JPanel maxWoundsPanel = getMaxWoundsPanel();
        add(maxWoundsPanel);

        JPanel weaponSkillPanel = getWeaponSkillPanel();
        add(weaponSkillPanel);

        JPanel goldPanel = getGoldPanel();
        add(goldPanel);

        JPanel toHitTable = buildToHitTable();
        add(toHitTable);

        // Notes
        final JLabel note_label = buildNotesLabel();
        add(note_label);

        JScrollPane notes_scroller = buildNotesField();

        add(notes_scroller, BorderLayout.CENTER);

        // Wounds panel should start at 5x5 from the top left
        layout.putConstraint(SpringLayout.WEST, woundsPanel, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, woundsPanel, 5, SpringLayout.NORTH, this);

        // Max Wounds Label should be 5 from the top and 5 to the left of wounds panel
        layout.putConstraint(SpringLayout.WEST, maxWoundsPanel, 5, SpringLayout.EAST, woundsPanel);
        layout.putConstraint(SpringLayout.NORTH, maxWoundsPanel, 5, SpringLayout.NORTH, this);

        // WS Panel should be 5 from the top and 5 to the left of max wounds panel
        layout.putConstraint(SpringLayout.WEST, weaponSkillPanel, 5, SpringLayout.EAST, maxWoundsPanel);
        layout.putConstraint(SpringLayout.NORTH, weaponSkillPanel, 5, SpringLayout.NORTH, this);

        // Gold panel should be below wounds panel
        layout.putConstraint(SpringLayout.WEST, goldPanel, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, goldPanel, 5, SpringLayout.SOUTH, woundsPanel);

        // To Hit should be below gold
        layout.putConstraint(SpringLayout.NORTH, toHitTable, 5, SpringLayout.SOUTH, goldPanel);
        layout.putConstraint(SpringLayout.WEST, toHitTable, 5, SpringLayout.WEST, this);

        // NOTES

        // top of the notes label should be just below to hit
        layout.putConstraint(SpringLayout.NORTH, note_label, 5, SpringLayout.SOUTH, toHitTable);
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

    private JPanel getWoundsPanel() {
        return getLabelFieldPairPanel(buildWoundsLabel(), buildWoundsField());
    }

    private JLabel buildWoundsLabel() {
        return new JLabel("Wounds");
    }

    private JComponent buildWoundsField() {
        SpinnerModel model = new SpinnerNumberModel(0, 0, 200, 1);
        wounds = new JSpinner(model);
        wounds.addChangeListener(new WoundsChangeListener());
        return wounds;
    }

    private JPanel getLabelFieldPairPanel(JComponent label, JComponent field) {
        GridLayout layout = new GridLayout(2, 1, 5, 5);
        JPanel panel = new JPanel(layout);
        panel.add(label);
        panel.add(field);
        return panel;
    }

    private JPanel getMaxWoundsPanel() {
        return getLabelFieldPairPanel(buildMaxWoundsLabel(), buildMaxWoundsField());
    }

    private JLabel buildMaxWoundsLabel() {
        return new JLabel("Max");
    }

    private JComponent buildMaxWoundsField() {
        SpinnerModel max_wounds_model = new SpinnerNumberModel(0, 0, 200, 1);
        max_wounds = new JSpinner(max_wounds_model);
        max_wounds.addChangeListener(new MaxWoundsChangeListener());
        return max_wounds;
    }

    private JPanel getWeaponSkillPanel() {
        return getLabelFieldPairPanel(buildWeaponSkillLabel(), buildWeaponSkillField());
    }

    private JLabel buildWeaponSkillLabel() {
        return new JLabel("WS");
    }

    private JComponent buildWeaponSkillField() {
        SpinnerModel ws_model = new SpinnerNumberModel(1, 1, 10, 1);
        weapon_skill = new JSpinner(ws_model);
        weapon_skill.addChangeListener(new WeaponSkillChangeListener());
        return weapon_skill;
    }

    private JPanel getGoldPanel() {
        BorderLayout layout = new BorderLayout(5, 5);
        JPanel panel = new JPanel(layout);

        panel.add(buildGoldLabel(), BorderLayout.PAGE_START);

        JSpinner goldField = buildGoldField();
        Dimension goldSize = goldField.getPreferredSize();
        goldSize.width = 80;
        goldField.setPreferredSize(goldSize);

        panel.add(buildSubtractGoldButton(), BorderLayout.LINE_START);
        panel.add(goldField, BorderLayout.CENTER);
        panel.add(buildAddGoldButton(), BorderLayout.LINE_END);

        return panel;
    }

    private JLabel buildGoldLabel() {
        return new JLabel("Gold");
    }

    private JSpinner buildGoldField() {
        SpinnerModel gold_model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        gold = new JSpinner(gold_model);
        gold.addChangeListener(new GoldChangeListener());
        return gold;
    }

    private JButton buildAddGoldButton() {
        final JButton plusGold = new JButton("+");
        plusGold.setFont(new Font("Ariel", Font.PLAIN, 8));
        plusGold.addActionListener(new PlusGoldActionListener());
        plusGold.setMargin(new Insets(1,1,1,1));
        Dimension size = plusGold.getPreferredSize();
        size.width = 30;
        plusGold.setPreferredSize(size);
        return plusGold;
    }

    private JButton buildSubtractGoldButton() {
        final JButton subGold = new JButton("-");
        subGold.setFont(new Font("Ariel", Font.PLAIN, 8));
        subGold.setMargin(new Insets(1,1,1,1));
        subGold.addActionListener(new SubGoldActionListener());
        Dimension size = subGold.getPreferredSize();
        size.width = 30;
        subGold.setPreferredSize(size);
        return subGold;
    }

    private JScrollPane buildNotesField() {
        notes = new JTextArea(10, 30);
        notes.setLineWrap(true);
        notes.setWrapStyleWord(true);
        notes.getDocument().addDocumentListener(new NotesDocumentListener());

        return new JScrollPane(notes, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    private JLabel buildNotesLabel() {
        return new JLabel("Notes");
    }

    private JPanel buildToHitTable() {
        return new ToHitTableView(characterData);
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

    private class WeaponSkillChangeListener implements ChangeListener {

        public void stateChanged(ChangeEvent arg0) {
            updating_ws = true;
            SpinnerNumberModel model = (SpinnerNumberModel) weapon_skill.getModel();
            int value = model.getNumber().intValue();
            characterData.setWeaponSkill(value);
            int real_value = characterData.getWeaponSkill();

            if (real_value != value) {
                model.setValue(real_value);
            }

            updating_ws = false;
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
        if ( ! updating_ws) {
            weapon_skill.setValue(characterData.getWeaponSkill());
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
