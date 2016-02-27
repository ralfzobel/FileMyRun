package de.acwhadk.rz.filemyrun;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.xml.bind.JAXBException;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import de.acwhadk.rz.filemyrun.equipment.EquipmentContainer;
import de.acwhadk.rz.filemyrun.equipment.EquipmentDefinition;
import de.acwhadk.rz.filemyrun.equipment.EquipmentItem;
import de.acwhadk.rz.filemyrun.equipment.EquipmentUsedEntry;
import de.acwhadk.rz.filemyrun.xml.EquipmentContainerToXML;

public class TestEquipment {

	@Test
	public void test() throws Exception {
		EquipmentContainer container = new EquipmentContainer();
		
		EquipmentDefinition def1 = new EquipmentDefinition("Schuhe");
		def1.getItems().add(new EquipmentItem(1, "Kinvara 6"));
		def1.getItems().add(new EquipmentItem(2, "Mizuno Sayonara"));
		container.getEquipmentDefinitionList().add(def1);
		
		EquipmentDefinition def2 = new EquipmentDefinition("Trikot");
		def2.getItems().add(new EquipmentItem(3, "WL"));
		def2.getItems().add(new EquipmentItem(4, "Unterhemd+Trikot"));
		container.getEquipmentDefinitionList().add(def2);
		
		EquipmentUsedEntry use1 = new EquipmentUsedEntry();
		use1.setActivity(new Date());
		use1.setId(1);
		use1.setType("Schuhe");
		use1.setUsedDistance(10.0);
		use1.setUsedTime(3600);
		container.getequipmentUsedEntryList().add(use1);
		
		try {
			EquipmentContainerToXML.save("src/test/resources/equip.xml", container);
			EquipmentContainer container2 = EquipmentContainerToXML.load(new File("src/test/resources/equip.xml"));
			assertEquals(container.getCurrentId(), container2.getCurrentId());
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
			throw e;
		}
		
	}
}
