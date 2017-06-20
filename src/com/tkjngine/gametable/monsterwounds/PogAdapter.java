package com.tkjngine.gametable.monsterwounds;

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;

import com.galactanet.gametable.GametableFrame;
import com.galactanet.gametable.Pog;

public class PogAdapter implements Observer {

    private Pog pog;
    private MonsterData monsterData;

    public PogAdapter(MonsterData monsterData, Pog pog) {
        this.monsterData = monsterData;
        this.monsterData.addObserver(this);
        this.pog = pog;

        String maxWounds = getPogMaxWounds();
        if (isWoundsInteger(maxWounds)) {
            initWounds(Integer.parseInt(maxWounds));
            refreshPog();
            return;
        }

        String wounds = getPogWounds();
        if (isWoundsInteger(wounds)) {
            initWounds(Integer.parseInt(wounds));
        }

        refreshPog();

    }

    private void initWounds(int wounds)
    {
        this.monsterData.setMaxWounds(wounds);
        this.monsterData.setWounds(wounds);
    }

    private String getPogMaxWounds()
    {
        return pog.getAttribute("Max Wounds");
    }

    private String getPogWounds()
    {
        return pog.getAttribute("Wounds");
    }

    public void refreshPog()
    {
        String currentWounds = getPogWounds();
        String currentMaxWounds = getPogMaxWounds();

        String maxWounds = String.valueOf(monsterData.getMaxWounds());
        String wounds = String.valueOf(monsterData.getWounds()) + " / " + maxWounds;

        HashMap<String, String> map = new HashMap<String, String>();
        if ( ! wounds.equals(currentWounds)) {
            map.put("Wounds", wounds);
        }

        if ( ! maxWounds.equals(currentMaxWounds)) {
            map.put("Max Wounds", maxWounds);
        }

        GametableFrame.getGametableFrame().getGametableCanvas().setPogData(pog.getId(), null, map, null);

    }

    public MonsterData getMonsterData()
    {
        return this.monsterData;
    }

    @Override
    public void update(Observable obs, Object obj)
    {
        if (obs == this.monsterData) {
            refreshPog();
        }
    }

    private boolean isWoundsInteger(String wounds)
    {
        if (null == wounds) {
            return false;
        }
        try {
            if (wounds.equals(String.valueOf(Integer.parseInt(wounds)))) {
                return true;
            }
        } catch (NumberFormatException e) {
        }
        return false;
    }
}
