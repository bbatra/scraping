import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Bharat on 04/03/15.
 */
public class ParseRunner
{
    public static void main(String args[]) throws Exception
    {
        Robot r = new Robot();
        Process process = Runtime.getRuntime().exec("nohup sh /Users/Bharat/callChrome.sh");
        System.out.println("Loading First Page");
        try
        {
            Thread.sleep(5000);
            r.keyPress(157);
            r.keyPress(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
            r.keyPress(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
            r.keyRelease(157);
            Thread.sleep(500);
            r.keyPress(KeyEvent.VK_ESCAPE);
            r.keyRelease(KeyEvent.VK_ESCAPE);
            Thread.sleep(500);
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }

        System.out.println("Start Fetching");
    }

}
