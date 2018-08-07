package android.zersey.expense_manger;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.zersey.expense_manger.Data.Transaction_contract;
import android.zersey.expense_manger.Data.Transactiondbhelper;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.BaseDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import android.zersey.expense_manger.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Graphs.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Graphs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Graphs extends Fragment implements OnChartValueSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PieChart pieChart;
    private List<BarEntry> BarChart_Entries ;
    private ArrayList<PieEntry> Item_list;
    private Transactiondbhelper mDbHelper ;
    private Calendar cal;
    private ArrayList<Integer> colors ;
    private int year_x,month_x,day_x,Current_day,Current_month,Current_year;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private BarChart mChart;
    private String mParam2;
    private String[] Months={"Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"};
    private OnFragmentInteractionListener mListener;

    public Graphs() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Graphs.
     */
    // TODO: Rename and change types and number of parameters
    public static Graphs newInstance(String param1, String param2) {
        Graphs fragment = new Graphs();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mDbHelper=new Transactiondbhelper(getContext());
        Item_list=new ArrayList<>();
        colors = new ArrayList<>();
        cal=Calendar.getInstance();

        //pieDataSet=new PieDataSet()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentLayout=inflater.inflate(R.layout.fragment_graphs, container, false);
        pieChart=(PieChart)fragmentLayout.findViewById(R.id.Expense_Chart);
        mChart = fragmentLayout.findViewById(R.id.Income_Chart);
        BarChart_Entries = new ArrayList<>();
        mChart.setOnChartValueSelectedListener(this);
        year_x=cal.get(Calendar.YEAR) ;
        month_x=cal.get(Calendar.MONTH)+1;
        day_x=cal.get(Calendar.DAY_OF_MONTH);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
       // xAxis.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        //rightAxis.removeAllLimitLines();
        rightAxis.setDrawGridLines(false);
//        rightAxis.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
       rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setAxisMaximum(0f);
        // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        //setData(12, 50);




        SQLiteDatabase db1 = mDbHelper.getReadableDatabase();
        //final  ArrayList<String> labellist=new ArrayList<>();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Transaction_contract.Transaction_Entry._ID,
                Transaction_contract.Transaction_Entry.Column_Title,
                Transaction_contract.Transaction_Entry.Column_Category,
                Transaction_contract.Transaction_Entry.Column_Amount,
                Transaction_contract.Transaction_Entry.Column_Date_Created,
                Transaction_contract.Transaction_Entry.Column_Date_Updated,
                Transaction_contract.Transaction_Entry.Column_Image,
                Transaction_contract.Transaction_Entry.Column_Notes};

        //Cursor cursor=db1.rawQuery("SELECT * FROM "+recipe_entry.Table_name,null);
        // Perform a query on the pets table
        Cursor cursor = db1.query(
                Transaction_contract.Transaction_Entry.Table_name,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order


        try {



            cursor.moveToFirst();
            // Iterate through all the returned rows in the cursor
            while (!cursor.isAfterLast()) {

                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(cursor.getColumnIndex(Transaction_contract.Transaction_Entry._id));
                String title = cursor.getString(cursor.getColumnIndex(Transaction_contract.Transaction_Entry.Column_Title));
                //Log.d( "current label ",currentName);
                String category = cursor.getString(cursor.getColumnIndex(Transaction_contract.Transaction_Entry.Column_Category));
                String amount = cursor.getString(cursor.getColumnIndex(Transaction_contract.Transaction_Entry.Column_Amount));
                String dateCreated = cursor.getString(cursor.getColumnIndex(Transaction_contract.Transaction_Entry.Column_Date_Created));
                //Character temp=dateCreated.substring(0,1);
                //if(temp.){}
                int Income_date= Integer.parseInt(extractNumber(dateCreated));
                    Current_day=Income_date;
                Log.d("Income Date",Income_date+"");
                if(dateCreated.contains(Months[1])){ Income_date=Income_date+31;
                    Current_month=2;
                }else if(dateCreated.contains(Months[2])){
                    Income_date=Income_date+60;
                    Current_month=3;
                }else if(dateCreated.contains(Months[3])){ Income_date=Income_date+91;
                    Current_month=4;
                }else if(dateCreated.contains(Months[4])){ Income_date=Income_date+121;
                    Current_month=5;
                }else if(dateCreated.contains(Months[5])){ Income_date=Income_date+152;
                    Current_month=6;
                }else if(dateCreated.contains(Months[6])){ Income_date=Income_date+182;
                    Current_month=7;
                }else if(dateCreated.contains(Months[7])){ Income_date=Income_date+213;
                    Current_month=8;
                }else if(dateCreated.contains(Months[8])){ Income_date=Income_date+243;
                    Current_month=9;
                }else if(dateCreated.contains(Months[9])){ Income_date=Income_date+274;
                    Current_month=10;
                }else if(dateCreated.contains(Months[10])){ Income_date=Income_date+304;
                    Current_month=11;
                }else if(dateCreated.contains(Months[11])){ Income_date=Income_date+335;
                    Current_month=2;}else{
                    Current_month=1;
                }

                Custom_items csitem=new Custom_items(category,title,amount,dateCreated);
                //Item_list.add(new PieEntry());
                amount=amount.replace("Rs ","");
               Float number = Float.parseFloat(amount);
               if("Entertainment".equals(category)){
                  /*boolean flag=false;
                   for(int i=0;i<Item_list.size();i++)
                   { if("Entertainment".equals(Item_list.get(i).getLabel())){
                       Item_list.add(i,new PieEntry(Item_list.get(i).getValue()+number,"Entertainment"));
                       flag=true;
                   }
                   }
                   if(flag==false){*/
                   Item_list.add(new PieEntry(number, "Entertainment"));
                   colors.add(getResources().getColor(R.color.Pink));

                  /* PieDataSet pieDataSet = new PieDataSet(Item_list, "Entertainment");
                   pieDataSet.setColor(getResources().getColor(R.color.Green));
                   PieData data = new PieData(pieDataSet);
                  // data.setValueFormatter(new valueFormat());
                   pieChart.setData(data);
                   pieChart.invalidate();*/
               }else if("Clothing".equals(category)) {
                   /*boolean flag=false;
                   for(int i=0;i<Item_list.size();i++)
                   { if("Clothing".equals(Item_list.get(i).getLabel())){
                       Item_list.add(i,new PieEntry(Item_list.get(i).getValue()+number,"Clothing"));
                       flag=true;
                   }
                   }
                   if(flag==false) {*/
                       Item_list.add(new PieEntry(number, "Clothing"));
                       colors.add(getResources().getColor(R.color.Blue));

                   /* PieDataSet pieDataSet = new PieDataSet(Item_list, "Clothing");
                   pieDataSet.setColor(getResources().getColor(R.color.Yellow));
                   PieData data = new PieData(pieDataSet);
                   pieChart.setData(data);
                   pieChart.invalidate();*/
               }else if("Health".equals(category)) {
                   /*boolean flag=false;
                   for(int i=0;i<Item_list.size();i++)
                   { if("Health".equals(Item_list.get(i).getLabel())){
                       Item_list.add(i,new PieEntry(Item_list.get(i).getValue()+number,"Health"));
                       flag=true;
                   }
                   }
                   if(flag==false) {*/
                       Item_list.add(new PieEntry(number, "Health"));
                       colors.add(getResources().getColor(R.color.Yellow));

                   /*PieDataSet pieDataSet = new PieDataSet(Item_list, "Health");
                   pieDataSet.setColor(getResources().getColor(R.color.Red));
                   PieData data = new PieData(pieDataSet);
                   data.setValueTextColor(getResources().getColor(R.color.Red));
                   pieChart.setData(data);
                   pieChart.invalidate();*/
               }else if("Food".equals(category)) {
                  /* boolean flag=false;
                   for(int i=0;i<Item_list.size();i++)
                   { if("Food".equals(Item_list.get(i).getLabel())){
                       Item_list.add(i,new PieEntry(Item_list.get(i).getValue()+number,"Food"));
                       flag=true;
                   }
                   }
                   if(flag==false) {*/
                       Item_list.add(new PieEntry(number, "Food"));
                       colors.add(getResources().getColor(R.color.Orange));

                   /*PieDataSet pieDataSet = new PieDataSet(Item_list, "Food");
                   pieDataSet.setColor(getResources().getColor(R.color.Blue));
                   PieData data = new PieData(pieDataSet);
                   pieChart.setData(data);
                   pieChart.invalidate();*/
               }else if("Fuel".equals(category)) {
                   /*boolean flag=false;
                   for(int i=0;i<Item_list.size();i++)
                   { if("Fuel".equals(Item_list.get(i).getLabel())){
                       Item_list.add(i,new PieEntry(Item_list.get(i).getValue()+number,"Fuel"));
                       flag=true;
                   }
                   }
                   if(flag==false) {*/
                       Item_list.add(new PieEntry(number, "Fuel"));
                       colors.add(getResources().getColor(R.color.Green));
                   //}
                   /*PieDataSet pieDataSet = new PieDataSet(Item_list, "Fuel");
                   pieDataSet.setColors(getResources().getColor(R.color.Orange));
                   PieData data = new PieData(pieDataSet);
                   pieChart.setData(data);
                   pieChart.invalidate();*/
               }else if("Income".equals(category)) {

                   if(Current_month==month_x){
                       if(Current_day==day_x){
                           BarChart_Entries.add(new BarEntry(Income_date,number));
                       }else if(Current_day==(day_x-1)){
                           BarChart_Entries.add(new BarEntry(Income_date,number));
                       }else if(Current_day==(day_x-2)){
                           BarChart_Entries.add(new BarEntry(Income_date,number));
                       }else if(Current_day==(day_x-3)){
                           BarChart_Entries.add(new BarEntry(Income_date,number));
                       }else if(Current_day==(day_x-4)){
                           BarChart_Entries.add(new BarEntry(Income_date,number));
                       }else if(Current_day==(day_x-5)){
                           BarChart_Entries.add(new BarEntry(Income_date,number));
                       }else if(Current_day==(day_x-6)){
                           BarChart_Entries.add(new BarEntry(Income_date,number));
                       }
                   }

                   //PieEntry pieEntry=new PieEntry(number, "Salary");
                   //Item_list.add(new PieEntry(number, title));
                   //colors.add(getResources().getColor(R.color.Black));
                   /*PieDataSet pieDataSet = new PieDataSet(Item_list, "Salary");
                   pieDataSet.setColor(getResources().getColor(R.color.Pink));
                   PieData data = new PieData(pieDataSet);
                   pieChart.setData(data);
                   pieChart.invalidate();*/
               }

                // Display the values from each column of the current row in the cursor in the TextView
                /*displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        //currentBreed + " - " +
                        currentGender + " - " +
                        currentWeight));
                */
                cursor.moveToNext();

            }

            //BarChart_Entries.add(new BarEntry(80,50000));
            /*BarChart_Entries.add(new BarEntry(0f, 30f));
            BarChart_Entries.add(new BarEntry(1f, 80f));
            BarChart_Entries.add(new BarEntry(2f, 60f));
            BarChart_Entries.add(new BarEntry(3f, 50f));
            // gap of 2f
            BarChart_Entries.add(new BarEntry(5f, 70f));
            BarChart_Entries.add(new BarEntry(6f, 60f));*/
            BarDataSet set = new BarDataSet(BarChart_Entries, "year of 2018");
            BarData data = new BarData(set);
           // data.setBarWidth(0.9f); // set custom bar width
            mChart.setData(data);
            mChart.setFitBars(true); // make the x-axis fit exactly all bars
            mChart.invalidate(); // refresh


           PieDataSet pieDataSet = new PieDataSet(Item_list,"");
            pieDataSet.setLabel("Expenses");
            pieDataSet.setSliceSpace(3f);



            //colors.add(getResources().getColor(R.color.Red));



            pieDataSet.setColors(colors);
            Legend l1 = pieChart.getLegend();
            l1.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l1.setOrientation(Legend.LegendOrientation.VERTICAL);
            l1.setDrawInside(false);
            l1.setXEntrySpace(7f);
            l1.setYEntrySpace(0f);
            l1.setYOffset(0f);

            // entry label styling
            pieChart.setEntryLabelColor(Color.WHITE);
            //pieChart.setEntryLabelTypeface(mTfRegular);
            pieChart.setEntryLabelTextSize(12f);

            PieData data1 = new PieData(pieDataSet);
            pieChart.setData(data1);
            pieChart.setTransparentCircleColor(Color.WHITE);
            pieChart.setTransparentCircleAlpha(110);
            pieChart.invalidate();
            pieChart.animateXY(2000, 1400);
            pieChart.invalidate();

            final Handler handler23 = new Handler();
            handler23.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    mChart.animateXY(2000, 1400);
                    pieChart.animateXY(2000, 1400);
                }
            }, 2000);



        }catch (Exception e){e.printStackTrace();}
        finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }



        return fragmentLayout;
    }


    private void setData(int count, float range) {

        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) start; i < start + count + 1; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);

            if (Math.random() * 100 < 25) {
                yVals1.add(new BarEntry(i, val, getResources().getDrawable(R.drawable.star)));
            } else {
                yVals1.add(new BarEntry(i, val));
            }
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");

            set1.setDrawIcons(false);

//            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            /*int startColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor = ContextCompat.getColor(this, android.R.color.holo_blue_bright);
            set1.setGradientColor(startColor, endColor);*/

            int startColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);

            List<GradientColor> gradientColors = new ArrayList<>();
            gradientColors.add(new GradientColor(startColor1, endColor1));
            gradientColors.add(new GradientColor(startColor2, endColor2));
            gradientColors.add(new GradientColor(startColor3, endColor3));
            gradientColors.add(new GradientColor(startColor4, endColor4));
            gradientColors.add(new GradientColor(startColor5, endColor5));



            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//            data.setValueTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //mChart.animateXY(2000, 1400);
        //pieChart.animateXY(2000, 1400);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        //mChart.animateXY(2000, 1400);
        //pieChart.animateXY(2000, 1400);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public static String extractNumber(final String str) {

        if(str == null || str.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for(char c : str.toCharArray()){
            if(Character.isDigit(c)){
                sb.append(c);
                found = true;
            } else if(found){
                // If we already found a digit before and this char is not a digit, stop looping
                break;
            }
        }

        return sb.toString();
    }


}
