package com.raismedia.taxiclient.fragment.customer;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import java.util.Calendar;
//import android.icu.util.Calendar;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

//import com.applandeo.materialcalendarview.CalendarView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.settings.AppConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RaisMedia on 01/01/2019.
 */

public class BottomSheetFragmentBookingLater extends BottomSheetDialogFragment implements TimePickerDialog.OnTimeSetListener/*, OnSelectDateListener*/ {
    private static Context context;
    private Activity activity;
    private TextView cout_requete,distance_requete,envoyer, nb_day,heure_depart,duration_requete;
    private String distance, distance_init,duration,img_data,place,number_poeple;
    private Location loc1, loc2;
    private LinearLayout layout_heure_depart;
    private RelativeLayout layout_date_depart;
//    private EditText input_phone;
    private TextInputLayout input_layout_phone;
    private int mYear_depart, mMonth_depart, mDay_depart;
    private int mYear_fin, mMonth_fin, mDay_fin;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
//    String val_date_fin = "", val_date_depart = "";
    private String mMonth_1;
    private String[] tabDistance = {};
    private TextView annuler,start_date,end_date,heure_retour;
    private String id_type_vehicle,id_driver,id_payment;
    private String depart_name,destination_name,price,cout;
    private OnSelectDateListener listener;
    private List<java.util.Calendar> listCalendars = new ArrayList<>();
    String date_book = "",statut_round;
    private SwitchCompat switch_round;

    public BottomSheetFragmentBookingLater() {
        // Required empty public constructor
    }

    public BottomSheetFragmentBookingLater(Activity activity, Location loc1, Location loc2, String distance, String duration, String id_type_vehicle, String depart_name , String destination_name , String price, String img_data, String id_driver, String id_payment, String place, String number_poeple, String statut_round) {
        this.activity = activity;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.distance = distance;
        this.duration = duration;
        this.id_type_vehicle = id_type_vehicle;
        this.depart_name = depart_name;
        this.destination_name = destination_name;
        this.price = price;
        this.img_data = img_data;
        this.id_driver = id_driver;
        this.id_payment = id_payment;
        this.number_poeple = number_poeple;
        this.place = place;
        this.statut_round = statut_round;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_booking_later, container, false);

        context = getContext();

        cout_requete = (TextView) rootView.findViewById(R.id.cout_requete);
        distance_requete = (TextView) rootView.findViewById(R.id.distance_requete);
        duration_requete = (TextView) rootView.findViewById(R.id.duree_requete);
        annuler = (TextView) rootView.findViewById(R.id.annuler);
        envoyer = (TextView) rootView.findViewById(R.id.envoyer);

        nb_day = (TextView) rootView.findViewById(R.id.nb_day);
        heure_depart = (TextView) rootView.findViewById(R.id.heure_depart);
        end_date = (TextView) rootView.findViewById(R.id.end_date);
        start_date = (TextView) rootView.findViewById(R.id.start_date);
        heure_retour = (TextView) rootView.findViewById(R.id.heure_retour);
        layout_date_depart = (RelativeLayout) rootView.findViewById(R.id.layout_date_depart);
        layout_heure_depart = (LinearLayout) rootView.findViewById(R.id.layout_heure_depart);
//        input_phone = (EditText)rootView.findViewById(R.id.input_phone);
        input_layout_phone = (TextInputLayout)rootView.findViewById(R.id.input_layout_phone);
        switch_round = (SwitchCompat) rootView.findViewById(R.id.switch_round);

//        distance = getDistance(loc1,loc2);
        tabDistance = distance.split(" ");

        if(tabDistance[1].equals("m"))
            distance_init = tabDistance[0];
        else
            distance_init = String.valueOf(Float.parseFloat(tabDistance[0])*1000);

