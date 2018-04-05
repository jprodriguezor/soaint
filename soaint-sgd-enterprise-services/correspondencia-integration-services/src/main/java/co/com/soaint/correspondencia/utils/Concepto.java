package co.com.soaint.correspondencia.utils;

import java.math.BigDecimal;

/**
 * Created by esaliaga on 26/03/2018.
 */
public class Concepto {

    private String claveProdServ;
    private String noIdentificacion;
    private BigDecimal cantidad;
    private String claveUnidad;
    private String unidad;
    private String descripcion;
    private BigDecimal valorUnitario;
    private BigDecimal importe;
    private BigDecimal descuento;
    private String impuestoTipo;
    private BigDecimal impuestoImporte;

    public Concepto(String claveProdServ, String noIdentificacion, BigDecimal cantidad, String claveUnidad, String unidad, String descripcion, BigDecimal valorUnitario, BigDecimal importe, BigDecimal descuento, String impuestoTipo, BigDecimal impuestoImporte) {
        this.claveProdServ = claveProdServ;
        this.noIdentificacion = noIdentificacion;
        this.cantidad = cantidad;
        this.claveUnidad = claveUnidad;
        this.unidad = unidad;
        this.descripcion = descripcion;
        this.valorUnitario = valorUnitario;
        this.importe = importe;
        this.descuento = descuento;
        this.impuestoTipo = impuestoTipo;
        this.impuestoImporte = impuestoImporte;
    }

    public String getClaveProdServ() {
        return claveProdServ;
    }

    public void setClaveProdServ(String claveProdServ) {
        this.claveProdServ = claveProdServ;
    }

    public String getNoIdentificacion() {
        return noIdentificacion;
    }

    public void setNoIdentificacion(String noIdentificacion) {
        this.noIdentificacion = noIdentificacion;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getClaveUnidad() {
        return claveUnidad;
    }

    public void setClaveUnidad(String claveUnidad) {
        this.claveUnidad = claveUnidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public String getImpuestoTipo() {
        return impuestoTipo;
    }

    public void setImpuestoTipo(String impuestoTipo) {
        this.impuestoTipo = impuestoTipo;
    }

    public BigDecimal getImpuestoImporte() {
        return impuestoImporte;
    }

    public void setImpuestoImporte(BigDecimal impuestoImporte) {
        this.impuestoImporte = impuestoImporte;
    }
}
