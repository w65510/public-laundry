package App;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Extensions
{
    public static boolean isInFuture(Timestamp timestamp) {
        return !isInPast(timestamp);
    }

    public static boolean isInPast(Timestamp timestamp) {
        long currentTime = System.currentTimeMillis();

        return timestamp.getTime() < currentTime;
    }

    public static String DurationToString(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        long seconds = duration.minusHours(hours).minusMinutes(minutes).get(ChronoUnit.SECONDS);

        return String.format("%02d", hours) + ":" +
                String.format("%02d", minutes) + ":" +
                String.format("%02d", seconds);
    }

    public static void openUrlInBrowser(String url) {
        String os = System.getProperty("os.name").toLowerCase();

        try
        {
            if (os.indexOf("win") >= 0)
            {
                Runtime rt = Runtime.getRuntime();
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            }
            else if (os.indexOf("mac") >= 0)
            {
                Runtime rt = Runtime.getRuntime();
                rt.exec("open " + url);
            }
            else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0)
            {
                Runtime rt = Runtime.getRuntime();
                String[] browsers = { "google-chrome", "firefox", "mozilla", "epiphany", "konqueror",
                        "netscape", "opera", "links", "lynx" };

                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < browsers.length; i++)
                    if(i == 0)
                        cmd.append(String.format(    "%s \"%s\"", browsers[i], url));
                    else
                        cmd.append(String.format(" || %s \"%s\"", browsers[i], url));

                rt.exec(new String[] { "sh", "-c", cmd.toString() });
            }
        }
        catch (Exception e) {}
    }

    public static String toIsoDateTime(LocalDateTime dt) {
        return dt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static String generatePickupCode() {
        String UPPER_CASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String NUMERIC_CHARS = "0123456789";
        int LENGTH = 6;

        StringBuilder builder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < LENGTH; i++) {
            if (i % 2 == 0) {
                builder.append(UPPER_CASE_CHARS.charAt(random.nextInt(UPPER_CASE_CHARS.length())));
            } else {
                builder.append(NUMERIC_CHARS.charAt(random.nextInt(NUMERIC_CHARS.length())));
            }
        }
        return builder.toString();
    }
}
