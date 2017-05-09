/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jrodriguez
 */
@Entity
@Table(name = "TVS_DSERIAL")
@NamedQueries({
    @NamedQuery(name = "TvsDserial.findAll", query = "SELECT t FROM TvsDserial t")})
public class TvsDserial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_SERIAL")
    private String ideSerial;
    @Basic(optional = false)
    @Column(name = "COD_SEDE")
    private String codSede;
    @Column(name = "COD_GRUPO")
    private String codGrupo;
    @Column(name = "COD_DEPENDENCIA")
    private String codDependencia;
    @Column(name = "COD_CMC")
    private String codCmc;
    @Basic(optional = false)
    @Column(name = "VAL_ANO")
    private BigInteger valAno;
    @Column(name = "COD_FUNC_RADICA")
    private String codFuncRadica;
    @Column(name = "FEC_CREA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCrea;
    @Column(name = "IND_TIPO_SERIAL")
    private Character indTipoSerial;
    @Column(name = "VAL_CONSECUTIVO_RAD")
    private String valConsecutivoRad;
    @Column(name = "VAL_CONSECUTIVO_PL")
    private String valConsecutivoPl;
    @Column(name = "COD_TIP_CONSECUTIVO")
    private String codTipConsecutivo;

    public TvsDserial() {
    }

    public TvsDserial(String ideSerial) {
        this.ideSerial = ideSerial;
    }

    public TvsDserial(String ideSerial, String codSede, BigInteger valAno) {
        this.ideSerial = ideSerial;
        this.codSede = codSede;
        this.valAno = valAno;
    }

    public String getIdeSerial() {
        return ideSerial;
    }

    public void setIdeSerial(String ideSerial) {
        this.ideSerial = ideSerial;
    }

    public String getCodSede() {
        return codSede;
    }

    public void setCodSede(String codSede) {
        this.codSede = codSede;
    }

    public String getCodGrupo() {
        return codGrupo;
    }

    public void setCodGrupo(String codGrupo) {
        this.codGrupo = codGrupo;
    }

    public String getCodDependencia() {
        return codDependencia;
    }

    public void setCodDependencia(String codDependencia) {
        this.codDependencia = codDependencia;
    }

    public String getCodCmc() {
        return codCmc;
    }

    public void setCodCmc(String codCmc) {
        this.codCmc = codCmc;
    }

    public BigInteger getValAno() {
        return valAno;
    }

    public void setValAno(BigInteger valAno) {
        this.valAno = valAno;
    }

    public String getCodFuncRadica() {
        return codFuncRadica;
    }

    public void setCodFuncRadica(String codFuncRadica) {
        this.codFuncRadica = codFuncRadica;
    }

    public Date getFecCrea() {
        return fecCrea;
    }

    public void setFecCrea(Date fecCrea) {
        this.fecCrea = fecCrea;
    }

    public Character getIndTipoSerial() {
        return indTipoSerial;
    }

    public void setIndTipoSerial(Character indTipoSerial) {
        this.indTipoSerial = indTipoSerial;
    }

    public String getValConsecutivoRad() {
        return valConsecutivoRad;
    }

    public void setValConsecutivoRad(String valConsecutivoRad) {
        this.valConsecutivoRad = valConsecutivoRad;
    }

    public String getValConsecutivoPl() {
        return valConsecutivoPl;
    }

    public void setValConsecutivoPl(String valConsecutivoPl) {
        this.valConsecutivoPl = valConsecutivoPl;
    }

    public String getCodTipConsecutivo() {
        return codTipConsecutivo;
    }

    public void setCodTipConsecutivo(String codTipConsecutivo) {
        this.codTipConsecutivo = codTipConsecutivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSerial != null ? ideSerial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TvsDserial)) {
            return false;
        }
        TvsDserial other = (TvsDserial) object;
        if ((this.ideSerial == null && other.ideSerial != null) || (this.ideSerial != null && !this.ideSerial.equals(other.ideSerial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.TvsDserial[ ideSerial=" + ideSerial + " ]";
    }
    
}
