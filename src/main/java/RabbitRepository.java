import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.json.JSONTokener;
//import org.json.XML;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.w3c.dom.NodeList;

import java.io.Console;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import javax.swing.text.Document;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;

import java.io.IOException;
import java.util.Observable;
import java.util.UUID;

/**
 * Created by Bharat on 25/09/14.
 */
public class RabbitRepository
{

    public static void main(String args[])
    {
        String urlString = "http://www.sportrabbit.com/sporting-goods-store-directory/sporting-goods-beginning-with/a";

        System.out.print("Name, ");
        System.out.print("Phone No., ");
        System.out.print("Website, ");
        System.out.print("Address, ");
        System.out.print("City, ");
        System.out.print("State, ");
        System.out.println("Zip Code");

        String data = getLinkData(urlString);

        LinkedList<MajorLink> majorLinks = getMajorLinks(data);

    }

    public static String getLinkData(String urlString)
    {
        try
        {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.122 Safari/537.36");

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

        }

        return "";
    }

    public static LinkedList<MajorLink> getMajorLinks(String data)
    {
        LinkedList<MajorLink> majorLinkList = new LinkedList<MajorLink>();
        int level = 1;

        int startIndex = 0;
        int endIndex = 0;
        int linkEndIndex = 0;
        int titleStartIndex = 0;
        int titleEndIndex = 0;

        String remaining = data;
        String temp;

        startIndex = remaining.indexOf("text-heading");

        endIndex = remaining.indexOf("</div>", startIndex);



        //System.out.println("Index: " + endIndex + " \n");

        try
        {
            temp = remaining.substring(startIndex, endIndex);
        }
        catch (Exception E)
        {
            temp = "Could not receive first substring containing major links";
        }



        remaining = temp;

        String link = "http://www.sportrabbit.com/sporting-goods-store-directory/sporting-goods-beginning-with/a";
        String title = "A";
//
//
//
        String linkPath = "http://www.sportrabbit.com";
        MajorLink mlink = new MajorLink();

 //       mlink = new MajorLink(link, title, level);
   //     majorLinkList.add(mlink);


        int i = 1;


        boolean completed = false;

        while(!completed)
        {
            i++;

            try {
                completed = false;

                startIndex = remaining.indexOf("\"/sporting-goods-store-directory/", titleEndIndex);
                linkEndIndex = remaining.indexOf("\">", startIndex) + 1;

                if (startIndex == -1) {
                    completed = true;
                    continue;
                }

                link = linkPath + remaining.substring(startIndex + 1, linkEndIndex - 1);//to remove quotation marks from link

                //System.out.println("Link: " + link);

                titleStartIndex = linkEndIndex + 1;
                titleEndIndex = titleStartIndex + 1; //title is just one letter/number
                title = remaining.substring(titleStartIndex, titleStartIndex + 1);

                //System.out.println(" Title: " + title);

                if (i > 20 )
                {
                    MajorLink mLink = new MajorLink(link, title, level);
                    majorLinkList.add(mLink);
                }
            }
            catch (StringIndexOutOfBoundsException e)
            {
                completed = true;
            }

        }



        return majorLinkList;

    }


    public static LinkedList<MajorLink> getLocatorLinks(String majorLink)
    {
//        System.out.println("Within Major Link" + majorLink);

        LinkedList<MajorLink> minorLinkList = new LinkedList<MajorLink>();
        int level = 2;

        int startIndex = 0;
        int endIndex = 0;
        int linkEndIndex = 0;
        int titleStartIndex = 0;
        int titleEndIndex = 0;

        String remaining = RabbitRepository.getLinkData(majorLink);

        String temp;

        startIndex = remaining.indexOf("padding:5px 0px 5px 0px");

        int tempIndex = remaining.lastIndexOf("padding:5px 0px 5px 0px");//there are 2 columns with same div style. need both
        endIndex = remaining.indexOf("</div>", tempIndex);


        try
        {
            temp = remaining.substring(startIndex, endIndex);
        }
        catch (Exception E)
        {
            temp = "Could not receive first substring containing major links";
        }

        remaining = temp;

        String linkPath = "http://www.sportrabbit.com";


        boolean completed = false;

        while(!completed)
        {
            try
            {
                completed = false;

                startIndex = remaining.indexOf("\"/sporting-goods-store-directory/", titleEndIndex);
                linkEndIndex = remaining.indexOf("\">", startIndex)+1;

                if(startIndex == -1)
                {
                    completed = true;
                    continue;
                }

                String link = linkPath + remaining.substring(startIndex + 1, linkEndIndex - 1);//to remove quotation marks from link

                //System.out.println("Link: " + link);

                titleStartIndex = linkEndIndex + 1;
                titleEndIndex = remaining.indexOf("</a", titleStartIndex);
                String title = remaining.substring(titleStartIndex, titleEndIndex);

                //System.out.println(" Title: " + title + "\n");

                MajorLink mLink = new MajorLink(link, title, level);
                minorLinkList.add(mLink);

            }
            catch (StringIndexOutOfBoundsException e)
            {
                completed = true;
            }
        }

        //System.out.println("Completed Running Through Link");

        return minorLinkList;
    }


