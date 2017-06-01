/*
 * PogLibraryDialog: GameTable is in the Public Domain.
 */


package co.tkjn.gametable.poglibrary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import java.io.File;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.galactanet.gametable.Log;
import com.galactanet.gametable.Pog;
import com.galactanet.gametable.util.UtilityFunctions;



/**
 * @author tkjn
 */
public class PogLibraryDialog extends JDialog implements ListSelectionListener
{
    private final JButton cancelButton = new JButton();
    private final JButton okButton     = new JButton();
    private final JButton deleteButton = new JButton();
    
    private Pog selectedPog;
    
    private final JList<PogFile> pogList = new JList<PogFile>(new PogListModel("poginstances"));
    
    private final PogDetailsPanel pogDetailsPanel = new PogDetailsPanel();

    private final int DIALOG_WIDTH = 400;
    private final int DIALOG_HEIGHT = 500;
    private final int PADDING = 5;

    public PogLibraryDialog()
    {
        try
        {
            initialize();
        }
        catch (final Exception e)
        {
            Log.log(Log.SYS, e);
        }

        // pack yourself
        pack();

        // centre yourself
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height)
        {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width)
        {
            frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    public Pog getPog()
    {
        return selectedPog;
    }

    public void valueChanged(ListSelectionEvent e)
    {
        Pog pog = getSelectedPog();
        if (null == pog)
        {
            removePogDetails();
            return;
        }

        setPogDetails(pog);
    }

    private void initialize()
    {
        setTitle("Pog Library");
        setResizable(false);
        setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));

        initialisePogList();
        initialiseOkButton();
        initialiseCancelButton();
        initialiseDeleteButton();

        final Box outerBox = generateOuterBox();

        outerBox.add(Box.createVerticalStrut(PADDING));

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(generatePogListPanel(), BorderLayout.WEST);

        JScrollPane pogDetails = generatePogDetails();
        panel.add(pogDetails, BorderLayout.EAST);

        outerBox.add(panel);

        outerBox.add(Box.createVerticalStrut(PADDING));
        outerBox.add(Box.createVerticalStrut(PADDING * 2));
        outerBox.add(Box.createVerticalStrut(PADDING * 3));
        outerBox.add(Box.createVerticalGlue());

        JPanel controlButtonPanel = generateControlButtonPanel();
        outerBox.add(controlButtonPanel);

        outerBox.add(Box.createVerticalStrut(PADDING));

        JPanel okCancelButtonPanel = generateOkCancelButtonPanel();
        outerBox.add(okCancelButtonPanel);

        outerBox.add(Box.createVerticalStrut(PADDING));

        setModal(true);
    }

    private JPanel generateOkCancelButtonPanel()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panel.add(okButton);
        panel.add(Box.createHorizontalStrut(PADDING));
        panel.add(cancelButton);
        return panel;
    }

    private JPanel generateControlButtonPanel()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panel.add(deleteButton);
        return panel;
    }

    private Box generateOuterBox()
    {
        final Box outmostBox = Box.createHorizontalBox();
        getContentPane().add(outmostBox, BorderLayout.CENTER);
        outmostBox.add(Box.createHorizontalStrut(PADDING));
        final Box outerBox = Box.createVerticalBox();
        outmostBox.add(outerBox);
        outmostBox.add(Box.createHorizontalStrut(PADDING));
        return outerBox;
    }

    private JPanel generatePogListPanel()
    {
        JPanel pogListPanel = new JPanel(new BorderLayout());

        JLabel pogListLabel = new JLabel();
        pogListLabel.setText("Pogs:");
        pogListPanel.add(pogListLabel, BorderLayout.NORTH);

        JScrollPane listScroller = new JScrollPane(pogList);
        listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        listScroller.setPreferredSize(new Dimension(DIALOG_WIDTH / 2 - 10, DIALOG_HEIGHT - 100));
        pogListPanel.add(listScroller, BorderLayout.EAST);

        return pogListPanel;
    }

    private JScrollPane generatePogDetails()
    {
        JScrollPane detailsScroller = new JScrollPane(pogDetailsPanel);
        detailsScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        detailsScroller.setPreferredSize(new Dimension(DIALOG_WIDTH / 2 - 10, DIALOG_HEIGHT - 100));
        return detailsScroller;
    }

    private void initialisePogList()
    {
        pogList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pogList.setLayoutOrientation(JList.VERTICAL);
        pogList.setVisibleRowCount(-1);
        pogList.addListSelectionListener(this);
    }

    private void initialiseOkButton()
    {
        okButton.setText("Load Pog");
        final JDialog dialog = this;
        okButton.addActionListener(new ActionListener()
        {
            /*
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             */
            public void actionPerformed(final ActionEvent e)
            {
                int index = pogList.getSelectedIndex();
                if (index < 0)
                {
                    removePogDetails();
                    UtilityFunctions.msgBox(dialog, "No pog selected.", "Load Pog Failure");
                    return;
                }

                selectedPog = getSelectedPog();
                dispose();
            }
        });
    }

    private void initialiseCancelButton()
    {
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new ActionListener()
        {
            /*
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             */
            public void actionPerformed(final ActionEvent e)
            {
                dispose();
            }
        });
    }

    private void initialiseDeleteButton()
    {
        deleteButton.setText("Delete Pog");
        final JDialog dialog = this;
        deleteButton.addActionListener(new ActionListener()
        {
            /*
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             */
            public void actionPerformed(final ActionEvent e)
            {
                int index = pogList.getSelectedIndex();
                if (index < 0)
                {
                    removePogDetails();
                    UtilityFunctions.msgBox(dialog, "No pog selected.", "Delete Pog Failure");
                    return;
                }

                PogListModel pogListModel = (PogListModel)pogList.getModel();

                Pog pog = pogListModel.getPogAt(index);

                final int result = UtilityFunctions.yesNoDialog(
                    dialog,
                    "Are you sure you wish to permanently delete the pog '" + pog.getText() + "'?",
                    "Delete Pog?"
                );
                if (result == UtilityFunctions.YES)
                {
                    pogListModel.deletePog(index);
                }

            }
        });
        disableDeleteButton();
    }

    private void enableDeleteButton()
    {
        deleteButton.setEnabled(true);
    }

    private void disableDeleteButton()
    {
        deleteButton.setEnabled(false);
    }

    private Pog getSelectedPog()
    {
        PogFile pogFile = pogList.getSelectedValue();

        if (null == pogFile)
        {
            return null;
        }

        return pogFile.getPog();
    }

    private void removePogDetails()
    {
        pogDetailsPanel.removePog();
        disableDeleteButton();
    }

    private void setPogDetails(Pog pog)
    {
        pogDetailsPanel.setPog(pog);
        enableDeleteButton();
    }
}
