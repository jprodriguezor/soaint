package co.com.soaint.correspondencia;

import org.h2.tools.RunScript;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jorge on 15/05/2017.
 */

public class JPAHibernateTest {

    protected static EntityManagerFactory emf;
    protected static EntityManager em;

    @BeforeClass
    public static void init() throws FileNotFoundException, SQLException {
        emf = Persistence.createEntityManagerFactory("servicios-correspondencia-h2-unit-test");
        em = emf.createEntityManager();
    }

    @Before
    public void initializeDatabase(){
        Session session = em.unwrap(Session.class);
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws RuntimeException {
                try {
                    File script = new File(getClass().getResource("/data.sql").getFile());
                    RunScript.execute(connection, new FileReader(script));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @AfterClass
    public static void tearDown(){
        em.clear();
        em.close();
        emf.close();
    }

    @Test
    public void testDummy_success() {
        assertEquals(1, 1);
    }


}
