package android.zersey.expense_manger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.zersey.expense_manger.Data.TransactionDbContract;
import android.zersey.expense_manger.Data.TransactionDbHelper;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.data.BarDataSet;

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
public class Graphs extends Fragment implements OnChartValueSelectedListener,OnChartGestureListener {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private LineChart mlineChart;
	private PieChart pieChart;
	private ArrayList<Entry> Line_Entries;
	private List<BarEntry> BarChart_Entries ;
	private ArrayList<PieEntry> Item_list;
	private List<IncomeModel> Db_list;
	private ArrayList<Expense_Info> Expense_List;
	private ArrayList<Income_Info> Income_List;
	private TransactionDbHelper mDbHelper ;
	private Calendar cal;
	private ArrayList<Integer> colors ;
	private int year_x,month_x,day_x,Current_day,Current_month,Current_year;
	// TODO: Rename and change types of parameters
	private String mParam1;
	private BarChart mChart;
	private String mParam2;
	private int Income_date, Total_Expense_PM = 0, Total_Income_PM = 0;
	private Float Entertainment_Expense = 0f, Clothing_Expense = 0f, Food_Expense = 0f, Fuel_Expense = 0f, Health_expense = 0f;
	private String[] Months={"Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"};
	private OnFragmentInteractionListener mListener;

	public Graphs() {
		// Required empty public constructor
	}