    public static LinkedList<MajorLink> getStorePage(String locatorLink)
    {
//        System.out.println("Within Locator Link" + locatorLink);

        boolean completed = false;

        if(locatorLink.equals("http://www.sportrabbit.com/sporting-goods-store-directory/1-limit-bait-shop"))
        {
           completed = true ;
        }



        LinkedList<MajorLink> minorLinkList = new LinkedList<MajorLink>();
        int level = 3;

        int startIndex = 0;
        int endIndex = 0;
        int linkEndIndex = 0;
        int titleStartIndex = 0;
        int titleEndIndex = 0;

        String remaining = RabbitRepository.getLinkData(locatorLink);

//        if (locatorLink.equals("http://www.sportrabbit.com/sporting-goods-store-directory/zappas"))
//        {
//            System.out.println(" Got Link Data");
//        }

        String temp;

        startIndex = remaining.indexOf("padding:5px 0px 5px 0px");



        endIndex = remaining.indexOf("</div>", startIndex);

//        if (locatorLink.equals("http://www.sportrabbit.com/sporting-goods-store-directory/zappas"))
//        {
//            System.out.println(" Found Start " + startIndex + " And End: " + endIndex);
//        }


        try
        {
            temp = remaining.substring(startIndex, endIndex);
        }
        catch (Exception E)
        {
            temp = "Could not receive first substring containing major links";
            System.out.println("Problem In Finding Store Page");
        }

        remaining = temp;

        String linkPath = "http://www.sportrabbit.com";




        while(!completed)
        {
            try
            {
                completed = false;

                startIndex = remaining.indexOf("href", titleEndIndex);
                linkEndIndex = remaining.indexOf("\">", startIndex)+1;

//                if (locatorLink.equals("http://www.sportrabbit.com/sporting-goods-store-directory/zappas"))
//                {
//                    System.out.println(" Found LINK Start " + startIndex + " And End: " + linkEndIndex);
//                }

                if(startIndex == -1)
                {
                    completed = true;
                    continue;
                }

                String link = linkPath + remaining.substring(startIndex + 6, linkEndIndex - 1);//to remove quotation marks from link

                //System.out.println("Link: " + link);

                titleStartIndex = linkEndIndex + 1;
                titleEndIndex = remaining.indexOf("</a", titleStartIndex);
                String title = remaining.substring(titleStartIndex, titleEndIndex);

                //System.out.println(" Title: " + title + "\n");

//                if (locatorLink.equals("http://www.sportrabbit.com/sporting-goods-store-directory/zappas"))
//                {
//                    System.out.print("LINK : " + link);
//                    System.out.print("TITLE : " + title);
//                }

                MajorLink mLink = new MajorLink(link, title, level);
                minorLinkList.add(mLink);

            }
            catch (StringIndexOutOfBoundsException e)
            {
                e.printStackTrace();
                completed = true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("Error");
            }
        }

        //System.out.println("Completed Running Through Locator Link");

        return minorLinkList;
    }

    public static void getStoreInfo(String storeLink)
    {
//        System.out.println("Within Store Link" + storeLink);

        int level = 4;
        int startIndex = 0;
        int endIndex = 0;
        int fieldEnd = 0;

        String address = "";
        String phoneNumber = "";
        String name = "";
        String website = "";

        String remaining = RabbitRepository.getLinkData(storeLink);
        startIndex = remaining.indexOf("store-address");
        endIndex = remaining.indexOf("</div>", startIndex);



        int fieldStart = remaining.indexOf("<p>", startIndex);
        int fieldMid = 0;

        while(fieldStart<endIndex)
        {

            fieldMid = remaining.indexOf("</", fieldStart);
            address += remaining.substring(fieldStart + 3, fieldMid);//+3 to remove <p> from string
            fieldStart = remaining.indexOf("<p>", fieldMid);
            if (fieldStart<endIndex)
                address += ", ";
        }




        int webStart = remaining.indexOf("Company Website Click", fieldMid);
        if(webStart > 0 && webStart <= remaining.length())
        {
            fieldStart = remaining.indexOf("\">", webStart);
            fieldEnd = remaining.indexOf("<", fieldStart);
            website = remaining.substring(fieldStart+2, fieldEnd);

        }



        fieldStart = remaining.indexOf("phoneDisplay\" class");
        fieldMid = remaining.indexOf(">", fieldStart);
        fieldEnd = remaining.indexOf("</", fieldMid);

        phoneNumber = remaining.substring(fieldMid + 1, fieldEnd);

        fieldStart = remaining.indexOf("class=\"info\"");
        fieldMid = remaining.indexOf("<h1>", fieldStart);
        fieldEnd = remaining.indexOf("</h1", fieldMid);

//        try
//        {
            name = remaining.substring(fieldMid + 4, fieldEnd);
//        }
//        catch (StringIndexOutOfBoundsException e)
//        {
//
//        }

        try
        {
            RabbitStore store = new RabbitStore(name, phoneNumber, address, website);

            store.storeToString();
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }



        //System.out.println("Name : " + name + " ||Address : " + address + " ||Phone : " + phoneNumber);

    }

    public static void loadStore(RabbitStore store)
    {
//        MongoClient mongo;
//        DBCollection coll;
//        try
//        {
//            mongo = new MongoClient("localhost");
//            DB db = mongo.getDB("Stores");
//            coll = db.getCollection("rabbitCollection");
//            BasicDBObject obj = new BasicDBObject();
//            String name = store.getName();
//            String phone = store.getPhone();
//            String streetAddress = store.getStreetAddress();
//            String state = store.getState();
//            String city = store.getCity();
//            String zipCode = store.getZipCode();
//            String uniqueID = store.getUniqueID();
//            String website = store.getWebsite();
//
//
//            BasicDBObject dbObj = new BasicDBObject();
//            dbObj.put("_id", uniqueID);
//            dbObj.put("Phone", phone);
//            dbObj.put("Website", website);
//            dbObj.put("State", state);
//            dbObj.put("City", city);
//            dbObj.put("Address", streetAddress);
//            dbObj.put("ZipCode", zipCode);
//            coll.save(dbObj);
//            store.setCompleted(true);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            store.setCompleted(false);
//        }
//
//
    }


}
