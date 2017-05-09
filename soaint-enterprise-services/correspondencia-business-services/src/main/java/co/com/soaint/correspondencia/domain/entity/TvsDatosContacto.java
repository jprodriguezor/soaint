/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jrodriguez
 */
@Entity
@Table(name = "TVS_DATOS_CONTACTO")
@NamedQueries({
    @NamedQuery(name = "TvsDatosContacto.findAll", query = "SELECT t FROM TvsDatosContacto t")})
public class TvsDatosContacto implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_AGENTE")
    private BigDecimal ideAgente;
    @Column(name = "NRO_VIA_GENERADORA")
    private String nroViaGeneradora;
    @Column(name = "NRO_PLACA")
    private String nroPlaca;
    @Column(name = "COD_TIPO_VIA")
    private BigInteger codTipoVia;
    @Column(name = "COD_PREFIJO_CUADRANT")
    private BigInteger codPrefijoCuadrant;
    @Column(name = "COD_POSTAL")
    private String codPostal;
    @Column(name = "DIRECCION")
    private String direccion;
    @Column(name = "CELULAR")
    private String celular;
    @Column(name = "TEL_FIJO1")
    private String telFijo1;
    @Column(name = "TEL_FIJO2")
    private String telFijo2;
    @Column(name = "EXTENSION1")
    private String extension1;
    @Column(name = "EXTENSION2")
    private String extension2;
    @Column(name = "CORR_ELECTRONICO")
    private String corrElectronico;
    @Column(name = "COD_PAIS")
    private String codPais;
    @Column(name = "COD_DEPARTAMENTO")
    private String codDepartamento;
    @Column(name = "COD_MUNICIPIO")
    private String codMunicipio;
    @Column(name = "PROV_ESTADO")
    private String provEstado;
    @Column(name = "CIUDAD")
    private String ciudad;

    public TvsDatosContacto() {
    }

    public TvsDatosContacto(BigDecimal ideAgente) {
        this.ideAgente = ideAgente;
    }

    public BigDecimal getIdeAgente() {
        return ideAgente;
    }

    public void setIdeAgente(BigDecimal ideAgente) {
        this.ideAgente = ideAgente;
    }

    public String getNroViaGeneradora() {
        return nroViaGeneradora;
    }

    public void setNroViaGeneradora(String nroViaGeneradora) {
        this.nroViaGeneradora = nroViaGeneradora;
    }

    public String getNroPlaca() {
        return nroPlaca;
    }

    public void setNroPlaca(String nroPlaca) {
        this.nroPlaca = nroPlaca;
    }

    public BigInteger getCodTipoVia() {
        return codTipoVia;
    }

    public void setCodTipoVia(BigInteger codTipoVia) {
        this.codTipoVia = codTipoVia;
    }

    public BigInteger getCodPrefijoCuadrant() {
        return codPrefijoCuadrant;
    }

    public void setCodPrefijoCuadrant(BigInteger codPrefijoCuadrant) {
        this.codPrefijoCuadrant = codPrefijoCuadrant;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelFijo1() {
        return telFijo1;
    }

    public void setTelFijo1(String telFijo1) {
        this.telFijo1 = telFijo1;
    }

    public String getTelFijo2() {
        return telFijo2;
    }

    public void setTelFijo2(String telFijo2) {
        this.telFijo2 = telFijo2;
    }

    public String getExtension1() {
        return extension1;
    }

    public void setExtension1(String extension1) {
        this.extension1 = extension1;
    }

    public String getExtension2() {
        return extension2;
    }

    public void setExtension2(String extension2) {
        this.extension2 = extension2;
    }

    public String getCorrElectronico() {
        return corrElectronico;
    }

    public void setCorrElectronico(String corrElectronico) {
        this.corrElectronico = corrElectronico;
    }

    public String getCodPais() {
        return codPais;
    }

    public void setCodPais(String codPais) {
        this.codPais = codPais;
    }

    public String getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(String codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    public String getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public String getProvEstado() {
        return provEstado;
    }

    public void setProvEstado(String provEstado) {
        this.provEstado = provEstado;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAgente != null ? ideAgente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TvsDatosContacto)) {
            return false;
        }
        TvsDatosContacto other = (TvsDatosContacto) object;
        if ((this.ideAgente == null && other.ideAgente != null) || (this.ideAgente != null && !this.ideAgente.equals(other.ideAgente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.TvsDatosContacto[ ideAgente=" + ideAgente + " ]";
    }
    
}
