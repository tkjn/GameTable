package com.tkjngine.gametable.monsterwounds;

import com.galactanet.gametable.Pog;
import com.galactanet.gametable.events.IPogMenuRenderEventListener;
import com.galactanet.gametable.events.PogMenuRenderEvent;
import com.galactanet.gametable.GametableFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.HashMap;

public class PogMenuRenderListener implements IPogMenuRenderEventListener
{
    private HashMap<Pog, PogAdapter> pogRegistry; // TODO: Pull this out of this class and contain separately

    public PogMenuRenderListener()
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

            JMenuItem item = new JMenuItem("Enable Monster Wounds");
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
            final MonsterData monsterData = pogAdapter.getMonsterData();
            final JMenu monsterWoundsMenu = new JMenu("Monster Wounds");
            menu.add(monsterWoundsMenu);
            JMenuItem item = null;

            item = new JMenuItem("-1 Wound");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    monsterData.decWounds();
                }
            });
            monsterWoundsMenu.addSeparator();
            monsterWoundsMenu.add(item);

            item = new JMenuItem("+1 Wound");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    monsterData.incWounds();
                }
            });
            monsterWoundsMenu.addSeparator();
            monsterWoundsMenu.add(item);

            item = new JMenuItem("Refill Wounds");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    monsterData.setWounds(monsterData.getMaxWounds());
                }
            });
            monsterWoundsMenu.addSeparator();
            monsterWoundsMenu.add(item);

            item = new JMenuItem("Set Wounds");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    // max wounds dialog
                    WoundsDialogue dialogue = new WoundsDialogue(monsterData.getWounds(), monsterData.getMaxWounds());
                    dialogue.setLocationRelativeTo(GametableFrame.getGametableFrame().getGametableCanvas());
                    dialogue.setVisible(true);
                    monsterData.setWounds(dialogue.getValue());
                }
            });
            monsterWoundsMenu.addSeparator();
            monsterWoundsMenu.add(item);

            item = new JMenuItem("Set Max Wounds");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    // max wounds dialog
                    MaxWoundsDialogue dialogue = new MaxWoundsDialogue(monsterData.getMaxWounds());
                    dialogue.setLocationRelativeTo(GametableFrame.getGametableFrame().getGametableCanvas());
                    dialogue.setVisible(true);
                    monsterData.setMaxWounds(dialogue.getValue());
                }
            });
            monsterWoundsMenu.addSeparator();
            monsterWoundsMenu.add(item);

        }
    }
}
