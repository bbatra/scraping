import java.util.Iterator;
import java.util.LinkedList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.*;
import java.util.UUID;

/**
 * Created by Bharat on 25/09/14.
 */
public class MajorLink
{
    public String link;
    public String title;
    public boolean completed;
    public LinkedList<MajorLink> listOfMajorLink;
    public int level; // level in links hierarchy; major links start at level 1 then as we move down level increases



    public MajorLink()
    {
        this.setLink("Unknown");
        this.setTitle("Unknown");
        this.setCompleted(false);
    }

    public MajorLink(String link, String title, int level)
    {
        this.setLink(link);
        this.setTitle(title);
        this.setCompleted(false);
        this.setLevel(level);
//        this.setListOfMajorLink(this.getLink());
    }



//    private void addLinkToDB(String dbName, String collection, int level )
//    {
//
//        try
//        {
//            MongoClient mongo = new MongoClient("localhost");
//            DB db = mongo.getDB(dbName);
//            DBCollection coll = db.getCollection(collection);
//            BasicDBObject dbObj = new BasicDBObject();
//
//            if(level == 4)
//            {
//                dbObj.put("_id", this.getLink());
//                dbObj.put("Title", this.getTitle());
//                dbObj.put("isCompleted", this.isCompleted());
//                coll.save(dbObj);
//            }
//            if(level == 5)
//            {
//                dbObj.put("_id", this.getLink());
//                dbObj.put("Title", this.getTitle()+" : " + this.getLink());
//                dbObj.put("isCompleted", this.isCompleted());
//                coll.save(dbObj);
//            }
//        }
//        catch(Exception e)
//        {
//            this.setCompleted(false);
//        }
//
//
//    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
    }

    public LinkedList<MajorLink> getListOfMajorLink()
    {
        return listOfMajorLink;
    }

    public void setListOfMajorLink(LinkedList<MajorLink> minorLinkList)
    {


//        if (this.getLevel() == 1)
//        {
//            minorLinkList = RabbitRepository.getLocatorLinks(majorLink);
//        }
//
//        if(this.getLevel() == 2)
//        {
//            minorLinkList = RabbitRepository.getStorePage(majorLink);
//        }
//
//        if(this.getLevel() == 3)
//        {
//            RabbitRepository.getStoreInfo(majorLink);
//        }

//        if(this.getLevel() == 4)//needs edits
//        {
//           try
//           {
//               minorLinkList = LiquorRepository.getPageLinks(this.getLink(), this.getTitle());
//           }
//           catch(Exception E)
//           {
//
//           }
//        }
//
//            if(this.getLevel() == 5)
//        {
//            this.addLinkToDB("Stores", this.getTitle(), this.getLevel());
//        }


        this.listOfMajorLink = minorLinkList;


    }
}
