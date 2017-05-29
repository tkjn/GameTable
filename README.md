# GameTable

An open ended game table with network support.

This also provides some convenience features for Warhammer Quest (1995) style gameplay.
In future these game specific features will likely be moved into some kind of optional plugin.

[![Build Status](https://travis-ci.org/tkjn/GameTable.svg?branch=master)](https://travis-ci.org/tkjn/GameTable)

## Networking
Servers listen on port 6812 by default, although this can be configured when starting a session.

## Map controls

### Scroll
Right click and drag

### Zoom
+/- keys and mouse wheel

### Display pointer (visiable to all players)
Space bar while mouse pointer present in map area

### Recenter view for all players
**Map** => **Recenter for all players**

### View all pog data text
Shift key

### Add pog
Drag from pog side panel
OR **Map** => **Load Pog**

### Move pog
Left click on pog and drag

### Pog menu
Left click on pog

### Disable grid-snapping while moving pog
Ctrl key

## Pog Library
Pogs can be saved to the Pog Library in their full state for future use.
To do this, access the Pog's menu and select **Save to Pog Library...**

To load a Pog from the library onto the map go to **Map** => **Pog Library**.
Select the desired Pog and then click **Load Pog**.

## Character sheet
The Character Sheet side-panel tab allows management of basic attributes of the player's character.

The state can be saved/loaded using the options in the **Character Sheet** menu.

A pog can be linked to the player's character sheet and it's attributes will be updated with the sheet.
This is achieved by accessing the Pog's menu and selecting **Set as my character**

## Chat commands
Shortcuts to focus chat text area: Enter key or / key

There are various special commands: type "//" or "/help" and press Enter to view a list of the available commands.

## Origin
This is a diversion of Gametable - http://gametableproj.sourceforge.net

This project attempts to fix issues and enhance the original, seemingly dead project.
This project also adds convenience features for gameplay similar to the original Warhammer Quest.
