package ca.mcgill.splendorserver.models;

import java.util.HashMap;

public class Noble {
   private String id;
   private int pts;
   private HashMap<String, Integer> cost;

    /**
     * Sole constructor.  (For invocation by subclass constructors, typically
     * implicit.)
     */
   public Noble(){

   }

   public String getId() {
       return id;
   }

   public int getPts() {
       return pts;
   }

   public HashMap<String, Integer> getCost() {
       return cost;
   }

   public void setId(String id) {
       this.id = id;
   }

   public void setPts(int pts) {
       this.pts = pts;
   }

   public void setCost(HashMap<String, Integer> cost) {
       this.cost = cost;
   }

}
