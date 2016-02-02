package SET;
import java.util.*;
/**
 * Created by Bharat on 12/02/15.
 */
public class Set
{

    private int disjointSets;
    private LinkedList<Set> listOfDisjoints = new LinkedList<Set>();
    private int[] comparisonVector;//for every 2 Cards, only 1 other can be in the set and has value equal to this
    private boolean isComplete;
    private Card[] cardCollection = new Card[3];



    public int getDisjointSets() //returns number of disjoint sets corresponding to this set
    {
        return disjointSets;
    }

    public void setDisjointSets(int disjointSets)
    {
        this.disjointSets = disjointSets;
    }

    public Set(Card card1, Card card2)//we create a set for any 2 cards and then check whether 3rd meets criteria
    {
        this.isComplete = false;
        this.addToCollection(card1, 0);
        this.addToCollection(card2, 1);
        int[] vec1 = card1.getVectorForm();
        int[] vec2 = card2.getVectorForm();
        this.setComparisonVector(vec1, vec2);
    }

    public LinkedList<Set> getListOfDisjoints() {
        return listOfDisjoints;
    }

    public void addToListDisjoint(Set s)
    {
        this.listOfDisjoints.add(s);
    }


    public Card[] getCardCollection()
    {
        return cardCollection;
    }

    public void addToCollection(Card C, int i)
    {
        this.cardCollection[i] = C;
    }



    public int[] getComparisonVector()
    {
        return comparisonVector;
    }

    public boolean compareAndComplete(Card c)//compares 3rd card with vector for set and saves if accepted
    {
        boolean equals = true;
        int[] v = c.getVectorForm();
        for(int i=0; i < v.length; i++)
        {
            if(v[i]!=this.getComparisonVector()[i])
                equals = false;
        }
        if(equals)
        {
            this.setComplete(equals);
            this.addToCollection(c, 2);
        }
        return equals;
    }

    public void setComparisonVector(int[] v1, int[] v2)
    {
        int[] compareResult = new int[v1.length];
        for(int i = 0; i < v1.length; i++)
        {
            if(v1[i]==v2[i])
                compareResult[i] = v1[i];//all three must have same value for this attribute
            else
            {
                compareResult[i]=0;
                while(compareResult[i]==v1[i] || compareResult[i]==v2[i])//third must have value different from others
                {
                    compareResult[i]++;
                }
            }
        }
        this.comparisonVector = compareResult;
    }

    public boolean isComplete()
    {
        return isComplete;
    }

    public void setComplete(boolean isComplete)
    {
        this.isComplete = isComplete;
    }
}
