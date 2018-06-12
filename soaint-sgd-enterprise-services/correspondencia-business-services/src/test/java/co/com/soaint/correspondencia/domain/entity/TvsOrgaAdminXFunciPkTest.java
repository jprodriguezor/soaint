package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by yleon on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class TvsOrgaAdminXFunciPkTest extends JPAHibernateContextTest {

    @Test
    public void test_findCodOrgaAdmiByIdeFunci_success() {
        //given
        String namedQuery = "TvsOrgaAdminXFunciPk.findCodOrgaAdmiByIdeFunci";
        BigInteger idFuncionario = BigInteger.ONE;

        //when
        List<String> codOrgaAdmiList = createNamedQuery(namedQuery, String.class)
                .setParameter("IDE_FUNCI", idFuncionario)
                .getResultList();

        //then
        assertNotNull(codOrgaAdmiList);
        assertEquals(1, codOrgaAdmiList.size());
    }
}
