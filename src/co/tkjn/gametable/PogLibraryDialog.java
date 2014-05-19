/*
 * PogLibraryDialog: GameTable is in the Public Domain.
 */


package co.tkjn.gametable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.galactanet.gametable.Log;
import com.galactanet.gametable.Pog;



/**
 * TODO: comment
 * 
 * @author tkjn
 */
public class PogLibraryDialog extends JDialog implements ListSelectionListener /*implements FocusListener*/
{
    /**
     * 
     */
    private static final long serialVersionUID = 7635834691550405596L;

    private boolean           m_bAccepted;
    private final JButton     m_cancel         = new JButton();
    private final JButton     m_ok             = new JButton();
    
    private Pog selectedPog;
    
    private final PogListModel pogListModel    = new PogListModel("poginstances");
    private final JLabel      pogListLabel     = new JLabel();
    private final JList       pogList          = new JList(pogListModel);
    
    private final PogDetailsPanel pogDetails   = new PogDetailsPanel();

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

        //m_nameEntry.requestFocus();
    }

  /*  public void focusGained(final FocusEvent e)
    {
        // only interested in JTextFields
        if (!(e.getSource() instanceof JTextField))
        {
            return;
        }

        final JTextField focused = (JTextField)e.getSource();
        focused.setSelectionStart(0);
        focused.setSelectionEnd(focused.getText().length());
    }

    public void focusLost(final FocusEvent e)
    {
    }*/

    private void initialize()
    {
        setTitle("Pog Library");
        setResizable(false);
        setPreferredSize(new Dimension(300,300));
        pogList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pogList.setLayoutOrientation(JList.VERTICAL);
        pogList.setVisibleRowCount(-1);
        m_ok.setText("Load Pog");
        m_ok.addActionListener(new ActionListener()
        {
            /*
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             */
            public void actionPerformed(final ActionEvent e)
            {
                selectedPog = ((PogListModel)pogList.getModel()).getPogAt(pogList.getSelectedIndex());
                dispose();
            }
        });

        m_cancel.setText("Cancel");
        m_cancel.addActionListener(new ActionListener()
        {
            /*
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             */
            public void actionPerformed(final ActionEvent e)
            {
                dispose();
            }
        });

        pogListLabel.setText("Pogs:");

        final int PADDING = 5;

        final Box outmostBox = Box.createHorizontalBox();
        getContentPane().add(outmostBox, BorderLayout.CENTER);
        outmostBox.add(Box.createHorizontalStrut(PADDING));
        final Box outerBox = Box.createVerticalBox();
        outmostBox.add(outerBox);
        outmostBox.add(Box.createHorizontalStrut(PADDING));

        outerBox.add(Box.createVerticalStrut(PADDING));

        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel pogListPanel = new JPanel(new BorderLayout());
 
        pogListPanel.add(pogListLabel, BorderLayout.NORTH);
        
        JScrollPane listScroller = new JScrollPane(pogList);
        listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScroller.setPreferredSize(new Dimension(120, 200));
        pogListPanel.add(listScroller, BorderLayout.EAST);
        
        pogList.addListSelectionListener(this);
        
        
        panel.add(pogListPanel, BorderLayout.WEST);
        
        listScroller = new JScrollPane(pogDetails);
        listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScroller.setPreferredSize(new Dimension(150, 200));
        panel.add(listScroller, BorderLayout.EAST);
        
        outerBox.add(panel);
        outerBox.add(Box.createVerticalStrut(PADDING));

        outerBox.add(Box.createVerticalStrut(PADDING * 2));

        outerBox.add(Box.createVerticalStrut(PADDING * 3));
        outerBox.add(Box.createVerticalGlue());

        panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        outerBox.add(panel);
        panel.add(m_ok);
        panel.add(Box.createHorizontalStrut(PADDING));
        panel.add(m_cancel);

        outerBox.add(Box.createVerticalStrut(PADDING));

        setModal(true);
    }

    public Pog getPog()
    {
        return selectedPog;
    }
    
    public void valueChanged(ListSelectionEvent e)
    {
        pogDetails.setPog(((PogListModel)pogList.getModel()).getPogAt(pogList.getSelectedIndex()));
    }
}
