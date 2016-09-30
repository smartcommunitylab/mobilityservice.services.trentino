
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
 *         &lt;element name="TipiTesseraResult" type="{c://inetub/wwwroot/webservice/Service.asmx}ArrayOfTipoTessera" minOccurs="0"/>
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
    "tipiTesseraResult"
})
@XmlRootElement(name = "TipiTesseraResponse")
public class TipiTesseraResponse {

    @XmlElement(name = "TipiTesseraResult")
    protected ArrayOfTipoTessera tipiTesseraResult;

    /**
     * Gets the value of the tipiTesseraResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTipoTessera }
     *     
     */
    public ArrayOfTipoTessera getTipiTesseraResult() {
        return tipiTesseraResult;
    }

    /**
     * Sets the value of the tipiTesseraResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTipoTessera }
     *     
     */
    public void setTipiTesseraResult(ArrayOfTipoTessera value) {
        this.tipiTesseraResult = value;
    }

}
