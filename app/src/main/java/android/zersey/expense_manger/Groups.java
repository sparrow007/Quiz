package android.zersey.expense_manger;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Groups.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Groups#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Groups extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView First_More,Second_More;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView First_RecyclerView,Second_RecyclerView;
    private List<String> First_List,Second_List;
    private OnFragmentInteractionListener mListener;

    public Groups() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Groups.
     */
    // TODO: Rename and change types and number of parameters
    public static Groups newInstance(String param1, String param2) {
        Groups fragment = new Groups();
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
        First_List=new ArrayList<>();
        Second_List=new ArrayList<>();
        First_RecyclerView=new RecyclerView(getContext());
        Second_RecyclerView=new RecyclerView(getContext());
        initList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentLayout=inflater.inflate(R.layout.fragment_groups, container, false);
        First_More=(TextView) fragmentLayout.findViewById(R.id.First_More);
        Second_More=(TextView) fragmentLayout.findViewById(R.id.Second_More);
        First_More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),More_Activity.class);
                intent.putStringArrayListExtra("More",(ArrayList<String>) First_List);
                Log.d( "onClick: ",First_List.size()+"");
                startActivity(intent);
            }
        });
        Second_More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),More_Activity.class);
                intent.putStringArrayListExtra("More",(ArrayList<String>) Second_List);
                Log.d( "onClick: ",First_List.size()+"");
                startActivity(intent);
            }
        });
       fragmentLayout=initRecyclerView(fragmentLayout);
        return fragmentLayout;
    }


    private void initList(){
        for(int i=0;i<5;i++){
            First_List.add("Title");
            Second_List.add("WAO 1");
        }
    }

    private View initRecyclerView(View fragmentLayout){
        First_RecyclerView = (RecyclerView) fragmentLayout.findViewById(R.id.First_Slider);
        First_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        //new CardSnapHelper().attachToRecyclerView(First_RecyclerView);
        First_RecyclerView.setAdapter(new First_Slider_Adapter(getContext(),First_List));
        //First_RecyclerView.smoothScrollToPosition(0);

        Second_RecyclerView = (RecyclerView) fragmentLayout.findViewById(R.id.Second_Slider);
        Second_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        //new CardSnapHelper().attachToRecyclerView(Second_RecyclerView);
        Second_RecyclerView.setAdapter(new Second_Slider_Adapter(getContext(),Second_List));
        //Second_RecyclerView.smoothScrollToPosition(0);

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
