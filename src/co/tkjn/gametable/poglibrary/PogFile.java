/*
 * PogFile.java: GameTable is in the Public Domain.
 */


package co.tkjn.gametable.poglibrary;

import java.io.File;

import com.galactanet.gametable.Pog;

/**
 * @author tkjn
 */
public class PogFile
{
    private File file;
    private Pog pog;

    public PogFile(File file, Pog pog)
    {
        this.file = file;
        this.pog  = pog;
    }

    public File getFile()
    {
        return file;
    }

    public Pog getPog()
    {
        return pog;
    }

    public String toString()
    {
        String pogText = pog.getText();
        if (pogText.equals(""))
        {
            pogText = "--Unknown Pog--";
        }
        return pogText;
    }
}
