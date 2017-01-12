package cinetcore.android_cinetpay_helpers;

import com.istat.cinetcore.cinetpay.sdk.Purchase;
import com.istat.cinetpay.helpers.CinetPayHelper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void parsePurchase() throws Exception {
        Article article = new Article();
        article.name = "Bananas";
        article.amount = 50;
        article.reference = Math.random() + "";
        Purchase purchase = CinetPayHelper.parsePurchase(article);
        assertEquals(article.name, "Bananas");
    }

    class Article {
        @CinetPayHelper.PurchaseTitle
        String name;
        @CinetPayHelper.PurchaseAmount
        int amount;
        @CinetPayHelper.PurchaseReference
        String reference;
    }
}