        cout = (price);
        cout_requete.setText(price+ M.getCurrency(context));
        distance_requete.setText(distance);
        duration_requete.setText(duration);
        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(input_phone.getText().toString().trim().length() != 0) {
                    M.showLoadingDialog(context);
                    for(int i=0; i<listCalendars.size(); i++) {
                        java.util.Calendar calendar2 = listCalendars.get(i);
                        int year = calendar2.get(Calendar.YEAR);
                        int month = calendar2.get(Calendar.MONTH);
                        int day = calendar2.get(Calendar.DAY_OF_MONTH);
                        mMonth_1 = String.valueOf(month +1);
                        if(mMonth_1.trim().length() == 1){
                            mMonth_1 = '0'+mMonth_1;
                        }
                        String date_ride = year + "-" + mMonth_1 + "-" + day;
                        if(i==0){
                            date_book = date_ride;
                        }else{
                            date_book = date_book+","+date_ride;
                        }
                    }
                    new setBookRide().execute(date_book,place,number_poeple);
//                }else
//                    Toast.makeText(activity, context.getResources().getString(R.string.entrez_correctement_vos_informations), Toast.LENGTH_SHORT).show();
            }
        });
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            mYear_depart = c.get(Calendar.YEAR);
            mMonth_depart = c.get(Calendar.MONTH);
            mDay_depart = c.get(Calendar.DAY_OF_MONTH);
            mYear_fin = c.get(Calendar.YEAR);
            mMonth_fin = c.get(Calendar.MONTH);
            mDay_fin = c.get(Calendar.DAY_OF_MONTH)+1;
        }
        start_date.setText(mDay_depart + " " + MONTHS[mMonth_depart] + ". " + mYear_depart);
        end_date.setText(mDay_depart + " " + MONTHS[mMonth_depart] + ". " + mYear_depart);
        nb_day.setText("1");
        mMonth_1 = String.valueOf(mMonth_depart +1);
        if(mMonth_1.trim().length() == 1){
            mMonth_1 = '0'+mMonth_1;
        }

//        val_date_fin = mDay_fin +"-"+mMonth_1+"-"+ mYear_fin;
        date_book = mYear_depart +"-"+mMonth_1+"-"+ mDay_depart;

        /** TIME PICKER **/
        EditText chooseTime;
        ;
        Calendar calendar;
        final int currentHour;
        final int currentMinute;
        final String[] amPm = new String[1];

        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
        heure_depart.setText(currentHour+":"+currentMinute);
        layout_heure_depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm[0] = "PM";
                        } else {
                            amPm[0] = "AM";
                        }
                        heure_depart.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        heure_retour.setText(String.format("%02d:%02d", currentHour, currentMinute));
        heure_retour.setEnabled(false);
        switch_round.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    statut_round = "yes";
                    heure_retour.setText(String.format("%02d:%02d", currentHour, currentMinute));
                    heure_retour.setEnabled(true);
                }else{
                    statut_round = "no";
                    heure_retour.setText(String.format("%02d:%02d", currentHour, currentMinute));
                    heure_retour.setEnabled(false);
                }
            }
        });
        heure_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm[0] = "PM";
                        } else {
                            amPm[0] = "AM";
                        }
                        heure_retour.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        listener = new OnSelectDateListener() {
            @Override
            public void onSelect(List<java.util.Calendar> calendars) {
                listCalendars = calendars;
                if(listCalendars.size() != 0) {
                    java.util.Calendar calendar1 = listCalendars.get(0);
                    int year = calendar1.get(Calendar.YEAR);
                    int month = calendar1.get(Calendar.MONTH);
                    int day = calendar1.get(Calendar.DAY_OF_MONTH);
                    mMonth_1 = String.valueOf(month);
                    if(mMonth_1.trim().length() == 1){
                        mMonth_1 = '0'+mMonth_1;
                    }
                    month = Integer.parseInt(mMonth_1);
                    start_date.setText(year + " " + MONTHS[month] + ". " + day);

                    calendar1 = listCalendars.get(listCalendars.size()-1);
                    year = calendar1.get(Calendar.YEAR);
                    month = calendar1.get(Calendar.MONTH);
                    day = calendar1.get(Calendar.DAY_OF_MONTH);
                    end_date.setText(year + " " + MONTHS[month] + ". " + day);

                    nb_day.setText(String.valueOf(listCalendars.size()));
                    cout = (price);
                    cout_requete.setText(price+M.getCurrency(context));
                }
            }
        };

        DatePickerBuilder builder = new DatePickerBuilder(context, listener)
                .pickerType(CalendarView.MANY_DAYS_PICKER)
                .date(java.util.Calendar.getInstance()) // Initial date as Calendar object
