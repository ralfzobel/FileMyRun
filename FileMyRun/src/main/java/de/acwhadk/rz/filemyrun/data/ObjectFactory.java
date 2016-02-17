package de.acwhadk.rz.filemyrun.data;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

   private final static QName _TrainingActivity_QNAME = new QName("http://rz.acwhadk.de/data/filemyrun", "TrainingActivity");
   
   public ObjectFactory() {
    }
    
	public TrainingActivity createTrainingActivity() {
		return new TrainingActivity();
	}
	
    @XmlElementDecl(namespace = "http://rz.acwhadk.de/data/filemyrun", name = "TrainingActivity")
    public JAXBElement<TrainingActivity> createTrainingActivity(TrainingActivity value) {
        return new JAXBElement<TrainingActivity>(_TrainingActivity_QNAME, TrainingActivity.class, null, value);
    }
}
