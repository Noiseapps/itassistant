
package com.noiseapps.itassistant.model.jira.issues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class AvatarUrls {

    @SerializedName("16x16")
    @Expose
    private String _16x16;
    @SerializedName("24x24")
    @Expose
    private String _24x24;
    @SerializedName("32x32")
    @Expose
    private String _32x32;
    @SerializedName("48x48")
    @Expose
    private String _48x48;

    /**
     * No args constructor for use in serialization
     * 
     */
    public AvatarUrls() {
    }

    /**
     * 
     * @param _24x24
     * @param _32x32
     * @param _48x48
     * @param _16x16
     */
    public AvatarUrls(String _16x16, String _24x24, String _32x32, String _48x48) {
        this._16x16 = _16x16;
        this._24x24 = _24x24;
        this._32x32 = _32x32;
        this._48x48 = _48x48;
    }

    /**
     * 
     * @return
     *     The _16x16
     */
    public String get16x16() {
        return _16x16;
    }

    /**
     * 
     * @param _16x16
     *     The 16x16
     */
    public void set16x16(String _16x16) {
        this._16x16 = _16x16;
    }

    /**
     * 
     * @return
     *     The _24x24
     */
    public String get24x24() {
        return _24x24;
    }

    /**
     * 
     * @param _24x24
     *     The 24x24
     */
    public void set24x24(String _24x24) {
        this._24x24 = _24x24;
    }

    /**
     * 
     * @return
     *     The _32x32
     */
    public String get32x32() {
        return _32x32;
    }

    /**
     * 
     * @param _32x32
     *     The 32x32
     */
    public void set32x32(String _32x32) {
        this._32x32 = _32x32;
    }

    /**
     * 
     * @return
     *     The _48x48
     */
    public String get48x48() {
        return _48x48;
    }

    /**
     * 
     * @param _48x48
     *     The 48x48
     */
    public void set48x48(String _48x48) {
        this._48x48 = _48x48;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(_16x16).append(_24x24).append(_32x32).append(_48x48).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AvatarUrls) == false) {
            return false;
        }
        AvatarUrls rhs = ((AvatarUrls) other);
        return new EqualsBuilder().append(_16x16, rhs._16x16).append(_24x24, rhs._24x24).append(_32x32, rhs._32x32).append(_48x48, rhs._48x48).isEquals();
    }

}
