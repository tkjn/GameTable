package com.tkjngine.gametable.monsterwounds;

import java.util.*;

public class MonsterData extends Observable
{
    private int maxWounds = 0;
    private int wounds = 0;

    public int getMaxWounds()
    {
        return maxWounds;
    }

    public void setMaxWounds(int maxWounds)
    {
        if (this.maxWounds == maxWounds
            && this.wounds <= maxWounds)
        {
            return;
        }

        this.maxWounds = maxWounds;
        if (this.wounds > maxWounds) {
            this.wounds = maxWounds;
        }
        notifyChange();
    }

    public int getWounds()
    {
        return wounds;
    }

    public void setWounds(int wounds)
    {
        if (this.wounds != wounds
            && wounds >= 0
            && wounds <= this.maxWounds
        ) {
            this.wounds = wounds;
            notifyChange();
        }
    }

    public void incWounds()
    {
        this.setWounds(wounds + 1);
    }

    public void decWounds()
    {
        this.setWounds(wounds - 1);
    }

    private void notifyChange()
    {
        setChanged();
        notifyObservers();
    }

    /**
     * Re-initialises data - e.g. when about to load from a save
     */
    public void resetData()
    {
        setMaxWounds(0); // Will also set wounds
    }
}
