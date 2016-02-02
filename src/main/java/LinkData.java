import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Bharat on 29/09/14.
 */
public class LinkData
{
    static int attempt = 0;
    public static String getLinkData(String urlString)
    {
        try
        {
            System.out.print("ATTEMPT:  " + attempt );
            attempt++;
            URL url = new URL(urlString);
            String userAgent = " ";
//            switch(attempt%5)
//            {
//                case 0: userAgent = "Opera/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14";
//                    break;
//                case 1: userAgent = "AmigaVoyager/3.2 (AmigaOS/MC680x0)";
//                    break;
//                case 2: userAgent = "HotJava/1.1.2 FCS";
//                    break;
//                case 3: userAgent = "Cyberdog/2.0 (Macintosh; PPC)";
//                    break;
//                case 4: userAgent ="";
//                    break;
//
//            }
            userAgent ="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.122 Safari/537.36";
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", userAgent);
//          conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.122 Safari/537.36");
            //conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1; Trident/5.0; InfoPath.2; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 2.0.50727) 3gpp-gba UNTRUSTED/1.0");

            if(conn.getResponseCode()!=200)
            {
                throw new Exception("Issues with getting the URL"   + conn.getResponseMessage());
            }

            BufferedReader buf = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String st;
            StringBuilder sb = new StringBuilder();

            while((st = buf.readLine())!=null)
            {
                sb.append(st).append('\n');
            }
            buf.close();

            return sb.toString();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return "";
    }
}
