
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
 *         &lt;element name="DatiUtenteAPPResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "datiUtenteAPPResult"
})
@XmlRootElement(name = "DatiUtenteAPPResponse")
public class DatiUtenteAPPResponse {

    @XmlElement(name = "DatiUtenteAPPResult")
    protected String datiUtenteAPPResult;

    /**
     * Gets the value of the datiUtenteAPPResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatiUtenteAPPResult() {
        return datiUtenteAPPResult;
    }

    /**
     * Sets the value of the datiUtenteAPPResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatiUtenteAPPResult(String value) {
        this.datiUtenteAPPResult = value;
    }

}
