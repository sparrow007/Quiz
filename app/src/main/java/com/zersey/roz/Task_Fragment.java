package com.zersey.roz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zersey.roz.Data.TransactionDbHelper;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Task_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Task_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Task_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private GroupModel mParam1;
    private String mParam2;

    public static Task_Adapter adapter;
    private List<Task_Model> Task_list;
    private RecyclerView Task_RecyclerView;
    private List<GroupModel> groupModels;
    private TransactionDbHelper mdbHelper;
    private OnFragmentInteractionListener mListener;

    public Task_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Task_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Task_Fragment newInstance(String param1, String param2) {
        Task_Fragment fragment = new Task_Fragment();
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
            mParam1 = (GroupModel) getArguments().getSerializable("group");
        }
        mdbHelper=TransactionDbHelper.getInstance(getContext());
        initTaskList();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment=inflater.inflate(R.layout.fragment_task, container, false);
        fragment=initRecyclerView(fragment);
        FloatingActionButton fab = (FloatingActionButton) fragment.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent=new Intent(getContext(),Task_Form_Activity.class);
                intent.putExtra("Group",mParam1);
                startActivityForResult(intent,1);
            }
        });
        return fragment;
    }


    public void initTaskList(){
        Task_list=new ArrayList<>();
        /*for (int i=0;i<10;i++){
            Task_list.add(new Task_Model("New Task","New Description","null",false));
        }*/
        Log.d( "initTaskList: ",""+mParam1.getGroupId());
        Task_list=mdbHelper.getTask(mParam1.getGroupId());
        Log.d( "initTaskList: ",""+Task_list.size());
    }

    public View initRecyclerView(View fragment){
        Task_RecyclerView=new RecyclerView(getContext());
        Task_RecyclerView=(RecyclerView)fragment.findViewById(R.id.Task_Recycler_View);
        Task_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new Task_Adapter(Task_list,mParam1);
        Task_RecyclerView.setAdapter(adapter);
        return fragment;
    }


    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==52){
            adapter.add(new Task_Model(data.getStringExtra("Title").toString(),data.getStringExtra("Des").toString(),data.getStringExtra("Date").toString(),false));
            //adapter.notifyDataSetChanged();
        }
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
