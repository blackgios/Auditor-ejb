/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.zazil.auditor.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import mx.zazil.security.Alta;
import mx.zazil.security.Baja;
import mx.zazil.security.Cambio;

/**
 *
 * @author Hector Rodriguez
 */
@Entity
@Table(name = "CamposCacheTabla")
@NamedQueries({
    @NamedQuery(name = "CamposCacheTabla.findByAll", query = "select c from CamposCacheTabla as c"),
    @NamedQuery(name = "CamposCacheTabla.findById", query = "select c from CamposCacheTabla as c where c.id =: id")
})
@Alta
@Baja
@Cambio
public class CamposCacheTabla implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "columna", length = 50)
    private String columna;
    @Column(name = "valor", length = 255)
    private String valor;
    @ManyToOne
    private CacheTabla idRegistro;

    public CamposCacheTabla() {
    }

    public CamposCacheTabla(Long id) {
        this.id = id;
    }

    public String getColumna() {
        return columna;
    }

    public void setColumna(String columna) {
        this.columna = columna;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public CacheTabla getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(CacheTabla idRegistro) {
        this.idRegistro = idRegistro;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CamposCacheTabla)) {
            return false;
        }
        CamposCacheTabla other = (CamposCacheTabla) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.zazil.auditor.entity.CamposCacheTabla[ id=" + id + " ]";
    }
    
}
