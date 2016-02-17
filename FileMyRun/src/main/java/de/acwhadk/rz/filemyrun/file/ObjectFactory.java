package de.acwhadk.rz.filemyrun.file;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

   private final static QName _TrainingFileContainer_QNAME = new QName("http://rz.acwhadk.de/file/filemyrun", "TrainingFileContainer");
   
   public ObjectFactory() {
    }
    
	public TrainingFileContainer createTrainingFileContainer() {
		return new TrainingFileContainer();
	}
	
    @XmlElementDecl(namespace = "http://rz.acwhadk.de/data/filemyrun", name = "TrainingActivity")
    public JAXBElement<TrainingFileContainer> createTrainingFileContainer(TrainingFileContainer value) {
        return new JAXBElement<TrainingFileContainer>(_TrainingFileContainer_QNAME, TrainingFileContainer.class, null, value);
    }
}
