import co.com.soaint.correspondencia.Palindrome;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Jorge on 15/05/2017.
 */
public class TestPalindrome {

    @Test
    public void whenEmptyString_thenAccept() {
        Palindrome palindromeTester = new Palindrome();
        assertTrue(palindromeTester.isPalindrome(""));
    }
}
