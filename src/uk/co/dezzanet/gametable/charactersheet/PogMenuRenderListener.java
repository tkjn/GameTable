package uk.co.dezzanet.gametable.charactersheet;

import com.galactanet.gametable.Pog;
import com.galactanet.gametable.events.IPogMenuRenderEventListener;
import com.galactanet.gametable.events.PogMenuRenderEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class PogMenuRenderListener implements IPogMenuRenderEventListener {
    private PogAdapter pogAdapter;

    PogMenuRenderListener(PogAdapter pogAdapter)
    {
        this.pogAdapter = pogAdapter;
    }

    @Override
    public void renderMenu(PogMenuRenderEvent event) {
        if (!Objects.equals(event.getMenuName(), "Pog Menu")) {
            return;
        }
        JPopupMenu menu = event.getMenu();
        final Pog activePog = event.getActivePog();

        menu.addSeparator();

        JMenuItem item = new JMenuItem("Set as my character");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                pogAdapter.setCurrentPog(activePog);
            }
        });
        menu.add(item);
    }
}
