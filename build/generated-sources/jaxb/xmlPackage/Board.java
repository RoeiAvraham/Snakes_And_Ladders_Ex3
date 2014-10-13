//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.14 at 12:48:07 AM EEST 
//


package xmlPackage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for board complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="board">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cells" type="{}cells"/>
 *         &lt;element name="ladders" type="{}ladders"/>
 *         &lt;element name="snakes" type="{}snakes"/>
 *       &lt;/sequence>
 *       &lt;attribute name="size" use="required" type="{}board_size" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "board", propOrder = {
    "cells",
    "ladders",
    "snakes"
})
public class Board {

    @XmlElement(required = true)
    protected Cells cells;
    @XmlElement(required = true)
    protected Ladders ladders;
    @XmlElement(required = true)
    protected Snakes snakes;
    @XmlAttribute(name = "size", required = true)
    protected int size;

    /**
     * Gets the value of the cells property.
     * 
     * @return
     *     possible object is
     *     {@link Cells }
     *     
     */
    public Cells getCells() {
        return cells;
    }

    /**
     * Sets the value of the cells property.
     * 
     * @param value
     *     allowed object is
     *     {@link Cells }
     *     
     */
    public void setCells(Cells value) {
        this.cells = value;
    }

    /**
     * Gets the value of the ladders property.
     * 
     * @return
     *     possible object is
     *     {@link Ladders }
     *     
     */
    public Ladders getLadders() {
        return ladders;
    }

    /**
     * Sets the value of the ladders property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ladders }
     *     
     */
    public void setLadders(Ladders value) {
        this.ladders = value;
    }

    /**
     * Gets the value of the snakes property.
     * 
     * @return
     *     possible object is
     *     {@link Snakes }
     *     
     */
    public Snakes getSnakes() {
        return snakes;
    }

    /**
     * Sets the value of the snakes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Snakes }
     *     
     */
    public void setSnakes(Snakes value) {
        this.snakes = value;
    }

    /**
     * Gets the value of the size property.
     * 
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     */
    public void setSize(int value) {
        this.size = value;
    }

}
