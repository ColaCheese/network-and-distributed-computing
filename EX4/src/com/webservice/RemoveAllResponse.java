package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>removeAllResponse complex type 的Java类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="removeAllResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "removeAllResponse", propOrder = {
        "_return"
})

public class RemoveAllResponse {

    @XmlElement(name = "return")
    protected boolean _return;

    /**
     * 获取return属性的值。
     *
     * @return possible object is {@link Boolean }
     */
    public boolean isReturn() {
        return _return;
    }

    /**
     * 设置return属性的值。
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setReturn(boolean value) {
        this._return = value;
    }
}