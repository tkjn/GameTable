/*
 * PogFile.java: GameTable is in the Public Domain.
 */


package co.tkjn.gametable.poglibrary;

import java.io.File;

import com.galactanet.gametable.Pog;

/**
 * TODO: comment
 * 
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

}
