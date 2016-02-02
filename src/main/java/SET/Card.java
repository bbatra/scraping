package SET;

/**
 * Created by Bharat on 12/02/15.
 */
public class Card
{
    static final String SPACE_CHAR = " ";
    private int color;//blue = 0, green = 1, yellow = 2
    private int symbol;//a = 0, h = 1, s = 2
    private int shading;//lowercase = 0, symbol = 1, uppercase = 2
    private int number;
    private int[] vectorForm = new int[4];
    private String faceValue;

    public Card(String inputLine) throws Exception
    {
       this.setFaceValue(inputLine);
       int []cardVector = parseLine(inputLine);
       this.setVectorForm(cardVector);
       this.setColor(cardVector[0]);
       this.setSymbol(cardVector[1]);
       this.setShading(cardVector[2]);
       this.setNumber(cardVector[3]);
    }

    public String getFaceValue()
    {
        return faceValue;
    }

    public void setFaceValue(String faceValue) {
        this.faceValue = faceValue;
    }

    public int[] getVectorForm()
    {
        return vectorForm;
    }

    public void setVectorForm(int[] vectorForm)
    {
        this.vectorForm = vectorForm;
    }

    public int getColor()
    {
        return color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public int getSymbol()
    {
        return symbol;
    }

    public void setSymbol(int symbol)
    {
        this.symbol = symbol;
    }

    public int getShading()
    {
        return shading;
    }

    public void setShading(int shading)
    {
        this.shading = shading;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public int[] parseLine(String line) throws Exception
    {
        int spaceIndex = line.indexOf(SPACE_CHAR, 0);
        int lastIndex = line.length();
//        System.out.println("last Index : " + lastIndex);
//        System.out.println("Space Index : " + spaceIndex);



        String color = line.substring(0, spaceIndex).toLowerCase();

        spaceIndex = line.lastIndexOf(SPACE_CHAR, lastIndex);//avoid errors with multiple space input by moving to last space
        int numberCode = lastIndex - spaceIndex - 2;//number starts at 0 but 0 maps to 1 symbol, 1 to 2, 2 to 3
        if(numberCode>2)//should never be greater than 2 (which means more than 3)
        {
            numberCode = -1;
        }


        char symbol = line.charAt(lastIndex-1);
        char symbolLetter = 'e';//error

//        System.out.println("Color : " + color);
//
//        System.out.println("Symbol : " + symbol);
//
//        System.out.println("Number :" + numberCode );

        if(symbol == 'a' || symbol =='A' || symbol == '@')
        {
            symbolLetter = 'a';
        }
        if(symbol == 'h' || symbol =='H' || symbol == '#')
        {
            symbolLetter = 'h';
        }
        if(symbol == 's' || symbol =='S' || symbol == '$')
        {
            symbolLetter = 's';
        }

//        System.out.println("Letter : " + symbolLetter) ;
        int colorCode = -1;//indicates error in input
        if (color.equals("blue"))
        {
            colorCode = 0;
        }
        else if(color.equals("green"))
        {
            colorCode = 1;
        }
        else if(color.equals("yellow"))
        {
            colorCode = 2;
        }


        int symbolCode = -1;
        int shadingCode = -1;

        if(symbolLetter =='a')
        {
            symbolCode = 0;
            if(symbol=='a')//0 = lower
            {
                shadingCode = 0;
            }
            else if(symbol=='@')//1 = symbol
            {
                shadingCode = 1;
            }
            else if(symbol=='A')//2 = upper
            {
                shadingCode = 2;
            }

        }
        else if(symbolLetter == 'h')
        {
            symbolCode = 1;
            if(symbol=='h')//0 = lower
            {
                shadingCode = 0;
            }
            else if(symbol=='#')//1 = symbol
            {
                shadingCode = 1;
            }
            else if(symbol=='H')//2 = upper
            {
                shadingCode = 2;
            }
        }
        else if(symbolLetter == 's')
        {
            symbolCode = 2;
            if(symbol=='s')//0 = lower
            {
                shadingCode = 0;
            }
            else if(symbol=='$')//1 = symbol
            {
                shadingCode = 1;
            }
            else if(symbol=='S')//2 = upper
            {
                shadingCode = 2;
            }
        }

        else
        {
            System.out.println("Error parsing symbol:" + symbolLetter);
        }

//        System.out.println("Shading Code : " + shadingCode);




        int [] cardVector = new int[4];

        cardVector[0] = colorCode;
        cardVector[1] = symbolCode;
        cardVector[2] = shadingCode;
        cardVector[3] = numberCode;

        for(int i = 0; i < cardVector.length; i++)
        {
            if (cardVector[i] == -1)
            {
                throw new Exception();
            }
        }

        return cardVector;

    }

    public boolean equalsVector(int setVector[])
    {
        boolean equals = true;
        for(int i = 0; i<setVector.length; i++)
        {
            if(setVector[i] != this.getVectorForm()[i])
            {
                equals = false;
            }
        }
        return false;
    }


}
