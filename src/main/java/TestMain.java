import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.awt.datatransfer.Clipboard;
import java.util.LinkedList;
import java.util.Set;

public class TestMain
{

public static void main( String [] args ) throws Exception
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

    try
    {
        MongoClient mongo = new MongoClient("localhost");
        DB db = mongo.getDB("Stores");
        Set<String> collections = db.getCollectionNames();
        LinkedList<String> stateLinks = new LinkedList<String>();
        LinkedList<MajorLink> minorLinks;

        int i = 0, j=0;
        for (String collectionName : collections)
        {
//            if(i==0)
//            {
                if (!collectionName.equals("StateLinks") && !collectionName.equals("system.indexes") && !collectionName.equals("StoreList"))
                {
                    stateLinks.add(collectionName);
                    minorLinks = LiquorMain.getIncompleteLinks("Stores", collectionName, 5);
                    System.out.println("Found Incomplete Page Links: " + collectionName);
                    j=0;
                    for (MajorLink minorPage : minorLinks)
                    {
//                        if(j==0)
//                        {

                            try
                            {
                                String data = LiquorMain.getLinkSource(minorPage.getLink(), r);
                                System.out.println("Retrieved Source: " + minorPage.getLink());


                                boolean parsed = LiquorMain.parseLiquorPage(data, minorPage.getLink());
                                System.out.println("Parsed Page" + minorPage.getLink());

                                if(parsed)
                                {

                                    minorPage.setCompleted(true);
                                    DBCollection coll = db.getCollection(collectionName);
                                    BasicDBObject dbObj = new BasicDBObject();
                                    dbObj.put("_id", minorPage.getLink());
                                    dbObj.put("Title", minorPage.getTitle());
                                    dbObj.put("isCompleted", minorPage.isCompleted());
                                    coll.save(dbObj);
                                    System.out.println("Saved To Mongo" + minorPage.getLink());
                                }
                                else
                                {
                                    System.out.print("Unable To Parse : " + minorPage.getLink());
                                    System.exit(-1);
                                }
                            }
                            catch (Exception e)
                            {

                            }
//                        }
                        j++;

                    }
                i++;
                }

//            }




        }
    }
    catch(Exception e )
    {
        e.printStackTrace();
    }

//    System.out.println("Start Fetching");
//    try
//    {
//        viewSource(r);
//        Thread.sleep(3000);
//
//        copyToClipboard(r);
//        Thread.sleep(800);
//
//        String data = getStringFromClipBoard();
//
//        System.out.println( data );
//
//        closeCurrentTab(r);
//
//        Thread.sleep(1000);
//
//        openNewTab(r);
//
//        Thread.sleep(1000);
//
//        typeString("www.manta.com/mb_44_B6399_09/liquor_stores/district_of_columbia?pg=8", r);
//
//        Thread.sleep(2500);
//
//        r.keyPress(KeyEvent.VK_ENTER);
//
//    }
//    catch( Exception e )
//    {
//        e.printStackTrace();
//    }
//
//    System.out.println("Finished with program, sleeping for a bit");
//
//    Thread.sleep(15000);
}

private static void viewSource(Robot r) throws Exception
{
    Thread.sleep(3000);
    r.keyPress(KeyEvent.VK_TAB);
    r.keyRelease(KeyEvent.VK_TAB);

    Thread.sleep(10);

    r.keyPress(157);
    r.keyPress(KeyEvent.VK_ALT);
    r.keyPress(KeyEvent.VK_U );

    r.keyRelease(KeyEvent.VK_U );
    r.keyRelease(KeyEvent.VK_ALT);
    r.keyRelease(157);
}

private static void copyToClipboard(Robot r) throws Exception
{
    r.keyPress(157);
    r.keyPress(KeyEvent.VK_A);
    r.keyRelease(KeyEvent.VK_A);
    r.keyRelease(157);

    Thread.sleep(800);

    r.keyPress(157);
    r.keyPress(KeyEvent.VK_C);
    r.keyRelease(KeyEvent.VK_C);
    r.keyRelease(157);
}


