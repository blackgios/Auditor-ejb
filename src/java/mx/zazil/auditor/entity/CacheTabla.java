/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.zazil.auditor.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Hector Rodriguez
 */
@Entity
@Table(name="CacheTabla")
@NamedQueries({
    @NamedQuery(name = "CacheTabla.findByAll", query = "select d from CacheTabla as d where d.tabla =: tabla"),
    @NamedQuery(name = "CacheTabla.findByIdRegistro", query = "select c from CacheTabla as c where c.idRegistro =:idRegistro and c.tabla =: tabla")
})
public class CacheTabla implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name="idRegistro")
    private long idRegistro;
    
    @Column(name = "tabla", length = 30)
    private String tabla;
    
    @OneToOne(targetEntity = Modificacion.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "idCambio",referencedColumnName = "idCambio")
    private Modificacion idCambio;
    
    @OneToMany(mappedBy = "idRegistro", cascade = CascadeType.ALL)
    private List<CamposCacheTabla> camposCacheTablas;

    public CacheTabla() {
    }

    public CacheTabla(Long idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Long getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Long idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public Modificacion getIdCambio() {
        return idCambio;
    }

    public void setIdCambio(Modificacion idCambio) {
        this.idCambio = idCambio;
    }

    public List<CamposCacheTabla> getCamposCacheTablas() {
        return camposCacheTablas;
    }

    public void setCamposCacheTablas(List<CamposCacheTabla> camposCacheTablas) {
        this.camposCacheTablas = camposCacheTablas;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CacheTabla)) {
            return false;
        }
        CacheTabla other = (CacheTabla) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.zazil.auditor.entity.CacheTabla[ id=" + id + " ]";
    }
    
}
