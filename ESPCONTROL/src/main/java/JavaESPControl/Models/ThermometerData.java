/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaESPControl.Models;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 *
 * @author misustefan
 */
@Entity
@Table(name = "thermometer_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThermometerData implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_RETRIEVED")
    private Integer idRetrieved;
    @Column(name = "Temperature")
    private Integer temperature;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Humidity")
    private BigDecimal humidity;
    @Basic(optional = false)
    @Column(name = "Date")
    @Temporal(TemporalType.DATE)
    private Date date;


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRetrieved != null ? idRetrieved.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ThermometerData)) {
            return false;
        }
        ThermometerData other = (ThermometerData) object;
        if ((this.idRetrieved == null && other.idRetrieved != null) || (this.idRetrieved != null && !this.idRetrieved.equals(other.idRetrieved))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JavaESPControl.Models.ThermometerData[ idRetrieved=" + idRetrieved + " ]";
    }
    
}
