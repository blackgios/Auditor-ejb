/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.zazil.auditor.sesion;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import mx.zazil.auditor.MensajeAudita;
import mx.zazil.auditor.Transaccion;
import mx.zazil.auditor.entity.CacheTabla;
import mx.zazil.auditor.entity.CamposCacheTabla;
import mx.zazil.auditor.entity.DetallesModificacion;
import mx.zazil.auditor.entity.Modificacion;
import mx.zazil.auditor.entity.detalle.TipoModificacionDetalle;
import mx.zazil.excepcion.OperacionInvalidaException;
import mx.zazil.security.UsuarioBalam;

/**
 *
 * @author Hector Rodriguez
 */
@Stateless(name = "Audita")
public class AuditaBean implements AuditaRemote, AuditaLocal {

    @PersistenceContext
    private EntityManager em;

    public void alta(MensajeAudita ma) throws OperacionInvalidaException {

        Modificacion m = new Modificacion();
        Transaccion t = ma.getTransaccion();
        m.setFecha(t.getTime());
        m.setUsuario(t.getUsuario().getIdUsuario());
        m.setFolio(t.getTransaccion());
        m.setTipoModificacion(TipoModificacionDetalle.Alta);
        m.setModulo(t.getModulo());

        CacheTabla ct = this.generaDetallesAlta(ma, m);
//
        // em.persist(m);
        em.persist(ct);

    }

    public HashMap<String, String> getValuesFor(Object o) {
        LinkedHashMap<String, String> values = new LinkedHashMap<String, String>();
        Class c = o.getClass();
        Field[] fs = c.getDeclaredFields();
        for (Field f : fs) {
            int mod = f.getModifiers();
            if (!(Modifier.isStatic(mod) || Modifier.isFinal(mod) || Modifier.isTransient(mod) || Modifier.isVolatile(mod))) {
                try {
                    //TODO Aqui va verificar si tiene la anotacion auditable, o si toda la clase es auditable

                    String fi = f.getName();
                    fi = Character.toUpperCase(fi.charAt(0)) + fi.substring(1);

                    Method me;

                    me = c.getMethod("get" + fi, null);
                    //Logger.getAnonymousLogger().log(Level.INFO,
                    //      "Annotations; " + java.util.Arrays.deepToString(f.getType().getAnnotations()));
                    Object ob = null;
                    try {
                        ob = me.invoke(o, null);
                    } catch (IllegalAccessException ex) {
                       // Logger.getLogger(AuditaBean.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        //Logger.getLogger(AuditaBean.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NullPointerException npe) {
                    }

                    if (!f.getType().isAnnotationPresent(Entity.class)) {
                        //TODO ver que pasa con las listas....
                        values.put(f.getName(), ob != null ? ob.toString() : null);
                    } else {

                        Long id = getId(ob);

                        Logger.getLogger("debugger").log(Level.FINE, "el id: " + id + " de " + ob);
                        values.put(f.getName(), id + "");
                    }
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(AuditaBean.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(AuditaBean.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(AuditaQueueBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return values;
    }

    private Long getId(Object o) {
        if(o == null)
            return -1L;
        Class c = o.getClass();
        Field[] fs = c.getDeclaredFields();
        for (Field f : fs) {
            if (f.getAnnotation(Id.class) != null) {
                try {
                    String fi = f.getName();
                    fi = Character.toUpperCase(fi.charAt(0)) + fi.substring(1);
                    Method me;

                    me = c.getMethod("get" + fi, null);
                    return Long.parseLong(me.invoke(o, null).toString());
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(AuditaBean.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(AuditaBean.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(AuditaBean.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(AuditaBean.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(AuditaBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    private CacheTabla generaDetallesAlta(MensajeAudita ma, Modificacion m) {
        CacheTabla ct = new CacheTabla();

        Object o = ma.getRegistro();
        Class c = o.getClass();
        Long id = this.getId(o);


        m.setTabla(c.getSimpleName());
        m.setIdEnTabla(id);

        HashMap<String, String> values = getValuesFor(o);
        ct.setIdCambio(m);
        ct.setIdRegistro(id);
        ct.setTabla(c.getSimpleName());
        List<CamposCacheTabla> l = new LinkedList<CamposCacheTabla>();
        ct.setCamposCacheTablas(l);

        for (String f : values.keySet()) {
            String valor = values.get(f);
            DetallesModificacion dm = new DetallesModificacion();
            dm.setIdCambio(m);
            dm.setColumna(f);
            dm.setValor(valor);
            m.addDetalle(dm);

            CamposCacheTabla cct = new CamposCacheTabla();
            cct.setColumna(f);
            cct.setValor(valor);
            cct.setIdRegistro(ct);
            l.add(cct);

        }
        return ct;
    }

    public void baja(MensajeAudita ma) throws OperacionInvalidaException {
        Modificacion m = new Modificacion();
        Transaccion t = ma.getTransaccion();
        Object o = ma.getRegistro();
        Class c = o.getClass();
        Long id = this.getId(o);


        m.setTabla(c.getSimpleName());
        m.setIdEnTabla(id);
        m.setTipoModificacion(TipoModificacionDetalle.Baja);
        m.setFolio(t.getTransaccion());
        m.setModulo(t.getModulo());
        m.setFecha(t.getTime());
        m.setUsuario(t.getUsuario().getIdUsuario());

        CacheTabla ct = findCacheTabla(c.getSimpleName(), id);
        if(ct!=null){
            ct.setIdCambio(null);
            em.merge(ct);
            em.remove(ct);
        }
        em.persist(m);
    }

    public void cambio(MensajeAudita ma) throws OperacionInvalidaException {

        Modificacion m = new Modificacion();
        Transaccion t = ma.getTransaccion();
        Object o = ma.getRegistro();
        Class c = o.getClass();
        Long id = this.getId(o);


        m.setTabla(c.getSimpleName());
        m.setIdEnTabla(id);
        m.setTipoModificacion(TipoModificacionDetalle.Cambio);
        m.setFolio(t.getTransaccion());
        m.setModulo(t.getModulo());
        m.setFecha(t.getTime());
        m.setUsuario(t.getUsuario().getIdUsuario());

        CacheTabla ct = findCacheTabla(c.getSimpleName(), id);
        ct.setIdCambio(m);
        HashMap<String, String> hm = this.getValuesFor(o);
        for (CamposCacheTabla cct : ct.getCamposCacheTablas()) {
            String nuevoValor = hm.get(cct.getColumna());
            if (!cct.getValor().equals(nuevoValor)) {
                cct.setValor(nuevoValor);
                DetallesModificacion dm = new DetallesModificacion();
                dm.setIdCambio(m);
                dm.setColumna(cct.getColumna());
                dm.setValor(nuevoValor);
                m.addDetalle(dm);

            }
        }
        //em.persist(m);
        em.merge(ct);

    }

    public void lista(UsuarioBalam u, Class clase) throws OperacionInvalidaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void log(UsuarioBalam u, String mensaje) throws OperacionInvalidaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Boolean ping() {
        return true;
    }

    private CacheTabla findCacheTabla(String tabla, Long id) {

        Logger.getAnonymousLogger().log(Level.INFO, "Buscando el cache de: " + id + " en la tabla " + tabla);
        try {
            Query q = em.createNamedQuery("CacheTabla.findByIdRegistro");
            q.setParameter("idRegistro", id);
            q.setParameter("tabla", tabla);
            return (CacheTabla) q.getSingleResult();
        } catch (javax.persistence.NoResultException nre) {
            return null;
        }
    }
}
