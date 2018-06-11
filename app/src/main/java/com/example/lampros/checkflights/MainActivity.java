package com.example.lampros.checkflights;


import android.app.ProgressDialog;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.Button;
import android.widget.ImageView;

import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.timessquare.CalendarPickerView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);





    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class FragmentFirst extends Fragment implements TextWatcher {

        private ArrayList<String> citiesFrom = new ArrayList<String>();
        private ArrayAdapter<String> adapterFrom;
        private ArrayList<String> citiesTo = new ArrayList<String>();
        private ArrayAdapter<String> adapterTo;
        ProgressDialog pdFrom;

        private CalendarPickerView calendar;

        private AutoCompleteTextView from;
        private AutoCompleteTextView to;

        private TextView adult_economyClass;
        private TextView sort;
        private TextView filter;

        private ImageView swapBtn;

        int whereIs = 0;

        String fromText="";
        String toText="";


        public static FragmentFirst newInstance(int sectionNumber) {
            FragmentFirst fragment = new FragmentFirst();
            return fragment;
        }

        public FragmentFirst() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.search, container, false);


            Calendar nextYear = Calendar.getInstance();
            nextYear.add(Calendar.YEAR, 1);

            calendar = (CalendarPickerView) rootView.findViewById(R.id.calendar_view);

            swapBtn = (ImageView) rootView.findViewById(R.id.swapID);

            adult_economyClass = (TextView) rootView.findViewById(R.id.Adult_EconomyClacssID);
            sort = (TextView) rootView.findViewById(R.id.SortID);
            filter = (TextView) rootView.findViewById(R.id.FilterID);

            from = (AutoCompleteTextView) rootView.findViewById(R.id.fromID);
            from.addTextChangedListener(this);
            to = (AutoCompleteTextView) rootView.findViewById(R.id.toID) ;
            to.addTextChangedListener(this);

            Date today = new Date();
            calendar.init(today, nextYear.getTime())
                    .withSelectedDate(today)
                    .inMode(CalendarPickerView.SelectionMode.RANGE);
            Log.i("Date", calendar.getSelectedDate().toString());


            from.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View arg0, MotionEvent arg1)
                {
                    whereIs = 1;
                    return false;
                }
            });

            to.setOnTouchListener(new View.OnTouchListener()
            {
                public boolean onTouch(View arg0, MotionEvent arg1)
                {
                    whereIs = 2;
                    return false;
                }
            });

            swapBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String fromTxt="";
                    String toTxt="";

                   fromTxt = from.getText().toString();
                   toTxt = to.getText().toString();

                   from.setText(toTxt);
                   to.setText(fromTxt);
                }
            });

            adult_economyClass.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.adult_economyclass, (ViewGroup) rootView.findViewById(R.id.adultEconomyClassID));
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                            .setView(layout);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                    SeekBar seekBarAdult = (SeekBar) layout.findViewById(R.id.seekBarAdultID);
                    SeekBar seekBarChild = (SeekBar) layout.findViewById(R.id.seekBarChildID);
                    SeekBar seekBarInfants = (SeekBar) layout.findViewById(R.id.seekBarInfantsID);

                    Button doneBtn  =   (Button) layout.findViewById(R.id.DoneID);

                    final TextView adultNumber = (TextView) layout.findViewById(R.id.adultNumberID);
                    final TextView childNumber = (TextView) layout.findViewById(R.id.childNumberID);
                    final TextView infantNumber = (TextView) layout.findViewById(R.id.infantsNumbersID);



                    seekBarAdult.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                        int progress = 0;

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                            progress = progresValue;
                            adultNumber.setText(String.valueOf(progress));

                        }
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }
                        @Override
                         public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });

                    seekBarChild.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                        int progressChild = 0;

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                            progressChild = progresValue;
                            childNumber.setText(String.valueOf(progressChild));

                        }
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });

                    seekBarInfants.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                        int progressInfants = 0;

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                            progressInfants = progresValue;
                            infantNumber.setText(String.valueOf(progressInfants));

                        }
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });







                    doneBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                }
            });





            return rootView;
        }



        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id){
                case R.id.action_settings:
                    return true;
                case R.id.action_next:
                    ArrayList<Date> selectedDates = (ArrayList<Date>)calendar
                            .getSelectedDates();
                    Toast.makeText(getContext(), selectedDates.toString(),
                            Toast.LENGTH_LONG).show();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }





        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Log.i("lam3" , "lam3");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Log.i("lam1" , "lam1");
            Log.i("TEXT CHANGED TO: " , s.toString());
            if(!TextUtils.isEmpty(s.toString())) {
                Log.i("TEXT CHANGED TO: 2" , s.toString());
                if(s.toString().length()>=3&& !s.toString().contains("[")){
                    //getNewText(s.toString());

                    pdFrom = new ProgressDialog(getContext());
                    pdFrom.setMessage("Please wait...");
                    pdFrom.show();

                    String url = getResources().getString(R.string.hello)+"&term="+s.toString();
                    url = url.replaceAll(" ", "%20");
                    if(url.contains("[")) {
                        url = url.replaceAll(" \\[ ", "");
                        url = url.replaceAll(" \\] ", "");
                    }
                    Log.i("Url", url);
                    Ion.with(this)
                            .load(url)
                            .setLogging("Parsvid", Log.DEBUG)
                            .asJsonArray()
                            .setCallback(new FutureCallback<JsonArray>() {

                                @Override
                                public void onCompleted(Exception e, JsonArray result) {
                                    citiesFrom.clear();
                                    for (int i = 0; i < result.size(); i++) {
                                        //result.get(i).getAsJsonObject();

                                        Log.i("obj", result.get(i).getAsJsonObject().toString());
                                        String splitObj[] = result.get(i).getAsJsonObject().toString().split("\"");
                                        String value = splitObj[3];
                                        String label = splitObj[7];
                                        Log.i("values", value + "," + label);
                                        String valueNew = value + "," + label;
                                        valueNew = valueNew.replaceAll("\\\\", "");

                                        citiesFrom.add(valueNew);

                                    }
                                    pdFrom.cancel();
//                                    for(int i=0; i<citiesFrom.size(); i++)
//                                        Log.i("cities",citiesFrom.get(i).toString());
                                    if(whereIs==1){
                                        Log.i("Lams","mpike1");
                                        adapterFrom= new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,citiesFrom);
                                        from.setAdapter(adapterFrom);
                                        from.showDropDown();
                                    }else if(whereIs ==2){
                                        Log.i("Lams","mpike2");
                                        adapterTo= new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,citiesFrom);
                                        to.setAdapter(adapterTo);
                                        to.showDropDown();
                                    }

//                                    My_arr_adapter.notifyDataSetChanged();

                                }

                            });

                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //Toast.makeText(getContext(), s.toString(), Toast.LENGTH_SHORT).show();
            if(whereIs==1){
                fromText = s.toString();
            }else if(whereIs ==2){
                toText = s.toString();
            }
        }


    }

    public static class FragmentSecond extends Fragment {





        public static FragmentSecond newInstance(int sectionNumber) {
            FragmentSecond fragment = new FragmentSecond();
            return fragment;
        }

        public FragmentSecond() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.history, container, false);




            return rootView;
        }

    }







    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    return FragmentFirst.newInstance(0);
                case 1:
                    return FragmentSecond.newInstance(1);
                default:
                    //assume you only have 3
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Search";
                case 1:
                    return "History";

            }
            return null;
        }
    }
}
