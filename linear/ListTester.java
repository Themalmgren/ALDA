package alda.linear;

import java.util.*;

public class ListTester
{
    private MyALDAList<Integer> myList = new MyALDAList<Integer>();
    
    public static void main(String[] args)
    {
        Long startTime = System.currentTimeMillis();
        
        ListTester test1 = new ListTester();
        test1.run();
        
        Long duration = System.currentTimeMillis()-startTime;
        System.out.println("\nIt took: "+duration+" ms" );
    }
    
    public void run()
    {
        for(int i=0;i<100;i++)
        {
            Random rand = new Random();
            myList.add(rand.nextInt(100));
            myList.add(i);
        }
        myList.add(4,100);
        myList.add(5,200);
        myList.add(6,300);
        myList.add(7,400);
        myList.add(8,500);
        myList.add(0, null);
        myList.add(1, null);
        
        System.out.println(myList.toString()+"\n");
        
        Integer var = 33;
        
        System.out.println("The number: "+var+(myList.contains(var)?" do":" does not"  )+" exist in the list");
        System.out.println("Index of it is: "+myList.indexOf(var)+"\n");
        
        System.out.println("Size of list is: "+myList.size()+" elements.");
//        System.out.println("Size of iteration lookup: "+myList.trueSize()+" elements.\n");
        
        for(int i=0;i<myList.size();i++)
        {
            Integer data = myList.get(i);
            System.out.println("Index: "+i+" - Value: "+data);
        }
        
        Integer var2 = new Integer(79);
        
        myList.remove(10);
        myList.remove(12);
        myList.remove(null);
        
        Random rand = new Random();
        Integer randNr = rand.nextInt(100);
        
        System.out.println("\nRemoved elements.\n");
//        System.out.println("Random element with the value: "+randNr+" was "
//        +(myList.remove(randNr)?"found and removed.":"not removed.")+"\n");
        
        for(int i=0;i<myList.size();i++)
        {
            Integer data = myList.get(i);
            System.out.println("Index: "+i+" - Value: "+data);
        }
        
        System.out.println(myList.get(0));
    }
}