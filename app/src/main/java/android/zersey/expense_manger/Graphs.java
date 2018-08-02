package android.zersey.expense_manger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.zersey.expense_manger.Data.Transaction_contract;
import android.zersey.expense_manger.Data.Transactiondbhelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Graphs.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Graphs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Graphs extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PieChart pieChart;
    private ArrayList<PieEntry> Item_list;
    private Transactiondbhelper mDbHelper ;
    private ArrayList<Integer> colors ;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        //pieDataSet=new PieDataSet()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentLayout=inflater.inflate(R.layout.fragment_graphs, container, false);
        pieChart=(PieChart)fragmentLayout.findViewById(R.id.Expense_Chart);



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
               }else if("Salary".equals(category)) {
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

           PieDataSet pieDataSet = new PieDataSet(Item_list,"");
            pieDataSet.setLabel("Expenses");
            pieDataSet.setSliceSpace(3f);



            //colors.add(getResources().getColor(R.color.Red));



            pieDataSet.setColors(colors);
            Legend l = pieChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            // entry label styling
            pieChart.setEntryLabelColor(Color.WHITE);
            //pieChart.setEntryLabelTypeface(mTfRegular);
            pieChart.setEntryLabelTextSize(12f);

            PieData data = new PieData(pieDataSet);
            pieChart.setData(data);
            pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            pieChart.setTransparentCircleColor(Color.WHITE);
            pieChart.setTransparentCircleAlpha(110);
            pieChart.invalidate();





        }catch (Exception e){e.printStackTrace();}
        finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }



        return fragmentLayout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
}
