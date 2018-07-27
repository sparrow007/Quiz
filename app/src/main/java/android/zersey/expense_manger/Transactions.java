package android.zersey.expense_manger;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.zersey.expense_manger.Data.Transaction_contract;
import android.zersey.expense_manger.Data.Transaction_contract.Transaction_Entry;
import android.zersey.expense_manger.Data.Transactiondbhelper;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Transactions.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Transactions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Transactions extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Custom_items> Item_list;
    private Transactiondbhelper mDbHelper ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Transactions() {

    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Transactions.
     */
    // TODO: Rename and change types and number of parameters
    public static Transactions newInstance(String param1, String param2) {
        Transactions fragment = new Transactions();
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
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentLayout=inflater.inflate(R.layout.fragment_transactions, container, false);
        ListView listView = (ListView)fragmentLayout.findViewById(R.id.Fragment_listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                SQLiteDatabase db1 = mDbHelper.getReadableDatabase();

                String[] projection = {
                        Transaction_contract.Transaction_Entry._ID,
                        Transaction_contract.Transaction_Entry.Column_Title,
                        Transaction_contract.Transaction_Entry.Column_Category,
                        Transaction_contract.Transaction_Entry.Column_Amount,
                        Transaction_contract.Transaction_Entry.Column_Date_Created,
                        Transaction_contract.Transaction_Entry.Column_Date_Updated,
                        Transaction_contract.Transaction_Entry.Column_Image,
                        Transaction_contract.Transaction_Entry.Column_Notes};

                Cursor cursor = db1.query(
                        Transaction_contract.Transaction_Entry.Table_name,   // The table to query
                        projection,            // The columns to return
                        null,                  // The columns for the WHERE clause
                        null,                  // The values for the WHERE clause
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // The sort order
                cursor.moveToPosition(position);

                        int currentID = cursor.getInt(cursor.getColumnIndex(Transaction_contract.Transaction_Entry._id));
                        String title = cursor.getString(cursor.getColumnIndex(Transaction_contract.Transaction_Entry.Column_Title));
                        //Log.d( "current label ",currentName);
                        String category = cursor.getString(cursor.getColumnIndex(Transaction_contract.Transaction_Entry.Column_Category));
                        String amount = cursor.getString(cursor.getColumnIndex(Transaction_contract.Transaction_Entry.Column_Amount));
                        String dateCreated = cursor.getString(cursor.getColumnIndex(Transaction_contract.Transaction_Entry.Column_Date_Created));
                        Custom_items csitem=new Custom_items(category,title,amount,dateCreated);
                Intent i=new Intent(getContext(),MainActivity.class);
                i.putExtra("_ID",currentID);
                i.putExtra("CardClicked","Yes");
                i.putExtra("Title",title);
                i.putExtra("Category",category);
                i.putExtra("Amount",amount);
                i.putExtra("DateCreated",dateCreated);
                startActivity(i);

                        }
        });



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
                Item_list.add(csitem);
                // Display the values from each column of the current row in the cursor in the TextView
                /*displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        //currentBreed + " - " +
                        currentGender + " - " +
                        currentWeight));
                */
                cursor.moveToNext();

            }





            Custom_listView_Adapter adapter=new Custom_listView_Adapter(getContext(),R.layout.transaction_list_layout,Item_list);
            listView.setAdapter(adapter);
        }catch (Exception e){e.printStackTrace();}
        finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }



        //Item_list=bundle.getParcelableArrayList("ARRAYLIST");}










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
