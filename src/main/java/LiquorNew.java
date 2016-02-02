/**
 * Created by Bharat on 05/11/14.
 */


import com.mongodb.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.io.FileWriter;
import java.io.IOException;

public class LiquorNew
{
    public static void main(String args[])
    {
        String dbName = "Stores";
        String collection = "StoreList";
        LinkedList<LiquorStore> listOfStores = new LinkedList<LiquorStore>();
        try
        {
            MongoClient mongo = new MongoClient("localhost");
            DB db = mongo.getDB(dbName);
            DBCollection coll = db.getCollection(collection);
            BasicDBObject ref = new BasicDBObject();
            DBCursor dbCursor =  coll.find(ref);
            System.out.println(dbCursor.toString());
            FileWriter writer = new FileWriter("/Users/Bharat/Desktop/LiquorList.csv");
            writer.append("Name");
            writer.append(',');
            writer.append("Street Address");
            writer.append(',');
            writer.append("City");
            writer.append(',');
            writer.append("State");
            writer.append(',');
            writer.append("ZipCode");
            writer.append(',');
            writer.append("Phone");
            writer.append('\n');




            Iterator<DBObject> it = dbCursor.iterator();
            while(it.hasNext())
            {
                BasicDBObject curr = (BasicDBObject) it.next();
//                String link =  "\""  + curr.get("_id").toString() + "\"" ;
                String title = "\"" + curr.get("Title").toString() +  "\"" ;
                String streetAddress = "\"" + curr.get("StreetAddress").toString() + "\"" ;
                String city = "\"" + curr.get("City").toString()+ "\"" ;
                String state = "\"" + curr.get("State").toString() +  "\"" ;
                String zipCode = "\"" + curr.get("ZipCode").toString() + "\"" ;
                String phone = "\"" + curr.get("Phone").toString() + "\"" ;

                writer.append(title);
                writer.append(',');
                writer.append(streetAddress);
                writer.append(',');
                writer.append(city);
                writer.append(',');
                writer.append(state);
                writer.append(',');
                writer.append(zipCode);
                writer.append(',');
                writer.append(phone);
                writer.append('\n');


            }

            writer.flush();
            writer.close();



        }
        catch(StringIndexOutOfBoundsException E)
        {
            System.out.println("Error parsing strings");
            E.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception E)
        {
            System.out.println("Error finding previous links") ;
            E.printStackTrace();
        }

    }

    public static void writeExcel()
    {

    }
//
//    public static LiquorStore getFromMongo()
//    {
//        LiquorStore liquor;
//
//        return liquor;
//    }


}
