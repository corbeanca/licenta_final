package a_barbu.gps_agenda;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Alex on 24-May-17.
 */

public class Itinerary {

    public String desc;
    public String name;
    public String date;
    List<Integer> Markers;
    List<Integer> Link;
    public int length =0;
    public int move;
    Map<Integer,Integer> IDs = new HashMap<>();
//nu stiu daca o sa vrea cu Markers
    public Itinerary(){

    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Itinerary (String desc, String name, int move ){
        this.desc = desc;
        this.name = name;
        this.move = move;

        //format();
    }
//    private void format() {
//        for (int i=0;i<25;i++) {
//            Markers.get(i) = 0;
//           // Markers[i][1] = 0;
//        }
//    }
//
//    public void SetDate(String d){
//        this.date=d;
//    }
//
    public void addID(int i){
        Markers.add(i);
        Link.add(-1);
        this.length += this.length;
    }

//    public void addIDsec(int i, int j){
//            Markers[i][1]=j;
//    }

    public boolean HasMk(MarkerObj mk){
      //  int lg = this.length;
        for(int p = 1; p<= Markers.size(); p++)
            if (this.Markers.get(p) == mk.ID)
                return true;
        return false;
    }

//    public void removeID(int i){
//        for(int p=0;p<length;p++)
//            if (Markers[p][0]==i)
//            {
//                for(int q=p;p<length-1;q++){
//                    Markers[q][0]=Markers[q+1][0];
//                    Markers[q][1]=Markers[q+1][1];
//                }
//            }
//        Markers[length][0]=0;
//        Markers[length][1]=0;
//        this.length=this.length-1;
//    }

    @Override
    public String toString() {
        return this.name ;
    }

}
