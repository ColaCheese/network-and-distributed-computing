package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ParseException complex type 的Java类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="ParseException">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="errorOffset" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParseException", propOrder = {
        "errorOffset",
        "message"
})

public class ParseException {

    protected int errorOffset;
    protected String message;

    /**
     * 获取errorOffset属性的值。
     *
     * @return possible object is {@link Integer }
     */
    public int getErrorOffset() {
        return errorOffset;
    }

    /**
     * 设置errorOffset属性的值。
     *
     * @return possible object is {@link Integer }
     */
    public void setErrorOffset(int value) {
        this.errorOffset = value;
    }

    /**
     * 获取message属性的值。
     *
     * @return possible object is {@link String }
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置message属性的值。
     *
     * @param value allowed object is {@link String }
     */
    public void setMessage(String value) {
        this.message = value;
    }

}