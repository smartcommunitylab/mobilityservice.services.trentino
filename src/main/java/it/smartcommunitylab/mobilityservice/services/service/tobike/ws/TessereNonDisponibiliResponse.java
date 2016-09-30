
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
 *         &lt;element name="TessereNonDisponibiliResult" type="{c://inetub/wwwroot/webservice/Service.asmx}ArrayOfString" minOccurs="0"/>
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
    "tessereNonDisponibiliResult"
})
@XmlRootElement(name = "TessereNonDisponibiliResponse")
public class TessereNonDisponibiliResponse {

    @XmlElement(name = "TessereNonDisponibiliResult")
    protected ArrayOfString tessereNonDisponibiliResult;

    /**
     * Gets the value of the tessereNonDisponibiliResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getTessereNonDisponibiliResult() {
        return tessereNonDisponibiliResult;
    }

    /**
     * Sets the value of the tessereNonDisponibiliResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setTessereNonDisponibiliResult(ArrayOfString value) {
        this.tessereNonDisponibiliResult = value;
    }

}
