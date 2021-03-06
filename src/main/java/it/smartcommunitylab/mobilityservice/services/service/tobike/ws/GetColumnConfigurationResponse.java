
package it.smartcommunitylab.mobilityservice.services.service.tobike.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetColumnConfigurationResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getColumnConfigurationResult"
})
@XmlRootElement(name = "GetColumnConfigurationResponse")
public class GetColumnConfigurationResponse {

    @XmlElement(name = "GetColumnConfigurationResult")
    protected int getColumnConfigurationResult;

    /**
     * Gets the value of the getColumnConfigurationResult property.
     * 
     */
    public int getGetColumnConfigurationResult() {
        return getColumnConfigurationResult;
    }

    /**
     * Sets the value of the getColumnConfigurationResult property.
     * 
     */
    public void setGetColumnConfigurationResult(int value) {
        this.getColumnConfigurationResult = value;
    }

}
