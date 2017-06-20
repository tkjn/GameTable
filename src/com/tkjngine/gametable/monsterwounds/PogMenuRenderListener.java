package com.tkjngine.gametable.monsterwounds;

import com.galactanet.gametable.Pog;
import com.galactanet.gametable.events.IPogMenuRenderEventListener;
import com.galactanet.gametable.events.PogMenuRenderEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.HashMap;

public class PogMenuRenderListener implements IPogMenuRenderEventListener
{
    private HashMap<Pog, PogAdapter> pogRegistry; // Pull this out of this class and contain separately

    PogMenuRenderListener()
    {
        this.pogRegistry = new HashMap<Pog, PogAdapter>();
    }

    private PogAdapter getPogAdapter(Pog pog)
    {
        return pogRegistry.get(pog);
    }

    @Override
    public void renderMenu(PogMenuRenderEvent event) {
        if (!Objects.equals(event.getMenuName(), "Pog Menu")) {
            return;
        }

        final Pog activePog = event.getActivePog();
        final PogAdapter pogAdapter = getPogAdapter(activePog);

        JPopupMenu menu = event.getMenu();

        if (null == pogAdapter) {
            final HashMap<Pog, PogAdapter> pogRegistry = this.pogRegistry;

            JMenuItem item = new JMenuItem("Enable Wounds");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    if (null == pogRegistry.get(activePog))
                    {
                        MonsterData monsterData = new MonsterData();
                        PogAdapter pogAdapter = new PogAdapter(monsterData, activePog);
                        pogRegistry.put(activePog, pogAdapter);
                    }
                }
            });

            menu.addSeparator();
            menu.add(item);
        } else {
            // TODO
            /*JMenuItem item = new JMenuItem("Set Max Wounds");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    // max wounds dialog
                }
            });
            menu.addSeparator();
            menu.add(item);*/

            item = new JMenuItem("-1 Wound");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    pogAdapter.getMonsterData().decWounds();
                }
            });
            menu.addSeparator();
            menu.add(item);

            item = new JMenuItem("+1 Wound");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    pogAdapter.getMonsterData().incWounds();
                }
            });
            menu.addSeparator();
            menu.add(item);

            item = new JMenuItem("Refill Wounds");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    MonsterData monsterData = pogAdapter.getMonsterData();
                    monsterData.setWounds(monsterData.getMaxWounds());
                }
            });
            menu.addSeparator();
            menu.add(item);
        }
    }
}
