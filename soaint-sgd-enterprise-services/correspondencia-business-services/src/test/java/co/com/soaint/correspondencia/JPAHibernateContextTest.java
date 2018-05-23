package co.com.soaint.correspondencia;

import lombok.extern.log4j.Log4j2;
import org.h2.tools.RunScript;
import org.hibernate.Session;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileReader;

@Log4j2
@Transactional
public abstract class JPAHibernateContextTest {

    @PersistenceContext
    protected EntityManager em;

    @Before
    public void initializeDatabase(){
        Session session = em.unwrap(Session.class);
        session.doWork((connection) -> {
            try {
                File script = new File(getClass().getResource("/data.sql").getFile());
                RunScript.execute(connection, new FileReader(script));
            }catch (Exception ex){
                log.error("Test - a system error has occurred", ex);
            }
        });
    }

    protected <T> TypedQuery<T> createNamedQuery(String namedQuery, Class<T> clazz) {
        return em.createNamedQuery(namedQuery, clazz);
    }

    protected Query createNamedQuery(String namedQuery) {
        return em.createNamedQuery(namedQuery);
    }
}
