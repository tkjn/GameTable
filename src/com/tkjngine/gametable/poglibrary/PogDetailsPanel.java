/*
 * Pog.java: GameTable is in the Public Domain.
 */


package com.tkjngine.gametable.poglibrary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;

import com.galactanet.gametable.Log;
import com.galactanet.gametable.Pog;


/**
 * Represents Pog Details in a Panel
 *
 * @author tkjn
 */
public class PogDetailsPanel extends JPanel
{

    private final JLabel nameLabel = new JLabel();

    public PogDetailsPanel()
    {
        super(new BorderLayout());

        try
        {
            initialize();
        }
        catch (final Exception e)
        {
            Log.log(Log.SYS, e);
        }
    }

    private void initialize()
    {
        nameLabel.setText("");
        add(nameLabel, BorderLayout.CENTER);
    }

    public void setPog(Pog pog)
    {
        Set attributes = pog.getAttributeNames();
        String attributeString = "";
        for (final Iterator iterator = attributes.iterator(); iterator.hasNext();)
        {
            String attribute = (String)iterator.next();
            attributeString += attribute + ": " + pog.getAttribute(attribute) +"<br>";
        }

        nameLabel.setText("<html>"+pog.getText()+"<br>" + attributeString+"</html>");
    }

    public void removePog()
    {
        nameLabel.setText("");
    }
}
