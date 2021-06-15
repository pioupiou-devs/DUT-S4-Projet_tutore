package fr.iut.orsay.myapplication;

public class Graph
    {
        private static int NUM_ID = 0;
        private int id;
        private String name;
        
        Graph(String name)
            {
                id = NUM_ID;
                NUM_ID++;
                this.name = name;
            }
        
        public int getId()
            {
                return id;
            }
        
        public String getName()
            {
                return name;
            }
        
        public void setName(String name)
            {
                this.name = name;
            }
    }
