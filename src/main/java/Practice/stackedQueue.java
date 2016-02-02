package Practice;


import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Created by Bharat on 02/03/15.
 */
public class stackedQueue
{
    Stack<Integer> input;
    Stack<Integer> output;

    int size = 0;

    public stackedQueue()
    {

        input = new Stack<Integer>();
        output = new Stack<Integer>();
    }

    public void insert(Integer i)
    {
        if(this.size==0)
        {
                input.push(i);

        }
        else
        {
            int n = input.size();
            for(int j = 0; j<n; j++)
            {
                output.push(input.pop());
            }
            input.push(i);
            for(int j = 0; j<n; j++)
            {
                input.push(output.pop());
            }

        }
        size++;
    }

    public Integer delete() throws Exception
    {
        if(this.size==0)
        {
            throw new NoSuchElementException("Queue Underflow");
        }
        else
        {
            this.size--;
            return input.pop();
        }
    }

    public Integer peek() throws Exception
    {
        if(this.size==0)
        {
            throw new NoSuchElementException("Queue Underflow");
        }
        else
        {
            return input.peek();
        }
    }


}
