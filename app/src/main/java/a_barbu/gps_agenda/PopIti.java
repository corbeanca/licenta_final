package a_barbu.gps_agenda;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PopIti extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    static Itinerary newIti;
    DatabaseReference Ref = Principal.database.getReference();
    int pos = 0;
    int year1, month1, day1;
    static final int DIALOG_ID = 0;
    EditText name;
    EditText desc;
    TextView exists;
    TextView special;
    ImageView calendar;
    List<Itinerary> itineraries;
    ImageView order;
    ListView lw_iti;
    RadioGroup r_group;
    RadioButton rb;

    ArrayAdapter<Itinerary> adapter;
    String cal_date = "";
    TextView date;
    int iti_move=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popiti);
        final Calendar cal = Calendar.getInstance();
        year1 = cal.get(Calendar.YEAR);
        month1 = cal.get(Calendar.MONTH);
        day1 = cal.get(Calendar.DAY_OF_MONTH);
        lw_iti = (ListView) findViewById(R.id.popiti_list);
        r_group = (RadioGroup) findViewById(R.id.popiti_rg);


        ImageView plus = (ImageView) findViewById(R.id.popiti_plus);
        ImageView minus = (ImageView) findViewById(R.id.popiti_minus);
        ImageView update = (ImageView) findViewById(R.id.popiti_update);
        ImageView mk_list = (ImageView) findViewById(R.id.popiti_up_down);
        mk_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopIti.this,Itinerary_mk.class));
            }
        });
        order = (ImageView) findViewById(R.id.popiti_up_down);
        order.setVisibility(View.INVISIBLE);
        calendar = (ImageView) findViewById(R.id.popiti_calendar);
        exists = (TextView) findViewById(R.id.popiti_exists);
        date = (TextView) findViewById(R.id.popiti_date);
        special = (TextView) findViewById(R.id.popiti_special);

        gocalendar();
        itineraries = importItinerary();



        adapter_iti(itineraries);

        name = (EditText) findViewById(R.id.popiti_name);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exists.setVisibility(View.INVISIBLE);
                special.setVisibility(View.INVISIBLE);
            }
        });
        desc = (EditText) findViewById(R.id.popiti_desc);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        getWindow().setLayout((int) (w * .7), (int) (h * .6));
  //      getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

    private void adapter_iti(List<Itinerary> lista) {
        //    adapter = null;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        lw_iti.setAdapter(adapter);
        lw_iti.setOnItemClickListener(this);
    }



    public void gocalendar() {
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

//    public void createEvent(GoogleAccountCredential mCredential){
//        HttpTransport transport = AndroidHttp.newCompatibleTransport();
//        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//        com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(
//                transport, jsonFactory, mCredential)
//                .setApplicationName("R_D_Location Callendar")
//                .build();
//
//        Event event = new Event()
//                .setSummary("Google I/O 2015")
//                .setLocation("800 Howard St., San Francisco, CA 94103")
//                .setDescription("A chance to hear more about Google's developer products.");
//
//        DateTime startDateTime = new DateTime("2017-06-30T09:00:00-07:00");
//        EventDateTime start = new EventDateTime()
//                .setDateTime(startDateTime)
//                .setTimeZone("America/Los_Angeles");
//        event.setStart(start);
//
//        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
//
//        EventReminder[] reminderOverrides = new EventReminder[] {
//                new EventReminder().setMethod("email").setMinutes(24 * 60),
//                new EventReminder().setMethod("popup").setMinutes(10),
//        };
//        Event.Reminders reminders = new Event.Reminders()
//                .setUseDefault(false)
//                .setOverrides(Arrays.asList(reminderOverrides));
//        event.setReminders(reminders);
//
//        String calendarId = "primary";
//        event = service.events().insert(calendarId, event).execute();
//        System.out.printf("Event created: %s\n", event.getHtmlLink());
//    }


    public void createEvent(View v) {

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 5, 30, 7, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 5, 30, 8, 30);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Yoga")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        startActivity(intent);

    }





    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // lw_iti.requestLayout();
        name.setText(itineraries.get(position).getName());
        desc.setText(itineraries.get(position).getDesc());
        setDate(itineraries.get(position).date);
        newIti = itineraries.get(position);
        pos = position;
        order.setVisibility(View.VISIBLE);
        load_iti_move(newIti.name);
        importLinks(newIti);

    }

    public void updateItionline(Itinerary it, String name) {
        DatabaseReference R_add = Ref.child(ShowPref("Email")).child("Itinerary").child(name);
        R_add.setValue(newIti);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, dpickListner, year1, month1, day1);
        }
        return null;
    }

    public void select_move(View v){
        int radiobuttonid = r_group.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(radiobuttonid);
        RadioButton r1 = (RadioButton) findViewById(R.id.popiti_car);
        RadioButton r2 = (RadioButton) findViewById(R.id.popiti_bicycle);
        RadioButton r3 = (RadioButton) findViewById(R.id.popiti_walk) ;
        RadioButton r4 = (RadioButton) findViewById(R.id.popiti_transit) ;

        rb.setChecked(true);

        switch (v.getId()) {
            case R.id.popiti_car:
                rb.setButtonDrawable(R.mipmap.car_r);
                r3.setButtonDrawable(R.mipmap.walk_g);
                r2.setButtonDrawable(R.mipmap.bicycle_g);
                r4.setButtonDrawable(R.mipmap.transit_g);
                iti_move=1;

                break;
            case R.id.popiti_bicycle:
                rb.setButtonDrawable(R.mipmap.bicycle_r);
                r3.setButtonDrawable(R.mipmap.walk_g);
                r1.setButtonDrawable(R.mipmap.car_g);
                r4.setButtonDrawable(R.mipmap.transit_g);
                iti_move=3;

                break;
            case R.id.popiti_transit:
                rb.setButtonDrawable(R.mipmap.transit_r);
                r3.setButtonDrawable(R.mipmap.walk_g);
                r1.setButtonDrawable(R.mipmap.car_g);
                r2.setButtonDrawable(R.mipmap.bicycle_g);
                iti_move=4;

                break;
            case R.id.popiti_walk:
                rb.setButtonDrawable(R.mipmap.walk_r);
                r4.setButtonDrawable(R.mipmap.transit_g);
                r1.setButtonDrawable(R.mipmap.car_g);
                r2.setButtonDrawable(R.mipmap.bicycle_g);
                iti_move=2;


                break;
        }


    }

    private DatePickerDialog.OnDateSetListener dpickListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year1 = year;
            month1 = month + 1;
            day1 = dayOfMonth;
            Toast.makeText(PopIti.this, "Date set !", Toast.LENGTH_SHORT).show();
            cal_date = new Integer(dayOfMonth).toString();
            cal_date = cal_date + " / " + new Integer(month + 1).toString();
            cal_date = cal_date + " / " + new Integer(year).toString();
            setDate(cal_date);

        }

    };

    private void setDate(String cal_date) {
        date.setText(cal_date);
    }

    public void clickIPlus(View v) {
        newIti = new Itinerary();
        String n = name.getText().toString();
        if (n.equals("")) {
            Toast.makeText(this, "Insert name first", Toast.LENGTH_SHORT).show();
            return;
        }

        looper:
        {
            for (int i = 0; i < itineraries.size(); i++) {
                Itinerary x = itineraries.get(i);
                if (n.equals(x.getName())) {
                    exists.bringToFront();
                    exists.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "nu s-a inserat", Toast.LENGTH_SHORT).show();
                    break looper;
                }
            }

            Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(n);
            boolean b = m.find();

            if (b) {
                special.bringToFront();
                special.setVisibility(View.VISIBLE);
                Toast.makeText(this, "nu s-a inserat", Toast.LENGTH_SHORT).show();
            } else {
                exists.setVisibility(View.INVISIBLE);
                newIti.setName(n);
                newIti.setDesc(desc.getText().toString());
                newIti.date = cal_date;
                newIti.move = iti_move;
                itineraries.add(newIti);
                updateItionline(newIti, name.getText().toString());

                adapter.add(newIti);
                adapter.notifyDataSetChanged();
                loader();
                Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clickIMinus(View v) {

        itineraries.remove(pos);



        DatabaseReference R_del = Ref.child(ShowPref("Email")).child("Itinerary").child(newIti.getName());
        R_del.removeValue();
        loader();
        Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();


        adapter.clear();
        adapter_iti(itineraries);
        adapter.notifyDataSetChanged();
        // itineraries.clear();
    }

    public void clickUpdate(View v) {
        newIti.date = cal_date;
        newIti.desc = desc.getText().toString();
        updateItionline(newIti, newIti.getName());

        itineraries.get(pos).desc = newIti.getDesc();
        itineraries.get(pos).date = newIti.date;
        adapter_iti(itineraries);
        adapter.notifyDataSetChanged();
        loader();
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();

    }

    public void loader(){
        new CountDownTimer(2000,1000){
            ProgressDialog pdLoading = new ProgressDialog(PopIti.this);

            @Override
            public void onTick(long millisUntilFinished) {
                pdLoading.setMessage("\tLoading...");
                pdLoading.show();
            }

            @Override
            public void onFinish() {
                pdLoading.dismiss();
            }
        }.start();
    }

    public void load_iti_move(String name){
        Ref.child(ShowPref("email")).child("Itinerary").child(name).child("move").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long m = (long) dataSnapshot.getValue();
                int x=((int) m);
                r_group.clearCheck();
                RadioButton r1 = (RadioButton) findViewById(R.id.popiti_car);
                RadioButton r2 = (RadioButton) findViewById(R.id.popiti_bicycle);
                RadioButton r3 = (RadioButton) findViewById(R.id.popiti_walk) ;
                RadioButton r4 = (RadioButton) findViewById(R.id.popiti_transit) ;

                switch (x) {

                    case 1:
                        rb = r1;
                        rb.setButtonDrawable(R.mipmap.car_r);
                        r3.setButtonDrawable(R.mipmap.walk_g);
                        r2.setButtonDrawable(R.mipmap.bicycle_g);
                        r4.setButtonDrawable(R.mipmap.transit_g);
                        rb.setChecked(true);

                        break;
                    case 2:
                        rb=r3;
                        rb.setButtonDrawable(R.mipmap.walk_r);
                        r4.setButtonDrawable(R.mipmap.transit_g);
                        r1.setButtonDrawable(R.mipmap.car_g);
                        r2.setButtonDrawable(R.mipmap.bicycle_g);
                        rb.setChecked(true);
                        iti_move=2;
                        break;
                    case 3:
                        rb=r2;
                        rb.setButtonDrawable(R.mipmap.bicycle_r);
                        r3.setButtonDrawable(R.mipmap.walk_g);
                        r1.setButtonDrawable(R.mipmap.car_g);
                        r4.setButtonDrawable(R.mipmap.transit_g);
                        rb.setChecked(true);
                        iti_move=3;
                        break;
                    case 4:
                        rb=r4;
                        rb.setButtonDrawable(R.mipmap.transit_r);
                        r3.setButtonDrawable(R.mipmap.walk_g);
                        r1.setButtonDrawable(R.mipmap.car_g);
                        r2.setButtonDrawable(R.mipmap.bicycle_g);
                        rb.setChecked(true);
                        iti_move=4;
                        break;

                    case 0:
                        r4.setButtonDrawable(R.mipmap.transit_g);
                        r1.setButtonDrawable(R.mipmap.car_g);
                        r2.setButtonDrawable(R.mipmap.bicycle_g);
                        r3.setButtonDrawable(R.mipmap.walk_g);
                        iti_move=0;
                        break;
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public String ShowPref(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getString(key, "var");
    }

    public ArrayList<Itinerary> importItinerary() {
        final ArrayList<Itinerary> iti = new ArrayList<>();
        Ref.child(ShowPref("email")).child("Itinerary")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        itineraries.clear();
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            Itinerary value = child.getValue(Itinerary.class);
                            iti.add(value);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        //  Toast.makeText(this, "Itineraries imported", Toast.LENGTH_LONG).show();

        return iti;
    }



    public void importLinks(final Itinerary it) {

            Ref.child(ShowPref("email")).child("Itinerary").child(it.name).child("Markers")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newIti.Markers = new ArrayList<>();
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for (DataSnapshot child : children) {
                                Integer value = child.getValue(Integer.class);
                                newIti.Markers.add(value);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
       // Ref.child(ShowPref("email")).child("Itinerary").child(it.name).removeEventListener(ValueEventListener);
        }




}