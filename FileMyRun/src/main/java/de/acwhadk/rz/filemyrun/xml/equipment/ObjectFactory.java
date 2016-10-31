package de.acwhadk.rz.filemyrun.xml.equipment;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * Object factory needed for jaxb.
 * 
 * @author Ralf
 *
 */
@XmlRegistry
public class ObjectFactory {

   private final static QName _EquipmentContainer_QNAME = new QName("http://rz.acwhadk.de/file/filemyrun", "EquipmentContainer");
   
   public ObjectFactory() {
    }
    
	public EquipmentContainer createEquipmentContainer() {
		return new EquipmentContainer();
	}
	
    @XmlElementDecl(namespace = "http://rz.acwhadk.de/data/filemyrun", name = "TrainingActivity")
    public JAXBElement<EquipmentContainer> createEquipmentContainer(EquipmentContainer value) {
        return new JAXBElement<EquipmentContainer>(_EquipmentContainer_QNAME, EquipmentContainer.class, null, value);
    }
}
