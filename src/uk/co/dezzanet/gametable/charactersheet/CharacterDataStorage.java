package uk.co.dezzanet.gametable.charactersheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.galactanet.gametable.util.UtilityFunctions;



public class CharacterDataStorage {
	
	private String savePath = null;
	
	public void userSave() {
		if (savePath == null) {
			userSaveAs();
			return;
		}
		File destination = new File(savePath);
		doSave(CharacterSheetPanel.getCharacterData(), destination);
	}
	
	public void autoSave() {
		String destination_path = savePath;
		if (savePath == null) {
			// Should we annoy the user with a save as? is there a better way?
			// No. There's probably a better way.
			// userSaveAs();
			return;
		}
		File destination = new File(destination_path);
		doSave(CharacterSheetPanel.getCharacterData(), destination);
	}
	
	public void userSaveAs() {
		File destination = UtilityFunctions.doFileSaveDialog("Save Character Data", "xml", true);
		if (destination == null) {
			return;
		}
		savePath = destination.getAbsolutePath();
		doSave(CharacterSheetPanel.getCharacterData(), destination);
	}
	
	private void doSave(CharacterData character_data, File destination) {
		FileOutputStream fop = null;
		try {
			
			if (destination == null) {
				return;
			}
			
			fop = new FileOutputStream(destination);
			
			if ( ! destination.exists()) {
				destination.createNewFile();
			}
			
			String data = getXMLString(character_data);
			byte[] contentInBytes = data.getBytes();
			
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.toString(), "Error Massage", JOptionPane.ERROR_MESSAGE);
			return;
		}
		finally {
			try {
				if (fop != null) {
					fop.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getXMLString(CharacterData character_data) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("character");
			doc.appendChild(rootElement);
			
			// Wounds
			Element wounds = doc.createElement("wounds");
			wounds.appendChild(doc.createTextNode(String.valueOf(character_data.getWounds())));
			rootElement.appendChild(wounds);
			
			// Max Wounds
			Element max_wounds = doc.createElement("max_wounds");
			max_wounds.appendChild(doc.createTextNode(String.valueOf(character_data.getMaxWounds())));
			rootElement.appendChild(max_wounds);
			
			// Gold
			Element gold = doc.createElement("gold");
			gold.appendChild(doc.createTextNode(String.valueOf(character_data.getGold())));
			rootElement.appendChild(gold);
			
			// Notes
			Element notes = doc.createElement("notes");
			notes.appendChild(doc.createTextNode(String.valueOf(character_data.getNotes())));
			rootElement.appendChild(notes);
			
			// Weapon Skill
			Element weapon_skill = doc.createElement("weapon_skill");
			weapon_skill.appendChild(doc.createTextNode(String.valueOf(character_data.getWeaponSkill())));
			rootElement.appendChild(weapon_skill);
			
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			writer.flush();
			return writer.toString();
			
		} catch (ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error Massage", JOptionPane.ERROR_MESSAGE);
		} catch (TransformerException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error Massage", JOptionPane.ERROR_MESSAGE);
		}
		return "";
	}
	
	public void open() {
		File savefile = UtilityFunctions.doFileOpenDialog("Open Character Data", "xml", true);
		if (savefile == null) {
			return;
		}
		savePath = savefile.getAbsolutePath();
		Document dom;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		CharacterData characterData = CharacterSheetPanel.getCharacterData();
		characterData.resetData();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(savePath);
			
			Element doc = dom.getDocumentElement();
			
			String maxWounds = getTextValue(doc, "max_wounds");
			if (maxWounds != null && ! maxWounds.isEmpty()) {
				characterData.setMaxWounds(Integer.parseInt(maxWounds));
			}
			
			String wounds = getTextValue(doc, "wounds");
			if (wounds != null && ! wounds.isEmpty()) {
				characterData.setWounds(Integer.parseInt(wounds));
			}
			
			String gold = getTextValue(doc, "gold");
			if (gold != null && ! gold.isEmpty()) {
				characterData.setGold(Integer.parseInt(gold));
			}
			
			String notes = getTextValue(doc, "notes");
			if (notes != null && ! notes.isEmpty()) {
				characterData.setNotes(notes);
			}
			
			String weapon_skill = getTextValue(doc, "weapon_skill");
			if (weapon_skill != null && ! weapon_skill.isEmpty()) {
				characterData.setWeaponSkill(Integer.parseInt(weapon_skill));
			}
			
		}
		catch (ParserConfigurationException pce) {
			savePath = null;
			JOptionPane.showMessageDialog(null, pce.getMessage(), "Error Massage", JOptionPane.ERROR_MESSAGE);
		}
		catch (SAXException se) {
			savePath = null;
			JOptionPane.showMessageDialog(null, se.getMessage(), "Error Massage", JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException ioe) {
			savePath = null;
			JOptionPane.showMessageDialog(null, ioe.getMessage(), "Error Massage", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private String getTextValue(Element doc, String tag) {
		String value = null;
	    NodeList nl;
	    nl = doc.getElementsByTagName(tag);
	    if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
	        value = nl.item(0).getFirstChild().getNodeValue();
	    }
	    return value;
	}
}
