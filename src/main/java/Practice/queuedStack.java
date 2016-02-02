package Practice;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Bharat on 02/03/15.
 */
public class queuedStack
{
    Queue<Integer> que;
    Queue<Integer> temp;

    int size = 0;
    public queuedStack()
    {
        this.que = new LinkedList<Integer>();
        this.temp = new LinkedList<Integer>();
    }


}
