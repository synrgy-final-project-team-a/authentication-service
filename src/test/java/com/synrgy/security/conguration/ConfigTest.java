import static org.junit.Assert.assertEquals;

import java.util.Date;

import com.synrgy.security.configuration.Config;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConfigTest {

    private Config config;
    private Date date;

    @Before
    public void setUp() {
        config = new Config();
        date = new Date();
    }

    @Test
    public void testCodeNotFound() {
        assertEquals("404", config.getCode_notFound());
    }

    @Test
    public void testCodeRequired() {
        assertEquals("403", config.getCodeRequired());
    }

    @Test
    public void testIsRequired() {
        assertEquals(" is Required", config.getIsRequired());
    }

    @Test
    public void testCodeSukses() {
        assertEquals("200", config.getCode_sukses());
    }

    @Test
    public void testCodeServer() {
        assertEquals("500", config.getCode_server());
    }

    @Test
    public void testCodeNull() {
        assertEquals("1", config.getCode_null());
    }

    @Test
    public void testMessageSukses() {
        assertEquals("sukses", config.getMessage_sukses());
    }

}