private static String getStringFromClipBoard() throws Exception
{
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Clipboard clipboard = toolkit.getSystemClipboard();
    String data = (String) clipboard.getData(DataFlavor.stringFlavor);

    return data;
}

private static void closeCurrentTab(Robot r)
{
    r.keyPress(157);
    r.keyPress(KeyEvent.VK_W);
    r.keyRelease(KeyEvent.VK_W);
    r.keyRelease(157);
}

private static void openNewTab(Robot r)
{
    r.keyPress(157);
    r.keyPress(KeyEvent.VK_T);
    r.keyRelease(KeyEvent.VK_T);
    r.keyRelease(157);
}

private static void printCharacters( String str, Robot r ) throws Exception
{


    Class keyEventClass = KeyEvent.class;


    char[] charArray = str.toCharArray();

    for( char c : charArray )
    {

        String key = null;
        if( c == ' ' )
        {
            key = "VK_SPACE";
        }
        else
        {
            if( c == '-' )
            {
                key = "VK_MINUS";
            }
            else if( c == ':' )
            {

                r.keyPress(KeyEvent.VK_SHIFT);
                r.keyPress(KeyEvent.VK_SEMICOLON);
                r.keyRelease(KeyEvent.VK_SEMICOLON);
                r.keyRelease(KeyEvent.VK_SHIFT);
            }
            else
            {
                key = "VK_" + Character.toUpperCase(c) ;
            }
        }

        System.out.println("Key : " + key );
        Field field = keyEventClass.getField(key);
        int keyInt = field.getInt(null);
        r.keyPress(keyInt);
        r.keyRelease(keyInt);


    }

}

    public static void typeString(String str, Robot r)
    {
        int keyValue;
        for(char character : str.toCharArray())
        {
            switch (character)
            {
                case 'a':
                    r.keyPress(keyValue = KeyEvent.VK_A);
                    break;
                case 'b':
                    r.keyPress(keyValue = KeyEvent.VK_B);
                    break;
                case 'c':
                    r.keyPress(keyValue = KeyEvent.VK_C);
                    break;
                case 'd':
                    r.keyPress(keyValue = KeyEvent.VK_D);
                    break;
                case 'e':
                    r.keyPress(keyValue = KeyEvent.VK_E);
                    break;
                case 'f':
                    r.keyPress(keyValue = KeyEvent.VK_F);
                    break;
                case 'g':
                    r.keyPress(keyValue = KeyEvent.VK_G);
                    break;
                case 'h':
                    r.keyPress(keyValue = KeyEvent.VK_H);
                    break;
                case 'i':
                    r.keyPress(keyValue = KeyEvent.VK_I);
                    break;
                case 'j':
                    r.keyPress(keyValue = KeyEvent.VK_J);
                    break;
                case 'k':
                    r.keyPress(keyValue = KeyEvent.VK_K);
                    break;
                case 'l':
                    r.keyPress(keyValue = KeyEvent.VK_L);
                    break;
                case 'm':
                    r.keyPress(keyValue = KeyEvent.VK_M);
                    break;
                case 'n':
                    r.keyPress(keyValue = KeyEvent.VK_N);
                    break;
                case 'o':
                    r.keyPress(keyValue = KeyEvent.VK_O);
                    break;
                case 'p':
                    r.keyPress(keyValue = KeyEvent.VK_P);
                    break;
                case 'q':
                    r.keyPress(keyValue = KeyEvent.VK_Q);
                    break;
                case 'r':
                    r.keyPress(keyValue = KeyEvent.VK_R);
                    break;
                case 's':
                    r.keyPress(keyValue = KeyEvent.VK_S);
                    break;
                case 't':
                    r.keyPress(keyValue = KeyEvent.VK_T);
                    break;
                case 'u':
                    r.keyPress(keyValue = KeyEvent.VK_U);
                    break;
                case 'v':
                    r.keyPress(keyValue = KeyEvent.VK_V);
                    break;
                case 'w':
                    r.keyPress(keyValue = KeyEvent.VK_W);
                    break;
                case 'x':
                    r.keyPress(keyValue = KeyEvent.VK_X);
                    break;
                case 'y':
                    r.keyPress(keyValue = KeyEvent.VK_Y);
                    break;
                case 'z':
                    r.keyPress(keyValue = KeyEvent.VK_Z);
                    break;
                case 'A':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_A);
                    r.keyRelease(KeyEvent.VK_A);
                    break;
                case 'B':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_B);
                    r.keyRelease(KeyEvent.VK_B);
                    break;
                case 'C':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_C);
                    r.keyRelease(KeyEvent.VK_C);
                    break;
                case 'D':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_D);
                    r.keyRelease(KeyEvent.VK_D);
                    break;
                case 'E':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_E);
                    r.keyRelease(KeyEvent.VK_E);
                    break;
                case 'F':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_F);
                    r.keyRelease(KeyEvent.VK_F);
                    break;
                case 'G':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_G);
                    r.keyRelease(KeyEvent.VK_G);
                    break;
                case 'H':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_H);
                    r.keyRelease(KeyEvent.VK_H);
                    break;
                case 'I':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_I);
                    r.keyRelease(KeyEvent.VK_I);
                    break;
                case 'J':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_J);
                    r.keyRelease(KeyEvent.VK_J);
                    break;
                case 'K':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_K);
                    r.keyRelease(KeyEvent.VK_K);
                    break;
                case 'L':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_L);
                    r.keyRelease(KeyEvent.VK_L);
                    break;
                case 'M':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_M);
                    r.keyRelease(KeyEvent.VK_M);
                    break;
                case 'N':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_N);
                    r.keyRelease(KeyEvent.VK_N);
                    break;
                case 'O':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_O);
                    r.keyRelease(KeyEvent.VK_O);
                    break;
                case 'P':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_P);
                    r.keyRelease(KeyEvent.VK_P);
                    break;
                case 'Q':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_Q);
                    r.keyRelease(KeyEvent.VK_Q);
                    break;
                case 'R':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_R);
                    r.keyRelease(KeyEvent.VK_R);
                    break;
                case 'S':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_S);
                    r.keyRelease(KeyEvent.VK_S);
                    break;
                case 'T':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_T);
                    r.keyRelease(KeyEvent.VK_T);;
                    break;
                case 'U':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_U);
                    r.keyRelease(KeyEvent.VK_U);
                    break;
                case 'V':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_V);
                    r.keyRelease(KeyEvent.VK_V);
                    break;
                case 'W':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_W);
                    r.keyRelease(KeyEvent.VK_W);
                    break;
                case 'X':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_X);
                    r.keyRelease(KeyEvent.VK_X);
                    break;
                case 'Y':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_Y);
                    r.keyRelease(KeyEvent.VK_Y);
                    break;
                case 'Z':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_Z);
                    r.keyRelease(KeyEvent.VK_Z);
                    break;
                case '`':
                    r.keyPress(keyValue = KeyEvent.VK_BACK_QUOTE);
                    break;
                case '0':
                    r.keyPress(keyValue = KeyEvent.VK_0);
                    break;
                case '1':
                    r.keyPress(keyValue = KeyEvent.VK_1);
                    break;
                case '2':
                    r.keyPress(keyValue = KeyEvent.VK_2);
                    break;
                case '3':
                    r.keyPress(keyValue = KeyEvent.VK_3);
                    break;
                case '4':
                    r.keyPress(keyValue = KeyEvent.VK_4);
                    break;
                case '5':
                    r.keyPress(keyValue = KeyEvent.VK_5);
                    break;
                case '6':
                    r.keyPress(keyValue = KeyEvent.VK_6);
                    break;
                case '7':
                    r.keyPress(keyValue = KeyEvent.VK_7);
                    break;
                case '8':
                    r.keyPress(keyValue = KeyEvent.VK_8);
                    break;
                case '9':
                    r.keyPress (keyValue = KeyEvent.VK_9);
                    break;
                case '-':
                    r.keyPress(keyValue = KeyEvent.VK_MINUS);
                    break;
                case '=':
                    r.keyPress(keyValue = KeyEvent.VK_EQUALS);
                    break;
                case '~':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_BACK_QUOTE);
                    r.keyRelease(KeyEvent.VK_BACK_QUOTE);
                    break;
                case '!':
                    r.keyPress(keyValue = KeyEvent.VK_EXCLAMATION_MARK);
                    break;
                case '@':
                    r.keyPress(keyValue = KeyEvent.VK_AT);
                    break;
                case '#':
                    r.keyPress(keyValue = KeyEvent.VK_NUMBER_SIGN);
                    break;
                case '$':
                    r.keyPress(keyValue = KeyEvent.VK_DOLLAR);
                    break;
                case '%':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_5);
                    r.keyRelease(KeyEvent.VK_5);
                    break;
                case '^':
                    r.keyPress(keyValue = KeyEvent.VK_CIRCUMFLEX);
                    break;
                case '&':
                    r.keyPress(keyValue = KeyEvent.VK_AMPERSAND);
                    break;
                case '*':
                    r.keyPress(keyValue = KeyEvent.VK_ASTERISK);
                    break;
                case '(':
                    r.keyPress(keyValue = KeyEvent.VK_LEFT_PARENTHESIS);
                    break;
                case ')':
                    r.keyPress(keyValue = KeyEvent.VK_RIGHT_PARENTHESIS);
                    break;
                case '_':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_MINUS);
                    r.keyRelease(KeyEvent.VK_MINUS);
                    break;
                case '+':
                    r.keyPress(keyValue = KeyEvent.VK_PLUS);
                    break;
                case '\t':
                    r.keyPress(keyValue = KeyEvent.VK_TAB);
                    break;
                case '\n':
                    r.keyPress(keyValue = KeyEvent.VK_ENTER);
                    break;
                case '[':
                    r.keyPress(keyValue = KeyEvent.VK_OPEN_BRACKET);
                    break;
                case ']':
                    r.keyPress(keyValue = KeyEvent.VK_CLOSE_BRACKET);
                    break;
                case '\\':
                    r.keyPress (keyValue = KeyEvent.VK_BACK_SLASH);
                    break;
                case '{':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_OPEN_BRACKET);
                    r.keyRelease(KeyEvent.VK_OPEN_BRACKET);
                    break;
                case '}':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_CLOSE_BRACKET);
                    r.keyRelease(KeyEvent.VK_CLOSE_BRACKET);
                    break;
                case '|':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_BACK_SLASH);
                    r.keyRelease(KeyEvent.VK_BACK_SLASH);
                    break;
                case ';':
                    r.keyPress(keyValue = KeyEvent.VK_SEMICOLON);
                    break;
                case ':':
                    r.keyPress(keyValue = KeyEvent.VK_COLON);
                    break;
                case '\'':
                    r.keyPress(keyValue = KeyEvent.VK_QUOTE);
                    break;
                case '"':
                    r.keyPress(keyValue = KeyEvent.VK_QUOTEDBL);
                    break;
                case ',':
                    r.keyPress(keyValue = KeyEvent.VK_COMMA);
                    break;
                case '<':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_COMMA);
                    r.keyRelease(KeyEvent.VK_COMMA);
                    break;
                case '.':
                    r.keyPress (keyValue = KeyEvent.VK_PERIOD);
                    break;
                case '>':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_PERIOD);
                    r.keyRelease(KeyEvent.VK_PERIOD);
                    break;
                case '/':
                    r.keyPress(keyValue = KeyEvent.VK_SLASH);
                    break;
                case '?':
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_SLASH);
                    r.keyRelease(KeyEvent.VK_SLASH);
                    break;
                case ' ':
                    r.keyPress(keyValue = KeyEvent.VK_SPACE);
                    break;
                default:
                    throw new IllegalArgumentException("Cannot type character " + character);
            }

            r.keyRelease(keyValue);
        }
    }



}