	// TODO: Rename and change types and number of parameters
	public static Graphs newInstance() {
		Graphs fragment = new Graphs();
		Bundle args = new Bundle();
		//args.putString(ARG_PARAM1, param1);
		//args.putString(ARG_PARAM2, param2);
		fragment.onCreate(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		mDbHelper=new TransactionDbHelper(getContext());
		Item_list=new ArrayList<>();
		colors = new ArrayList<>();
		cal=Calendar.getInstance();
		year_x=cal.get(Calendar.YEAR) ;
		month_x=cal.get(Calendar.MONTH)+1;
		day_x=cal.get(Calendar.DAY_OF_MONTH);
		Db_list = new ArrayList<>();
		Db_list.addAll(mDbHelper.getAllEntries());
		Line_Entries=new ArrayList<>();
		Expense_List=new ArrayList<>();
		Income_List=new ArrayList<>();
		BarChart_Entries = new ArrayList<>();
		//saving_amount=new ArrayList<>();
		//pieDataSet=new PieDataSet()
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View fragmentLayout=inflater.inflate(R.layout.fragment_graphs, container, false);
		mlineChart=(LineChart)fragmentLayout.findViewById(R.id.Savings_Chart);
		pieChart=(PieChart)fragmentLayout.findViewById(R.id.Expense_Chart);
		mChart = (BarChart) fragmentLayout.findViewById(R.id.Income_Chart);
		initBarChart();
		initLineChart();
		initPieChart();








		Line_Entries.add(new Entry(0, 0f));

		//cursor.moveToFirst();
		// Iterate through all the returned rows in the cursor
		Log.d("onCreateView: ",Db_list.size()+"");
		for (int i = 0; i < Db_list.size(); i++) {

			// Use that index to extract the String or Int value of the word
			// at the current row the cursor is on.
			//int currentID = Db_list.get(i).getId();
			String title = Db_list.get(i).getTitle();
			String category = Db_list.get(i).getCatId();
			String Type = Db_list.get(i).getType();
			String amount = Db_list.get(i).getTotalAmount();
			String dateCreated = Db_list.get(i).getPaidAtDate();


			//Custom_items csitem=new Custom_items(category,title,amount,dateCreated);
			//Item_list.add(new PieEntry());
			amount = amount.replace("Rs ", "");
			Float number = Float.parseFloat(amount);
			if ("Expense".equals(Type)) {

				Entertainment_Expense = Entertainment_Expense + number;
				Log.d("Savings", dateCreated + " = " + Months[month_x - 1]);

				if (Current_month == month_x) {
					Total_Expense_PM = Total_Expense_PM + Integer.parseInt(amount);
				}
				Expense_Info expense_info = new Expense_Info(amount, dateCreated, title);
				Expense_List.add(expense_info);

			} else if ("Clothing".equals(category)) {

				Clothing_Expense = Clothing_Expense + number;
				if (Current_month == month_x) {
					Total_Expense_PM = Total_Expense_PM + Integer.parseInt(amount);
				}
				Expense_Info expense_info = new Expense_Info(amount, dateCreated, title);
				Expense_List.add(expense_info);

			} else if ("Health".equals(category)) {

				Health_expense = Health_expense + number;
				if (Current_month == month_x) {
					Total_Expense_PM = Total_Expense_PM + Integer.parseInt(amount);
				}
				Expense_Info expense_info = new Expense_Info(amount, dateCreated, title);
				Expense_List.add(expense_info);

			} else if ("Food".equals(category)) {

				Food_Expense = Food_Expense + number;
				if (Current_month == month_x) {
					Total_Expense_PM = Total_Expense_PM + Integer.parseInt(amount);
				}
				Expense_Info expense_info = new Expense_Info(amount, dateCreated, title);
				Expense_List.add(expense_info);

			} else if ("Fuel".equals(category)) {

				Fuel_Expense = Fuel_Expense + number;
				if (Current_month == month_x) {
					Total_Expense_PM = Total_Expense_PM + Integer.parseInt(amount);
				}
				Expense_Info expense_info = new Expense_Info(amount, dateCreated, title);
				Expense_List.add(expense_info);

			} else if (TextUtils.equals(Type,"Income")) {
				Income_Info Info = new Income_Info(amount, dateCreated, title);
				Income_List.add(Info);
				String[] temp = dateCreated.split("-");
				Current_year=Integer.parseInt(temp[0]);
				Income_date = Integer.parseInt(temp[2]);
				Current_day = Income_date;
				Current_month = Integer.parseInt(temp[1]);
				if (Current_month == month_x) {
					try{Total_Income_PM = Total_Income_PM + Integer.parseInt(amount);
					}catch (Exception e){e.printStackTrace();}
				}
				Log.d("Income Date", dateCreated + "");
				if (Current_month == 2) {
					Income_date = Income_date + 31;
				} else if (Current_month == 3) {
					Income_date = Income_date + 60;
				} else if (Current_month == 4) {
					Income_date = Income_date + 91;
				} else if (Current_month == 5) {
					Income_date = Income_date + 121;
				} else if (Current_month == 6) {
					Income_date = Income_date + 152;
				} else if (Current_month == 7) {
					Income_date = Income_date + 182;
				} else if (Current_month == 8) {
					Income_date = Income_date + 213;
				} else if (Current_month == 9) {
					Income_date = Income_date + 243;
				} else if (Current_month == 10) {
					Income_date = Income_date + 274;
				} else if (Current_month == 11) {
					Income_date = Income_date + 304;
				} else if (Current_month == 12) {
					Income_date = Income_date + 335;
				}
				Log.d("onCreateView: ",Current_day+"");

                GettingValueForBarEntryList(number);

			}


		}
		Float Total_Savings_PM = (float) (Total_Income_PM - Total_Expense_PM);
		Log.d("Savings", Total_Savings_PM + "");
		Log.d("onCreateView: ",  Income_List.size()+"  "+BarChart_Entries.size());
		//mChart.invalidate();

          GettingValueForLineEntryList();
          GettingValueForPieEntryList();



		CheckingLineDataAvailability(Total_Savings_PM);
		CheckingBarDataAvailability();
		CheckingPieDataAvailability();

	/*	final Handler handler23 = new Handler();
		handler23.postDelayed(new Runnable() {
			@Override public void run() {
				//Do something after 100ms

				mChart.invalidate();
				mlineChart.invalidate();
				pieChart.invalidate();
				Log.d("onCreateView: ",  Income_List.size()+"  "+BarChart_Entries.size()+"  ");
				mChart.animateXY(1400, 1400);
				pieChart.animateXY(1400, 1400);
				mlineChart.animateXY(1400, 1400);
			}
		}, 2000);*/


		return fragmentLayout;
	}

	Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {
            GettingValueForLineEntryList();
        }
    });

	public void CheckingLineDataAvailability(float Total_Savings_PM){
		if(Line_Entries.size()>0){
			LimitLine ll1 = new LimitLine(Total_Savings_PM+100, "Upper Limit");
			ll1.setLineWidth(4f);
			ll1.enableDashedLine(10f, 10f, 0f);
			ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
			ll1.setTextSize(10f);
			//ll1.setTypeface(tf);

			LimitLine ll2 = new LimitLine(-10000, "Lower Limit");
			ll2.setLineWidth(4f);
			ll2.enableDashedLine(10f, 10f, 0f);
			ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
			ll2.setTextSize(10f);
			//ll2.setTypeface(tf);

			YAxis leftAxis1 = mlineChart.getAxisLeft();
			leftAxis1.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
			leftAxis1.addLimitLine(ll1);
			leftAxis1.addLimitLine(ll2);
			//leftAxis1.setAxisMaximum(200f);
			//leftAxis1.setAxisMinimum(-20000f);
			//leftAxis.setYOffset(20f);
			leftAxis1.enableGridDashedLine(10f, 10f, 0f);
			leftAxis1.setDrawZeroLine(false);
			leftAxis1.setDrawLimitLinesBehindData(true);
			mlineChart.getAxisRight().setEnabled(false);
			LineDataSet set1;
			set1 = new LineDataSet(Line_Entries, "Total Savings of current month");
			set1.setDrawIcons(false);

			// set the line to be drawn like this "- - - - - -"
			set1.enableDashedLine(10f, 5f, 0f);
			set1.enableDashedHighlightLine(10f, 5f, 0f);
			set1.setColor(Color.BLACK);
			set1.setCircleColor(Color.BLACK);
			set1.setLineWidth(1f);
			set1.setCircleRadius(3f);
			set1.setDrawCircleHole(false);
			set1.setValueTextSize(9f);
			set1.setDrawFilled(true);
			set1.setFormLineWidth(1f);
			set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
			set1.setFormSize(15.f);
			set1.setDrawFilled(true);
			//set1.setFillDrawable(gradientDrawable);
			LineData data2 = new LineData(set1);

			mlineChart.setData(data2);
			mlineChart.animateXY(3000, 3000);
			mlineChart.invalidate();
		}else{Toast.makeText(getContext(),"No Data to be displayed in LineChart",Toast.LENGTH_LONG).show();}}

    public void CheckingBarDataAvailability(){
        Log.d("CheckingAvailability",BarChart_Entries.size()+"");

		if(BarChart_Entries.size()>0) {
			BarDataSet set = new BarDataSet(BarChart_Entries, "year of 2018");
			BarData data = new BarData(set);
			// data.setBarWidth(0.9f); // set custom bar width
			mChart.setData(data);
			mChart.setFitBars(true); // make the x-axis fit exactly all bars
			mChart.invalidate(); // refresh
			mChart.animateXY(1400, 1400);
			mChart.invalidate();
		}
		else {
			Toast.makeText(getContext(),"No Data to be displayed in BarChart",Toast.LENGTH_LONG).show();
		}
	}

	public void CheckingPieDataAvailability(){if(Item_list.size()>0) {
		PieDataSet pieDataSet = new PieDataSet(Item_list, "");
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
		//pieChart.animateXY(2000, 1400);
	}else {
		Toast.makeText(getContext(),"No Data to be displayed in PieChart",Toast.LENGTH_LONG).show();
	}
	}

	public void GettingValueForLineEntryList(){
        float saving_amount = 0f;
        for (int j = 1; j <= day_x; j++) {
            boolean check=false;

            for (int i = 0; i < Expense_List.size(); i++) {

                if (Expense_List.get(i).getExpense_day() == j) {
                    saving_amount = (saving_amount - Expense_List.get(i).getExpense_amount());
                    check=true;
                    //Amount2=saving_amount;
                    //saving_amount.set(0,saving_amount.get(0)-Expense_List.get(i).getExpense_amount());
                }
            }
            for (int k = 0; k < Income_List.size(); k++) {
                if (Income_List.get(k).getIncome_day() == j) {
                    saving_amount = saving_amount + Income_List.get(k).getIncome_amount();
                    check=true;
                    //Amount2=saving_amount;
                    //saving_amount.set(0,saving_amount.get(0)+temp);
                    //saving_amount[0]=saving_amount[0]+temp;

                    Log.d("onCreateView: ", saving_amount + " ");
                }
            }
            //saving_amount=saving_amount+0f;

            if (check) {
                Line_Entries.add(new Entry(j, saving_amount));
            }
            Log.d("on", saving_amount +"");
        }

    }

    public void GettingValueForPieEntryList(){
		if (Entertainment_Expense>0f){
			Item_list.add(new PieEntry(Entertainment_Expense, "Entertainment"));
			colors.add(getResources().getColor(R.color.Pink));
		}if (Clothing_Expense>0f) {
			Item_list.add(new PieEntry(Clothing_Expense, "Clothing"));
			colors.add(getResources().getColor(R.color.Blue));
		}if (Health_expense>0f) {
			Item_list.add(new PieEntry(Health_expense, "Health"));
			colors.add(getResources().getColor(R.color.Yellow));
		}if (Food_Expense>0f) {
			Item_list.add(new PieEntry(Food_Expense, "Food"));
			colors.add(getResources().getColor(R.color.Orange));
		}if (Fuel_Expense>0f) {
			Item_list.add(new PieEntry(Fuel_Expense, "Fuel"));
			colors.add(getResources().getColor(R.color.Green));
		}
	}

	public void GettingValueForBarEntryList(Float number){
		if (Current_month == month_x) {
			if (Current_day == day_x) {
				//Log.d("Category ", Type);
				BarChart_Entries.add(new BarEntry(Income_date, number));
			} if (Current_day == (day_x - 1)) {
				//Log.d("Category ", Type);
				BarChart_Entries.add(new BarEntry(Income_date, number));
			} if (Current_day == (day_x - 2)) {
				//Log.d("Category ", Type);
				BarChart_Entries.add(new BarEntry(Income_date, number));
			} if (Current_day == (day_x - 3)) {
				//Log.d("Category ", Type);
				BarChart_Entries.add(new BarEntry(Income_date, number));
			} if (Current_day == (day_x - 4)) {
				//Log.d("Category ", Type);
				BarChart_Entries.add(new BarEntry(Income_date, number));
			} if (Current_day == (day_x - 5)) {
				//Log.d("Category ", Type);
				BarChart_Entries.add(new BarEntry(Income_date, number));
			} if (Current_day == (day_x - 6)) {
				//Log.d("Category ", Type);
				BarChart_Entries.add(new BarEntry(Income_date, number));
			} if (Current_day == (day_x - 7)) {
				//BarChart_Entries.add(new BarEntry(Income_date, number));
			} else {
				Log.d("Category ", Current_day + "  " + day_x);
			}
		} else {
			Log.d("Category ", Current_month + "  " + month_x);
		}

	}

	public void initLineChart(){
		mlineChart.setOnChartGestureListener(this);
		mlineChart.setOnChartValueSelectedListener(this);
		mlineChart.setDrawGridBackground(false);
		mlineChart.getDescription().setEnabled(false);
		mlineChart.setTouchEnabled(true);
		mlineChart.setDragEnabled(true);
		mlineChart.setScaleEnabled(true);
		mlineChart.setPinchZoom(true);


		MyMarkerView mv1 = new MyMarkerView(getContext(), R.layout.custom_marker_view);
		mv1.setChartView(mlineChart); // For bounds control
		mlineChart.setMarker(mv1); // Set the marker to the chart

		// x-axis limit line
		LimitLine llXAxis = new LimitLine(10f, "Index 10");
		llXAxis.setLineWidth(4f);
		llXAxis.enableDashedLine(10f, 10f, 0f);
		llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
		llXAxis.setTextSize(10f);

		XAxis xAxis1 = mlineChart.getXAxis();
		xAxis1.enableGridDashedLine(10f, 10f, 0f);
		xAxis1.setDrawGridLines(false);
	}

	public void initBarChart(){
		mChart.setOnChartValueSelectedListener(this);

		mChart.setDrawBarShadow(false);
		mChart.setDrawValueAboveBar(true);
		mChart.getDescription().setEnabled(false);
		mChart.setMaxVisibleValueCount(60);
		mChart.setPinchZoom(false);
		mChart.setDrawGridBackground(false);
		// mChart.setDrawYLabels(false);
		IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

		XAxis xAxis = mChart.getXAxis();
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
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

		XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter);
		mv.setChartView(mChart); // For bounds control
		mChart.setMarker(mv); // Set the marker to the chart



	}
	public void initPieChart(){

	}



	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	public void Reload(){
		mChart.invalidate();
		mlineChart.invalidate();
		pieChart.invalidate();
	}

   @Override
    public void onResume() {
        super.onResume();
        mChart.invalidate();
        mlineChart.invalidate();
        pieChart.invalidate();
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

	@Override
	public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

	}

	@Override
	public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

	}

	@Override
	public void onChartLongPressed(MotionEvent me) {

	}

	@Override
	public void onChartDoubleTapped(MotionEvent me) {

	}

	@Override
	public void onChartSingleTapped(MotionEvent me) {

	}

	@Override
	public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

	}

	@Override
	public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

	}

	@Override
	public void onChartTranslate(MotionEvent me, float dX, float dY) {

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


    /*public static String extractNumber(final String str) {
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
    }*/


}