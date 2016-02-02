import java.io.PrintWriter;

/**
 * Created by Bharat on 28/09/14.
 */
public class RabbitStore
{
    String name;
    String phone;
    String streetAddress;
    String state;
    String city;
    String zipCode;
    String uniqueID;
    String website;
    boolean isCompleted;

    public RabbitStore(String name, String phone, String fullAddress, String website)
    {
        this.setName(name);
        this.setPhone(phone);
        this.setAddressParts(fullAddress);
        this.setUniqueID(fullAddress);
        this.setWebsite(website);

        RabbitRepository.loadStore(this);
    }

    public boolean isCompleted()
    {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted)
    {
        this.isCompleted = isCompleted;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {

        if(website.equals(""))
        {
            this.website = "Unknown";

        }
        else
        {
            this.website = website.replaceAll(",", "");
        }
    }

    public String getUniqueID()
    {
        return uniqueID;
    }

    public void setUniqueID(String fullAddress)
    {
        this.uniqueID = fullAddress.replaceAll(",", "").replaceAll(" ", "");
    }

    public void setAddressParts(String fullAdress)
    {
//        int firstPart = fullAdress.indexOf(",");
//        String streetAddress = fullAdress.substring(0, firstPart);
//
//        int secondPart = fullAdress.indexOf(",", firstPart+1);
//        String city = fullAdress.substring(firstPart+1, secondPart);
//
//        int thirdPart = fullAdress.lastIndexOf(",", secondPart+1);
//        String state = fullAdress.substring(secondPart+1, thirdPart);
//
//        String zipCode = fullAdress.substring(thirdPart+1);

        int indexInt = fullAdress.lastIndexOf(",");
        String zipCode = fullAdress.substring(indexInt+1);
        zipCode = zipCode.replaceAll(",", "");

        int nextIndex = fullAdress.lastIndexOf(",", indexInt-1);
        String state = fullAdress.substring(nextIndex+1, indexInt);
        state = state.replaceAll(",", "");

        String city = "not set";
        try
        {

            indexInt = fullAdress.lastIndexOf(",", nextIndex - 1);
            city = fullAdress.substring(indexInt + 1, nextIndex);
            city = city.replaceAll(",", "");
        }
        catch(Exception e)
        {
             city = "error";
            indexInt = nextIndex;
        }
        //nextIndex = fullAdress.lastIndexOf(",", indexInt - 1);
        String streetAddress = fullAdress.substring(0, indexInt);
        streetAddress = streetAddress.replaceAll(",","");

        this.setStreetAddress(streetAddress);
        this.setCity(city);
        this.setState(state);
        this.setZipCode(zipCode);
    }

    public String getName()
    {
        return name;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setName(String name)
    {
        name = name.replaceAll(",", ";");
        int replaceIndex = name.indexOf("&amp;");
        if (replaceIndex>=0 && replaceIndex<=name.length())
        {
            String newName = name.replace("&amp;", "&");
            this.name = newName;
        }
        else
        {
            this.name = name;
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void storeToString()
    {

          System.out.print(this.getName() + ", ");
          System.out.print(this.getPhone() + ", ");
          System.out.print(this.getWebsite() + ", ");
          System.out.print(this.getStreetAddress() + ", ");
          System.out.print(this.getCity() + ", ");
          System.out.print(this.getState() + ", ");
          System.out.println(this.getZipCode());

    }




}
