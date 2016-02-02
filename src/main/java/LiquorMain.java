import com.mongodb.*;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.awt.datatransfer.Clipboard;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;


public class LiquorMain
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

        System.out.println("Start Fetching");
        try
        {
            LinkedList<MajorLink> remainingStates = getIncompleteLinks("Stores", "StateLinks", 4);
            for(MajorLink state: remainingStates)
            {
                String title = state.getTitle();
                boolean isComplete = state.isCompleted();
                String link = state.getLink();

//                System.out.println("Title:" + title);
//                System.out.println("isCompleted:" + isComplete);
                System.out.println("Link:" + link);
                String data = getLinkSource(link, r);
                if(data!="")
                {
                    LinkedList<MajorLink> minorLinks = LiquorRepository.getPageLinks(data, title);
                    try
                    {
                        if (minorLinks.getFirst().getTitle() != "Unknown")
                        {
                            boolean mongoEntered = true;
                            state.setListOfMajorLink(minorLinks);
                            for (MajorLink minor : minorLinks)
                            {
                                mongoEntered = addLinkToDB(state.getTitle(), minor);
                            }

                            if (mongoEntered)
                            {
                                state.setCompleted(mongoEntered);
                                MongoClient mongo = new MongoClient("localhost");
                                DB db = mongo.getDB("Stores");
                                DBCollection coll = db.getCollection("StateLinks");
                                BasicDBObject dbObj = new BasicDBObject();
                                dbObj.put("_id", state.getLink());
                                dbObj.put("Title", state.getTitle());
                                dbObj.put("isCompleted", state.isCompleted());
                                coll.save(dbObj);
                            }
                        }
                        else {
                            state.setCompleted(false);
                            System.out.print("Links Not Added For State:" + state.getTitle());
                        }
                    }catch (NoSuchElementException e)
                    {
                        state.setCompleted(false);
                        System.out.print("Links Not Added For State:" + state.getTitle());
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    System.out.println("Error Getting The Page Source : " + title + " : " + link);
                }


            }

        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        System.out.println("Finished with program, sleeping for a bit");

        Thread.sleep(15000);
    }

    public static String getLinkSource(String link, Robot r)
    {
        String data = "";
        openNewTab(r);

        try
        {
        Thread.sleep(200);

        typeString(link, r);
        Thread.sleep(1000);

        r.keyPress(KeyEvent.VK_ENTER);
        Thread.sleep(18000);

        viewSource(r);
        Thread.sleep(1500);

        copyToClipBoard(r);
        Thread.sleep(500);

        data = getStringFromClipBoard();

        closeCurrentTab(r);
        Thread.sleep(500);

        closeCurrentTab(r);

        return data;

        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return data;

    }

    public static void viewSource(Robot r) throws Exception
    {
        System.out.print("Attempting to view source");
        r.keyPress(KeyEvent.VK_TAB);
        r.keyRelease(KeyEvent.VK_TAB);

        Thread.sleep(100);

        r.keyPress(157);
        r.keyPress(KeyEvent.VK_ALT);
        r.keyPress(KeyEvent.VK_U );

        r.keyRelease(KeyEvent.VK_U );
        r.keyRelease(KeyEvent.VK_ALT);
        r.keyRelease(157);
    }

    private static void copyToClipBoard(Robot r) throws Exception
    {
        System.out.println("Copying Source Text");
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
                    r.keyPress(keyValue = KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_SEMICOLON);
                    r.keyRelease(KeyEvent.VK_SEMICOLON);

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

    public static LinkedList<MajorLink> getIncompleteLinks(String dbName, String collection, int level)
    {
        LinkedList<MajorLink> remainingLinks = new LinkedList<MajorLink>();

        try
        {
            MongoClient mongo = new MongoClient("localhost");
            DB db = mongo.getDB(dbName);
            DBCollection coll = db.getCollection(collection);
            BasicDBObject ref = new BasicDBObject();
            ref.put("isCompleted", false);
            DBCursor dbCursor =  coll.find(ref);
            System.out.println(dbCursor.toString());

            if(level == 4)
            {

                Iterator<DBObject> it = dbCursor.iterator();
                while(it.hasNext())
                {
                    BasicDBObject curr = (BasicDBObject) it.next();
                    String link = curr.get("_id").toString();
                    String title = curr.get("Title").toString();
                    MajorLink state = new MajorLink(link, title, level);
                    remainingLinks.add(state);
                }

            }

            if(level == 5)
            {

                Iterator<DBObject> it = dbCursor.iterator();
                while(it.hasNext())
                {
                    BasicDBObject curr = (BasicDBObject) it.next();
                    String link = curr.get("_id").toString();
                    String title = curr.get("_id").toString();
                    MajorLink state = new MajorLink(link, title, level);
                    remainingLinks.add(state);
                }

            }

        }
        catch(StringIndexOutOfBoundsException E)
        {
            System.out.println("Error parsing strings");
            E.printStackTrace();


        }
        catch (Exception E)
        {
            System.out.println("Error finding previous links") ;
            E.printStackTrace();
        }

        return remainingLinks;
    }

    public static boolean addLinkToDB(String collection, MajorLink link)
    {
        String dbName = "Stores";

        try
        {
            MongoClient mongo = new MongoClient("localhost");
            DB db = mongo.getDB(dbName);
            DBCollection coll = db.getCollection(collection);
            BasicDBObject dbObj = new BasicDBObject();

            dbObj.put("_id", link.getLink());
            dbObj.put("Title", link.getTitle()+" : " + link.getLink());
            dbObj.put("isCompleted", link.isCompleted());
            coll.save(dbObj);

        }
        catch(Exception e)
        {
           e.printStackTrace();
           return false;
        }
        return true;


    }

    public static boolean addStoreToDB(LiquorStore store)
    {
        String dbName = "Stores";

        try
        {
            MongoClient mongo = new MongoClient("localhost");
            DB db = mongo.getDB(dbName);
            DBCollection coll = db.getCollection("StoreList");
            BasicDBObject dbObj = new BasicDBObject();

            dbObj.put("_id", store.getLinkFoundOn() + " : " + store.getStreetAddress());
            dbObj.put("Title", store.getName());
            dbObj.put("StreetAddress", store.getStreetAddress());
            dbObj.put("City", store.getCity());
            dbObj.put("State", store.getState());
            dbObj.put("ZipCode", store.getZipCode());
            dbObj.put("Phone", store.getPhone());
            coll.save(dbObj);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;


    }

    public static boolean parseLiquorPage(String data, String link)
    {
        boolean parsed = false;
        try
        {
            int startIndex = 0;

            try
            {
                startIndex = data.indexOf("All Company Listings");
                if(startIndex<0)
                {
                    startIndex = 0;
                }
                startIndex = data.indexOf("media-body", startIndex+1);
            }
            catch (StringIndexOutOfBoundsException e)
            {
                startIndex = 0;
                startIndex = data.indexOf("media-body", startIndex+1);
            }

            System.out.println("Begin Parsing: " + link);
            while(startIndex>=0)
            {

                int nextIndex = data.indexOf("<strong>", startIndex);
                int endIndex = data.indexOf("</strong>", nextIndex);
                String name = data.substring(nextIndex+8
                        , endIndex);

                String streetAddress;

                nextIndex = data.indexOf("streetAddress", endIndex);
                if(nextIndex<0)
                {
                    streetAddress = "";
                }
                else
                {
                    endIndex = data.indexOf("</", nextIndex);
                    streetAddress = data.substring(nextIndex + 15, endIndex);
                }


                nextIndex = data.indexOf("addressLocality", endIndex);
                String city;
                if(nextIndex<0)
                {
                    city = "";
                }
                else
                {

                    endIndex = data.indexOf("</", nextIndex);
                    city = data.substring(nextIndex + 17, endIndex);
                }


                nextIndex = data.indexOf("addressRegion", endIndex);
                String state;

                if(nextIndex<0)
                {
                    state="";
                }
                else
                {
                    endIndex = data.indexOf("</", nextIndex);
                    state = data.substring(nextIndex + 15, endIndex);
                }

                nextIndex = data.indexOf("postalCode", endIndex);
                String zipCode;
                if(nextIndex<0)
                {
                    zipCode="";
                }
                else
                {
                    endIndex = data.indexOf("</", nextIndex);
                    zipCode = data.substring(nextIndex+12, endIndex);
                }



                String phoneSearchString = "<div rel=\"mainPhone\" itemprop=\"telephone\" class=\"hidden-device-xs\">";
                String phone;

                nextIndex = data.indexOf(phoneSearchString, endIndex);
                if(nextIndex<0)
                {
                    phone = "";
                }
                else
                {

                    endIndex = data.indexOf("</", nextIndex);
                    phone = data.substring(nextIndex + phoneSearchString.length() + 1, endIndex);
                }

                LiquorStore current = new LiquorStore(name, streetAddress, city, state, zipCode, phone, link);

                startIndex = data.indexOf("media-body", endIndex);
                System.out.println("Parsed one store" + link);
                parsed = true;
            }

        }
        catch (StringIndexOutOfBoundsException E)
        {
//            System.out.println("Links Failed :(");
//            E.printStackTrace();
            System.out.println("Got A StringOoB Exception");
            return parsed;

        }
        catch(Exception E)
        {
            E.printStackTrace();
        }

        return parsed;
    }
}