//                        .minimumDate(java.util.Calendar.getInstance()) // Minimum available date
//                        .maximumDate(java.util.Calendar.getInstance()) // Maximum available date
//                .disabledDays(List<Calendar>) /// List of disabled days
                .headerColor(R.color.colorBlack) // Color of the dialog header
//                .headerLabelColor(R.color.color) // Color of the header label
//                .previousButtonSrc(R.drawable.drawable) // Custom drawable of the previous arrow
//                .forwardButtonSrc(R.drawable.drawable) // Custom drawable of the forward arrow
                .previousPageChangeListener(new OnCalendarPageChangeListener(){
                    @Override
                    public void onChange() {

                    }
                }) // Listener called when scroll to the previous page
                .forwardPageChangeListener(new OnCalendarPageChangeListener(){
                    @Override
                    public void onChange() {

                    }
                }) // Listener called when scroll to the next page
                .abbreviationsBarColor(R.color.colorAbreviationBarColor) // Color of bar with day symbols
                .abbreviationsLabelsColor(R.color.white) // Color of symbol labels
                .pagesColor(R.color.colorLogoBlack) // Color of the calendar background
                .selectionColor(R.color.white) // Color of the selection circle
                .selectionLabelColor(R.color.colorLogoBlack) // Color of the label in the circle
                .daysLabelsColor(R.color.white) // Color of days numbers
                .anotherMonthsDaysLabelsColor(R.color.colorLogoBlack) // Color of visible days numbers from previous and next month page
//                .disabledDaysLabelsColor(R.color.color) // Color of disabled days numbers
                .todayLabelColor(R.color.colorPrimary) // Color of the today number
                .dialogButtonsColor(R.color.colorPrimary); // Color of "Cancel" and "OK" buttons

        com.applandeo.materialcalendarview.DatePicker datePicker = builder.build();

        layout_date_depart.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /*DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String newdayOfMonth = String.valueOf(dayOfMonth);
                                String newmonthOfYear = String.valueOf(monthOfYear+1);
                                if(String.valueOf(dayOfMonth).trim().length() == 1){
                                    newdayOfMonth = '0'+newdayOfMonth;
                                }
                                if(newmonthOfYear.trim().length() == 1){
                                    newmonthOfYear = '0'+newmonthOfYear;
                                }
                                nb_day.setText(newdayOfMonth + " " + MONTHS[monthOfYear] + ". " + year);
                                val_date_depart = newdayOfMonth+"-"+newmonthOfYear+"-"+year;

                                mYear_depart = year;
                                mMonth_depart = monthOfYear;
                                mDay_depart = Integer.parseInt(newdayOfMonth);
                            }
                        }, mYear_depart, mMonth_depart, mDay_depart);
                datePickerDialog.show();*/

                datePicker.show();
            }
        });


        /*CalendarView calendarView = (CalendarView) rootView.findViewById(R.id.calendarView);
        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
//                Toast.makeText(context, "Forward", Toast.LENGTH_SHORT).show();
            }
        });

        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
//                Toast.makeText(context, "Previous", Toast.LENGTH_SHORT).show();
            }
        });

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                java.util.Calendar clickedDayCalendar = eventDay.getCalendar();
                Toast.makeText(activity, ""+clickedDayCalendar, Toast.LENGTH_SHORT).show();

                //Get all dates selected
                List<java.util.Calendar> selectedDates = calendarView.getSelectedDates();
//                Toast.makeText(activity, ""+selectedDates, Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, ""+selectedDates.size(), Toast.LENGTH_SHORT).show();

                //Get first selected date
//                java.util.Calendar selectedDate = calendarView.getFirstSelectedDate();
//                Toast.makeText(activity, ""+selectedDate, Toast.LENGTH_SHORT).show();
            }
        });

        java.util.Calendar calendar2 = java.util.Calendar.getInstance();
        calendar.set(mYear_depart, mMonth_depart, mDay_depart);

        try {
            calendarView.setDate(calendar2);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

//        calendarView.setSelectedDates(getSelectedDays());

        List<EventDay> events = new ArrayList<>();

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_MONTH, 7);
//        events.add(new EventDay(cal, R.drawable.sample_four_icons));

        calendarView.setEvents(events);*/

        setCancelable(false);

        return rootView;
    }

