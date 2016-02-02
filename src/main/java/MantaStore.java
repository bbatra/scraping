/**
 * Created by Bharat on 24/02/15.
 */
public class MantaStore
{
    private String name;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String phone;
    private String linkFoundOn;


    public MantaStore(String name, String streetAddress, String phone)
    {
        this.name = name;
        this.streetAddress = streetAddress;
        this.phone = phone;
    }


    public MantaStore(String name, String streetAddress, String city, String state, String zipCode, String phone, String linkFoundOn) {
        this.setName(name);
        this.setStreetAddress(streetAddress);
        this.setCity(city);
        this.setState(state);
        this.setZipCode(zipCode);
        this.setPhone(phone);
        this.setLinkFoundOn(linkFoundOn);
//            LiquorMain.addStoreToDB(this);
    }

    public String getLinkFoundOn() {
        return linkFoundOn;
    }

    public void setLinkFoundOn(String linkFoundOn) {
        this.linkFoundOn = linkFoundOn;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = parseString(name);
    }

    public String getStreetAddress()
    {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress)
    {
        this.streetAddress = parseString(streetAddress);
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = parseString(phone);
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = parseString(city);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = parseString(state);
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = parseString(zipCode);
    }

    private String parseString(String field)
    {
        if (field.contains("&#39;"))
        {
            field = field.replace("&#39;", "'");
        }
        if (field.contains("&amp;"))
        {
            field = field.replace("&amp;", "&");
        }
        if(field.contains("\n"))
        {
            field = field.replace("\n","");
        }
        if(field.contains("\t"))
        {
            field = field.replace("\t","");
        }
        if(field.contains("\r"))
        {
            field = field.replace("\r","");
        }
        field = field.trim();
        return field;
    }

}
