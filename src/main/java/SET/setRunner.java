package SET;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.*;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

/**
 * Created by Bharat on 12/02/15.
 */
public class setRunner
{
    public static void main(String args[])
    {
        System.out.print("Enter number of cards: ");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));



        String  inputLine = null;
        int number = 0;

        try
        {
            inputLine = br.readLine().trim();
            number = Integer.parseInt(inputLine);

        }
        catch (IOException ioe)
        {
            System.out.println("IO error trying to read line!");
            ioe.printStackTrace();
            System.exit(1);
        }
        catch (Exception e)
        {
            System.out.println("IO error trying to read number!");
            e.printStackTrace();
            System.exit(1);
        }

        ArrayList<Card> gameCards = new ArrayList<Card>();

        for(int i=0; i<number ; i++)
        {
            System.out.print("Enter another card: ");

            br = new BufferedReader(new InputStreamReader(System.in));

            inputLine = null;

            try
            {
                inputLine = br.readLine().trim();
                Card temp = new Card(inputLine);
                gameCards.add(temp);
//                int[] vector = temp.getVectorForm();
//                System.out.println(Arrays.toString(vector));
            }
            catch (IOException ioe)
            {
                System.out.println("IO error trying to read line!");
                ioe.printStackTrace();
                System.exit(1);
            }
            catch (Exception e)
            {
                System.out.println("Error with the input, try again");//catch exception and accept new input
                i--;
            }
        }

        for(int i=0; i<number ; i++)
        {
            int[] vectorCard = gameCards.get(i).getVectorForm();
        }

        startGame(gameCards);

//        Code to test Input Parsing::


//        String inputString = "blue ##";
//        testParser(inputString);
//
//        inputString = "yellow a";
//        testParser(inputString);
//
//        inputString = "Green $$$";
//        testParser(inputString);
//
//        inputString = "blUe A";
//        testParser(inputString);
//
//        inputString = "yellow HHH";
//        testParser(inputString);


    }

    public static void startGame(ArrayList<Card> cardList)
    {
        int number = cardList.size();
        int maxSets = 0;

        ArrayList<Set> completeSets = new ArrayList<Set>();

        for(int i=0; i<number; i++)
        {
            for(int j=i+1; j<number; j++)
            {
                Set pair = new Set(cardList.get(i), cardList.get(j));
                for(int t= j+1; t<number; t++)
                {
                    if(pair.compareAndComplete(cardList.get(t)))
                    {
                        maxSets++;
                        completeSets.add(pair);
                    }

                }
            }
        }

        System.out.println("Max Sets:" + maxSets);

        for (Set s : completeSets)
        {
            int numDisjoints = 0;
            Card[] cardsInSet = s.getCardCollection();
            LinkedList<Card> cardsAdded = new LinkedList<Card>();
            for(Set other : completeSets)
            {
                if(other!=s)
                {

                    Card[] cardsInOther = other.getCardCollection();
                    boolean disjoint = true;
                    for(int i=0; i<cardsInOther.length; i++)
                    {
                        for(int j=0; j<cardsInSet.length; j++) {
                            if ((Arrays.toString(cardsInOther[i].getVectorForm()).equals(Arrays.toString(cardsInSet[j].getVectorForm()))))
                                disjoint = false;
                            for(Card c : cardsAdded)
                            {
                                if((Arrays.toString(cardsInOther[i].getVectorForm()).equals(Arrays.toString(c.getVectorForm()))))
                                    disjoint = false;
                            }
                        }

                    }

                    if (disjoint==true)
                    {
                        numDisjoints++;

                        for(int i=0; i<cardsInOther.length; i++)
                        {
                            cardsAdded.add(cardsInOther[i]);
                        }

                        s.addToListDisjoint(other);
                    }
                }
            }
            s.setDisjointSets(numDisjoints);
        }
        int maxDisjoint = 0;
        for (Set s : completeSets)
        {
            if(maxDisjoint<s.getDisjointSets())
            {
                maxDisjoint = s.getDisjointSets();
            }
        }

        for (Set s : completeSets)
        {
            if(maxDisjoint==s.getDisjointSets())
            {
                System.out.println("Max Disjoint Sets : " + (maxDisjoint+1));//Add 1 to include self
                System.out.println();

                Card[] setCards = s.getCardCollection();
                for(int i=0; i<setCards.length; i++)
                {
                    System.out.println(setCards[i].getFaceValue());
                }
                System.out.println();

                LinkedList<Set> disjoints = s.getListOfDisjoints();
                for(Set set : disjoints)
                {
                    Card[] dispCards = set.getCardCollection();
                    for(int i=0; i<dispCards.length; i++)
                    {
                        System.out.println(dispCards[i].getFaceValue());
                    }
                    System.out.println();
                }

                return;
            }
        }

    }

    public static void testParser(String inputString)
    {
//        System.out.println("Input : " + inputString);
//        Card testCard = new Card(inputString);
//        int[] vectorCard = testCard.getVectorForm();
//        System.out.println("Color : " + vectorCard[0] );
//        System.out.println("Symbol : " + vectorCard[1] );
//        System.out.println("Shading : " + vectorCard[2] );
//        System.out.println("Number : " + vectorCard[3] );
    }
}

