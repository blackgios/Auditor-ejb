/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.zazil.auditor.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import mx.zazil.auditor.entity.detalle.TipoModificacionDetalle;

/**
 *
 * @author Hector Rodriguez
 */
@Entity
@Table(name = "Modificacion")
@NamedQueries({
    @NamedQuery(name = "Modificacion.findByIdRegistro", query = "select m from Modificacion as m where m.idCambio =: idCambio")
})
public class Modificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long idCambio;
    
    private String modulo;
    
    private Long folio;
    
    @OneToMany(mappedBy = "idCambio", cascade = CascadeType.ALL)
    private List<DetallesModificacion> detalles = new LinkedList<DetallesModificacion>();
    @Column(name = "tipoModificacion")
    @Enumerated(EnumType.STRING)
    private TipoModificacionDetalle tipoModificacion = TipoModificacionDetalle.Alta;
    @Column(name = "tabla", length = 30)
    private String tabla;
    @Column(name = "idEnTabla")
    private Long idEnTabla;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private String usuario;

    public Modificacion() {
    }
    
    public Modificacion(Long idCambio) {
        this.idCambio = idCambio;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public Long getFolio() {
        return folio;
    }

    public void setFolio(Long folio) {
        this.folio = folio;
    }
    
    public void addDetalle(DetallesModificacion detallesModificacion){
        getDetalles().add(detallesModificacion);
    }

    public List<DetallesModificacion> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallesModificacion> detalles) {
        this.detalles = detalles;
    }

    public TipoModificacionDetalle getTipoModificacion() {
        return tipoModificacion;
    }

    public void setTipoModificacion(TipoModificacionDetalle tipoModificacion) {
        this.tipoModificacion = tipoModificacion;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public Long getIdEnTabla() {
        return idEnTabla;
    }

    public void setIdEnTabla(Long idEnTabla) {
        this.idEnTabla = idEnTabla;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = (int)(29 * hash +this.idCambio % Integer.MAX_VALUE);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if(object == null){
            return false;
        }
        if(getClass() != object.getClass()){
            return false;
        }
        final Modificacion other = (Modificacion) object;
        if(this.idCambio != other.idCambio){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.zazil.auditor.entity.Modificacion[]";
    }
    
}
