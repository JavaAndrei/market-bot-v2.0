package pro.keenetic.marketbot.bot.market_bot.markets.exmo;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ExmoSecurity implements MarketSecurity {

    private final String PUBLIC_KEY;
    private final String SECRET_KEY;

    public ExmoSecurity(String PUBLIC_KEY, String SECRET_KEY) {
        this.PUBLIC_KEY = PUBLIC_KEY;
        this.SECRET_KEY = SECRET_KEY;
    }

    private byte[] sign(String postData) {

        // Create a new secret key
        SecretKeySpec secretKeySpec;
        secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA512");

        // Create a new mac
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA512");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No such algorithm exception: " + e.getMessage());
            return null;
        }

        // Init mac with key.
        try {
            mac.init(secretKeySpec);
        } catch (InvalidKeyException e) {
            System.err.println("Invalid key exception: " + e.getMessage());
            return null;
        }

        // Encode the post data by the secret and encode the result as base64.
        try {
            return mac.doFinal(postData.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding exception: " + e.getMessage());
            return null;
        }
    }

    public String signWS(String postData) {
        return Base64.getEncoder().encodeToString(sign(postData));
    }

    public String signRest(String postData) {
        return Hex.encodeHexString(sign(postData));
    }

    public String getPublicKey() {
        return PUBLIC_KEY;
    }
}

