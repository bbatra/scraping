/**
 * Created by Bharat on 29/09/14.
 */

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.LinkedList;

import com.mongodb.*;

import java.util.NoSuchElementException;
import java.util.UUID;

public class LiquorRepository
{
    public static void main(String args[]) throws Exception {

        Robot r = new Robot();
        Process process = Runtime.getRuntime().exec("nohup sh /Users/Bharat/callChrome.sh");
        System.out.println("Loading First Page");

        try {
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Start Fetching");
        try
        {
            MongoClient mongo = new MongoClient("localhost");
            DB db = mongo.getDB("Stores");
            String bigStates[] = {"california", "florida", "texas", "new_york"};
            int i = 0;
            for(String currentState: bigStates)
            {
                LinkedList<MajorLink> minorLinks = LiquorMain.getIncompleteLinks("Stores", currentState, 4);
                System.out.println("Found Incomplete Page Links: " + currentState);
                int j=0;
                for (MajorLink cityLevel : minorLinks)
                {

                   LinkedList<MajorLink> pageLevel = LiquorMain.getIncompleteLinks("Stores", cityLevel.getTitle(), 5);
                    for(MajorLink minorPage : pageLevel)
                    {

//                        if(j==0)
//                        {

                        try {
                            String data = LiquorMain.getLinkSource(minorPage.getLink(), r);
                            System.out.println("Retrieved Source: " + minorPage.getLink());


                            boolean parsed = LiquorMain.parseLiquorPage(data, minorPage.getLink());
                            System.out.println("Parsed Page" + minorPage.getLink());

                            if (parsed)
                            {

                                minorPage.setCompleted(true);
                                DBCollection coll = db.getCollection(cityLevel.getTitle());
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
//
// }}
                        j++;
                    }

                }
                i++;
            }


//                for (MajorLink city : remainingCities)
//                {
//                    String title = city.getTitle();
//                    boolean isComplete = city.isCompleted();
//                    String link = city.getLink();
//
//                    System.out.println("Link:" + link);
//                    String data = LiquorMain.getLinkSource(link, r);
//                    if (data != "") {
//                        LinkedList<MajorLink> minorLinks = getPageLinks(data, title);
//                        try {
//                            if (minorLinks.getFirst().getTitle() != "Unknown") {
//                                boolean mongoEntered = true;
//                                city.setListOfMajorLink(minorLinks);
//                                for (MajorLink minor : minorLinks) {
//                                    mongoEntered = LiquorMain.addLinkToDB(city.getTitle(), minor);
//                                }
//
//                                if (mongoEntered) {
//                                    city.setCompleted(mongoEntered);
//                                    MongoClient mongo = new MongoClient("localhost");
//                                    DB db = mongo.getDB("Stores");
//                                    DBCollection coll = db.getCollection("StateLinks");
//                                    BasicDBObject dbObj = new BasicDBObject();
//                                    dbObj.put("_id", city.getLink());
//                                    dbObj.put("Title", city.getTitle());
//                                    dbObj.put("isCompleted", city.isCompleted());
//                                    coll.save(dbObj);
//                                }
//                            } else {
//                                city.setCompleted(false);
//                                System.out.print("Links Not Added For State:" + city.getTitle());
//                            }
//                        } catch (NoSuchElementException e) {
//                            city.setCompleted(false);
//                            System.out.print("Links Not Added For State:" + city.getTitle());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        System.out.println("Error Getting The Page Source : " + title + " : " + link);
//                    }
//                }
//            }
        }
        catch(Exception e)
        {
           e.printStackTrace();
        }

    }


    public static LinkedList<MajorLink> getPageLinks(String data, String state)
    {
        LinkedList<MajorLink> pageLinks = new LinkedList<MajorLink>();

        int level = 5;

        //String data = LinkData.getLinkData(link);

        try
        {
            int endIndex = data.indexOf("&rsaquo;");
//            System.out.println(endIndex);

            if(endIndex!=-1)
            {
                int startIndex = data.indexOf("href", endIndex);
//                System.out.println(startIndex);

                int linkEndIndex = data.indexOf("rel", startIndex);
//                System.out.println(linkEndIndex);
                String relativeLink = data.substring(startIndex + 6, linkEndIndex - 2);
                String path = "www.manta.com";

                int pgIndex = relativeLink.indexOf("pg");
                int pages = Integer.parseInt(relativeLink.substring(pgIndex + 3));

                String pageLink = path + relativeLink.substring(0, pgIndex + 3);

                int i = 1; //page Counter

                while (i <= pages)
                {
                    String fullPageLink = pageLink + Integer.toString(i);
                    MajorLink pLink = new MajorLink(fullPageLink, state, level);
                    pageLinks.add(pLink);
                    i++;
                }

//                System.out.println("LINK: " + path + relativeLink);

            }

        }
        catch (StringIndexOutOfBoundsException E)
        {
            System.out.println(state + "Links Failed");
            E.printStackTrace();

        }
        catch(Exception E)
        {

        }
        return  pageLinks;
    }

    public static LinkedList<MajorLink> getIncompleteLinks(String dbName, String collection, int level)
    {
        LinkedList<MajorLink> remainingStates = new LinkedList<MajorLink>();

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
                    System.out.println("TITLE : " + title + "   LINK: " + link);
                    MajorLink state = new MajorLink(link, title, level);
                    remainingStates.add(state);
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

        return remainingStates;
    }

//    public static LinkedList<MajorLink> getStoreLinks (String data)
//    {
//        LinkedList<MajorLink> storeList = new LinkedList<MajorLink>();
//        int level = 5;
//
//        int startIndex = data.indexOf("All Company Listings");
//        int endIndex = data.indexOf("Browse Subcategories");
//        int nextIndex;
//        int linkStartIndex;
//        int linkEndIndex;
//
//
//        String path = "http://www.manta.com";
//
//        if(startIndex >= 0 && startIndex <= data.length())
//        {
//            nextIndex = data.indexOf("itemprop=\"logo\"", startIndex);
//        }
//        else
//        {
//            nextIndex = data.indexOf("itemprop=\"logo\"");
//        }
//
//        linkStartIndex = data.indexOf("href", nextIndex) + 6;
//        linkEndIndex = data.indexOf("class=\"pull-left\"");
//
//        while(linkStartIndex <= endIndex && linkStartIndex >= 0)
//        {
//
//            String link = data.substring(linkStartIndex, linkEndIndex);
//
//            int titleStartIndex = data.indexOf("<strong", linkStartIndex);
//            int titleEndIndex = data.indexOf("</" , titleStartIndex);
//
//            String title = data.substring(titleStartIndex, titleEndIndex);
//
//            MajorLink mLink = new MajorLink(link, title, level);
//            storeList.add(mLink);
//
//            nextIndex = data.indexOf("itemprop=\"logo\"");
//            linkStartIndex = data.indexOf("href", nextIndex) + 6;
//            linkEndIndex = data.indexOf("class=\"pull-left\"");
//
//        }
//
//
//        return storeList;
//    }

    public static LinkedList<MajorLink> getStateLinks(String data)
    {
        int level = 4;//4 is first level for now because 1-3 in MajorLink are for RabbitRepository
        LinkedList<MajorLink> stateList = new LinkedList<MajorLink>();

        int startIndex = data.indexOf("list-group list-unstyled geo-list-left");
        int nextIndex = data.indexOf("list-group list-unstyled geo-list-right");
        int endIndex = data.indexOf("</div>", nextIndex);
        nextIndex = data.indexOf("href", startIndex);
        int linkEndIndex = data.indexOf("itemprop", nextIndex);


        String linkDirectory = "http://www.manta.com/";


        while(nextIndex<endIndex)
        {

            String link = linkDirectory + data.substring(nextIndex + 6, linkEndIndex - 2);
            System.out.println("LINK: " + link);

            int titleStart = link.lastIndexOf("/");
            String title = link.substring(titleStart+1);

            System.out.println("Title: " + title);

            MajorLink stateLink = new MajorLink(link, title, level);
            stateList.add(stateLink);

            startIndex = linkEndIndex;
            nextIndex = data.indexOf("href", startIndex);
            linkEndIndex = data.indexOf("itemprop", nextIndex);

        }

        return stateList;
    }

}
