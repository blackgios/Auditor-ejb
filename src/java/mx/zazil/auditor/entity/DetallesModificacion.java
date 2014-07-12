/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.zazil.auditor.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Hector Rodriguez
 */
@Entity
@Table(name = "DetallesModificacion")
@NamedQueries({
    @NamedQuery(name = "DetallesModificacion.findByAll",query = "SELECT d FROM DetallesModificacion AS d"),
    @NamedQuery(name = "DetallesModificacion.finByIdDetalleModificacion", query = "SELECT d FROM DetallesModificacion AS d WHERE d.idDetalleModificacion =: idDetalleModificacion")
})
public class DetallesModificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "idDetalleModificacion", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idDetalleModificacion;
    @Column(name = "columna", length = 50)
    private String columna;
    @Column(name = "valor", length = 255)
    private String valor;
    @OneToOne(targetEntity = Modificacion.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "idCambio", referencedColumnName = "idCambio")
    @ManyToOne
    private Modificacion idCambio;

    public DetallesModificacion() {
    }

    public DetallesModificacion(Long idDetalleModificacion) {
        this.idDetalleModificacion = idDetalleModificacion;
    }
    
    public Long getIdDetalleModificacion() {
        return idDetalleModificacion;
    }

    public void setIdDetalleModificacion(Long id) {
        this.idDetalleModificacion = id;
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

    public Modificacion getIdCambio() {
        return idCambio;
    }

    public void setIdCambio(Modificacion idCambio) {
        this.idCambio = idCambio;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int)(this.idDetalleModificacion%Integer.MAX_VALUE);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if(object == null) return false;
        if(this.getClass() != object.getClass())return false;
        
        final DetallesModificacion other = (DetallesModificacion) object;
        if(this.idDetalleModificacion != other.getIdDetalleModificacion())return false;
        
        return true;
    }

    @Override
    public String toString() {
        //"tabla: DetallesModificacion; registro:> idRegistro:  "
        return "mx.zazil.auditor.entity.DetallesModificacion[ id=" + idDetalleModificacion + " ]";
    }
    
}
