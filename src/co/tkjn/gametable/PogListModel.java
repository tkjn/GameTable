/*
 * PogListModel.java: GameTable is in the Public Domain.
 */


package co.tkjn.gametable;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.AbstractListModel;

import com.galactanet.gametable.Log;
import com.galactanet.gametable.Pog;
import com.galactanet.gametable.net.PacketManager;
import com.galactanet.gametable.ui.PogLibrary;

/**
 * TODO: comment
 * 
 * @author tkjn
 */
public class PogListModel extends AbstractListModel<String>
{
    
    private File pogDir;
    
    private Vector<Pog> pogs = new Vector<Pog>();
    
    public PogListModel(String libName)
    {
        setDir(libName);
        refreshPogs();
    }

    /*
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public String getElementAt(int arg0)
    {
        Pog pog = getPogAt(arg0);
        String pogText = pog.getText();
        if (pogText.equals(""))
        {
            pogText = "--Unknown Pog--";
        }
        return pogText;
    }
    
    public Pog getPogAt(int arg0)
    {
        return pogs.get(arg0);
    }

    /*
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize()
    {
        // TODO Auto-generated method stub
        return pogs.size();
    }
    
    public void setDir(String dirName)
    {
        pogDir = new File(dirName).getAbsoluteFile();
    }
    
    public void refreshPogs()
    {
        String[] pogFiles = pogDir.list(new PogFilenameFilter());
        pogs.clear();
        for (int i=0; i < pogFiles.length; i++)
        {
            try {
                File openFile = new File(pogDir, pogFiles[i]);
                final FileInputStream infile = new FileInputStream(openFile);
                final DataInputStream dis = new DataInputStream(infile);
                final Pog nPog = new Pog(dis);

                if (nPog.isUnknown()) { // we need this image
                    PacketManager.requestPogImage(null, nPog);
                }
                
                pogs.add(nPog);
                
            }
            catch (final IOException ex1) {
                Log.log(Log.SYS, ex1);
            }
        }
    }

}