//    private List<java.util.Calendar> getSelectedDays() {
//        List<java.util.Calendar> calendars = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            java.util.Calendar calendar = DateUtils.getCalendar();
//            calendar.add(java.util.Calendar.DAY_OF_MONTH, i);
//            calendars.add(calendar);
//        }
//
//        return calendars;
//    }
//
//    private List<java.util.Calendar> getDisabledDays() {
//        java.util.Calendar firstDisabled = DateUtils.getCalendar();
//        firstDisabled.add(java.util.Calendar.DAY_OF_MONTH, 2);
//
//        java.util.Calendar secondDisabled = DateUtils.getCalendar();
//        secondDisabled.add(java.util.Calendar.DAY_OF_MONTH, 1);
//
//        java.util.Calendar thirdDisabled = DateUtils.getCalendar();
//        thirdDisabled.add(java.util.Calendar.DAY_OF_MONTH, 18);
//
//        List<java.util.Calendar> calendars = new ArrayList<>();
//        calendars.add(firstDisabled);
//        calendars.add(secondDisabled);
//        calendars.add(thirdDisabled);
//        return calendars;
//    }

    private String getDistance(Location loc1, Location loc2){
        final String[][] tab = {{}};

        float distanceInMeters1 = loc1.distanceTo(loc2);
        tab[0] = String.valueOf(distanceInMeters1).split("\\.");
        String distance = tab[0][0];
        return distance;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    }
                });
                behavior.setSkipCollapsed(true);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return bottomSheetDialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        if (mapFragment != null)
//            getFragmentManager().beginTransaction().remove(mapFragment).commit();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }

    /*@Override
    public void onSelect(List<java.util.Calendar> calendars) {
        Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();
//        Stream.of(calendars).forEach(calendar -> Toast.makeText(context, calendar.getTime().toString(), Toast.LENGTH_SHORT).show());
    }*/

    /** Enregistrement d'une requête**/
    private class setBookRide extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_requete_book.php";
            String date_book = params[0];
            String place = params[1];
            String number_poeple = params[2];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                M.hideLoadingDialog();
                                if(etat.equals("1")){
                                    dialogSucess();
                                }else{
                                    Toast.makeText(activity, context.getResources().getString(R.string.an_error_occurred_while_sending_your_booking), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", M.getID(context));
                    params.put("lat1", String.valueOf(loc1.getLatitude()));
                    params.put("lng1", String.valueOf(loc1.getLongitude()));
                    params.put("lat2", String.valueOf(loc2.getLatitude()));
                    params.put("lng2", String.valueOf(loc2.getLongitude()));
                    params.put("cout", cout);
                    params.put("duree", duration);
                    params.put("distance", distance_init);
                    params.put("id_conducteur", id_driver);
                    params.put("id_payment", id_payment);
                    params.put("depart_name", depart_name);
                    params.put("destination_name", destination_name);
                    params.put("image", img_data);
                    params.put("image_name", "recu_trajet_image_"+System.currentTimeMillis()+".jpg");
                    params.put("nb_day", nb_day.getText().toString());
                    params.put("heure_depart", heure_depart.getText().toString());
                    params.put("date_book", date_book);
                    params.put("price", price);
                    params.put("place", place);
                    params.put("number_poeple", number_poeple);
                    params.put("statut_round", statut_round);
                    if(statut_round.equals("yes"))
                        params.put("heure_retour", heure_retour.getText().toString());
                    else
                        params.put("heure_retour", "");
//                    params.put("nb_date", String.valueOf(listCalendars.size()));
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //to add spacing between cards
            if (this != null) {

            }

        }

        @Override
        protected void onPreExecute() {

        }
    }

    //This method would confirm the otp
    private void dialogSucess() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_subscribe_success, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        TextView close = (TextView) confirmDialog.findViewById(R.id.close);
        TextView msg = (TextView) confirmDialog.findViewById(R.id.msg);

        msg.setText(context.getResources().getString(R.string.your_booking_as_been_sent_successfully));

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                BottomSheetFragmentBooking.dismissBottomSheetFragmentBookingDriver();
                FragmentHome.dismissBottomSheetFragmentBooking();
                dismiss();
            }
        });
        alertDialog.setCancelable(false);
    }
}
