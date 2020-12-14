import java.util.Random;

public class NameGenerator {

    protected static String getSaltString(int length) {
        final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();

        while (salt.length() < length)
            salt.append(SALTCHARS.charAt((int) (new Random().nextFloat() * SALTCHARS.length())));
        return salt.toString();
    }

    protected static String getSaltNumber(int length) {
        final String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();

        while (salt.length() < length)
            salt.append(SALTCHARS.charAt((int) (new Random().nextFloat() * SALTCHARS.length())));
        return salt.toString();
    }
}
