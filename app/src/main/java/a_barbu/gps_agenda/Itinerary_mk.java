package a_barbu.gps_agenda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Itinerary_mk extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    ListView lw_mk;
    ListView mk_present;
    List<MarkerObj> markers;
    ArrayAdapter<MarkerObj> adapter2;
    DatabaseReference Ref = Principal.database.getReference();
    static boolean [] present_bool ;
    static ArrayList<String> names;
    static ArrayList<Integer> model;
    ArrayList<String> present_list;
    TextView text_empty;
    ImageView up;
    ImageView down;
    ImageView update;
    ImageView reverse;
    AdapterListaMark adapter;
    ArrayList<String> markers_title;
    static int [] IDs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.itinerary_mk);
        text_empty = (TextView) findViewById(R.id.popML_empty_text);
        text_empty.setVisibility(View.INVISIBLE);
        up = (ImageView) findViewById(R.id.popML_up);
        down = (ImageView) findViewById(R.id.popML_down);
        update = (ImageView) findViewById(R.id.popML_update);
        reverse = (ImageView) findViewById(R.id.popML_undo);
        TextView iti_name = (TextView) findViewById(R.id.popML_title);
        iti_name.setText(PopIti.newIti.name);
        names = new ArrayList<>();
        model = new ArrayList<>();
        check();

        lw_mk = (ListView) findViewById(R.id.popML_markers);


        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToIti();
            }
        });

       // markers = Principal.listaMarkere;
              //  importMarkers();

        mk_present = (ListView) findViewById(R.id.popML_pos);

        markers_title = getNames();
     //   adapter_mk(markers);
        draw_present();


        adapter = new AdapterListaMark(this, R.layout.side_list_item_marker,markers_title );
        lw_mk.setAdapter(adapter);
        lw_mk.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                         @Override
                                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                             //                   Toast.makeText(Principal.this, "Shape changed for  " + position, Toast.LENGTH_SHORT).show();

                                             if (present_bool[position])
                                                 present_bool[position] = false;
                                             else present_bool[position] = true;

                                             adapter.notifyDataSetChanged();
                                             updateView(position);

                                         }
                                     });



        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        getWindow().setLayout((int) (w * .7), (int) (h * .6));
    }

    private ArrayList<String> getNames() {
        ArrayList<String> list = new ArrayList<>();
        for (int i=0;i<Principal.listaMarkere.size();i++){
            list.add(Principal.listaMarkere.get(i).title);
        }

        return list;
    }

    private void adapter_mk(List<MarkerObj> lista) {
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        lw_mk.setAdapter(adapter2);
        lw_mk.setOnItemClickListener(this);
    }

    public void check(){
                present_bool = new boolean[Principal.listaMarkere.size()];

            for (int i = 0; i < Principal.listaMarkere.size(); i++) {
                names.add(i,Principal.listaMarkere.get(i).title);
                model.add(i,Principal.listaMarkere.get(i).model);
                int pos = 0;
                for (int value : PopIti.newIti.Markers)
                if ( Principal.listaMarkere.get(i).ID ==value)
                {present_bool[i]=true;
                    break;
                }
                else present_bool[i]=false;
            }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        if (present_bool[position])
//            present_bool[position] = false;
//        else present_bool[position] = true;
//
//        adapter.notifyDataSetChanged();
//        updateView(position);


        Toast.makeText(this, "////   will be added", Toast.LENGTH_SHORT).show();
    }

    public void draw_present(){
        present_list = new ArrayList<>();
        int pos_list=0;

        if (PopIti.newIti.Markers ==null)
            text_empty.setVisibility(View.VISIBLE);
        else {
            text_empty.setVisibility(View.INVISIBLE);
            mk_present.bringToFront();
            while (pos_list < PopIti.newIti.Markers.size()) {

                for (int i = 0; i < Principal.listaMarkere.size(); i++) {
                    if (PopIti.newIti.Markers.get(pos_list) == Principal.listaMarkere.get(i).ID) {
                        pos_list++;
                        present_list.add(Principal.listaMarkere.get(i).title);
                    }
                }
            }
        }
    }

    public void addToIti(){

    }

    public ArrayList<MarkerObj> importMarkers() {
        final ArrayList<MarkerObj> mk = new ArrayList<>();
        Ref.child(ShowPref("email")).child("Marker")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            MarkerObj value = child.getValue(MarkerObj.class);
                            mk.add(value);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        return mk;
    }

    private void updateView(int index){
        View v = lw_mk.getChildAt(index -
                lw_mk.getFirstVisiblePosition());
        if(v == null)
            return;

    }

    public String ShowPref(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getString(key, "var");
    }

    public static class AdapterListaMark extends ArrayAdapter<String> {

        private int layout;


        private AdapterListaMark(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder mainViewholder = null;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.side_list_marker_img);
                viewHolder.title = (TextView) convertView.findViewById(R.id.side_list_text);
                viewHolder.check = (CheckBox) convertView.findViewById(R.id.side_list_check);


             //   if (PopIti.newIti.HasMk())



                if (present_bool[position])
                    viewHolder.check.setChecked(true);
                else viewHolder.check.setChecked(false);

                convertView.setTag(viewHolder);
            }

            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.title.setText(getItem(position));

            if (present_bool[position])
                mainViewholder.check.setChecked(true);
            else mainViewholder.check.setChecked(false);


            switch (model.get( position)) {
                case 0://red green blue yell
                    mainViewholder.thumbnail.setImageResource(R.mipmap.circle_red);
                    break;
                case 1:
                    mainViewholder.thumbnail.setImageResource(R.mipmap.circle_green);
                    break;
                case 2:
                    mainViewholder.thumbnail.setImageResource(R.mipmap.circle_blue);
                    break;
                case 3:
                    mainViewholder.thumbnail.setImageResource(R.mipmap.circle_yellow);
                    break;
                case 4:
                    mainViewholder.thumbnail.setImageResource(R.mipmap.square_red);
                    break;
                case 5:
                    mainViewholder.thumbnail.setImageResource(R.mipmap.square_green);
                    break;
                case 6:
                    mainViewholder.thumbnail.setImageResource(R.mipmap.square_blue);
                    break;
                case 7:
                    mainViewholder.thumbnail.setImageResource(R.mipmap.square_yellow);
                    break;
                case 8:
                    mainViewholder.thumbnail.setImageResource(R.mipmap.shield_red);
                    break;
                case 9:
                    mainViewholder.thumbnail.setImageResource(R.mipmap.shield_green);
                    break;
                case 10:
                    mainViewholder.thumbnail.setImageResource(R.mipmap.shield_blue);
                    break;
                case 11:
                    mainViewholder.thumbnail.setImageResource(R.mipmap.shield_yellow);
                    break;

            }
            return convertView;
        }


    }

    public static class ViewHolder {
        ImageView thumbnail;
        TextView title;
        CheckBox check;
    }

}
