package com.zersey.roz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DialogSplitRecyclerViewAdapter
	extends RecyclerView.Adapter<DialogSplitRecyclerViewAdapter.Split_ViewHolder> {
	private List<Split_Contact_model> list;
	private List<Integer> Amount_List;
	private String Slider;
	private int Amount;
	private Boolean split_Correctly;

	public DialogSplitRecyclerViewAdapter(List<Split_Contact_model> list, String Slider,
		int Amount) {
		this.list = list;
		this.Slider = Slider;
		Amount_List = new ArrayList<>();
		this.Amount = Amount;

		if (TextUtils.equals(Slider, "ratio")){
			BillsFormFragment.splitNotes.setText(Html.fromHtml("<font color=#000000>Total: 100% of 100%</font> \n<font color=#90A4AE> <br>left "+0+"%</font>"));
		}else if (TextUtils.equals(Slider, "Equally")){
			BillsFormFragment.splitNotes.setText("");
		}else {
			double amount = Double.parseDouble(Amount+"");
			DecimalFormat formatter = new DecimalFormat("#,###");

			BillsFormFragment.splitNotes.setText(Html.fromHtml("<font color=#000000>Total: Rs "+formatter.format(amount)+" of Rs "+formatter.format(amount)+"</font> <font color=#90A4AE> <br>left Rs "+0+"</font>"));
			//MainActivity.splitNotes.setText(("Total: "+Amount+" of "+Amount+" \nleft "+0));
		}
	}

	@NonNull @Override
	public Split_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.slip_contact_recyclerview_layout, parent, false);
		return new Split_ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final Split_ViewHolder holder, final int position) {


		if (TextUtils.equals(Slider, "ratio")) {
			holder.Currency2.setVisibility(View.VISIBLE);
			holder.Currency.setVisibility(View.GONE);
			holder.Currency2.setText("%");
			holder.Contact_Name.setText(list.get(position).getContact_Name().getName());
			//holder.Split_Amount.setText(100 / list.size() + "");
			if (position==list.size()-1)
			{
			if (list.size() % 2 != 0) {
				holder.Split_Amount.setText((100 / list.size()) + 1 + "");
			} else {
				holder.Split_Amount.setText(100 / list.size() + "");
			}
		       }else {holder.Split_Amount.setText(100 / list.size() + "");}
			Amount_List.add(position, 100 / list.size());
		}
		if (TextUtils.equals(Slider, "") || TextUtils.equals(Slider, "Equally")) {
			Amount_List.add(position, Integer.parseInt(list.get(position).getSplit_Amount()));
			holder.Currency2.setVisibility(View.GONE);
			holder.Currency.setVisibility(View.VISIBLE);
			holder.Currency.setText("Rs");
			holder.Contact_Name.setText(list.get(position).getContact_Name().getName());
			holder.Split_Amount.setText(list.get(position).getSplit_Amount());
			/*if (position==list.size()-1)
			{
				if (list.size() % 2 != 0) {
				holder.Split_Amount.setText(Integer.parseInt(list.get(position).getSplit_Amount()) + 1+"");
			} else {
				holder.Split_Amount.setText(list.get(position).getSplit_Amount());
			}
		         }else {holder.Split_Amount.setText(list.get(position).getSplit_Amount());}*/

			if (TextUtils.equals(Slider, "Equally")) {
				holder.Split_Amount.setInputType(InputType.TYPE_NULL);
			}
		}
		Log.d("splitDialog: ", list.size() + "");
		String name = list.get(position).getSplit_Amount();
		Log.d("splitDialog: ", name);

		holder.Split_Amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override public void onFocusChange(View v, boolean hasFocus) {

			}
		});
		holder.Split_Amount.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (TextUtils.equals(holder.Split_Amount.getText().toString(), "")) {
					Amount_List.set(position, 0);
				} else {
					Amount_List.set(position,
						Integer.parseInt(holder.Split_Amount.getText().toString()));
				}

				if (TextUtils.equals(Slider, "ratio")) {
					try {
						list.get(position)
							.setSplit_Amount(Integer.toString(
								(Integer.parseInt(holder.Split_Amount.getText().toString()) * Amount)
									/ 100));
					} catch(NumberFormatException ignored){

					}
				} else {
					list.get(position).setSplit_Amount(holder.Split_Amount.getText().toString());
				}
                   Check();
				/*if (Check()) {

					if (TextUtils.equals(Slider, "ratio")) {
						MainActivity.splitNotes.setText("Total:" + "with 100%");
					} else {
						MainActivity.splitNotes.setText("All settle with " + Amount);
					}
				} else {
					MainActivity.splitNotes.setText("All Amount is not Split Correctly");
				}*/
			}

			@Override public void afterTextChanged(Editable s) {
				Check();
				/*if (Check()) {

					if (TextUtils.equals(Slider, "ratio")) {
						MainActivity.splitNotes.setText("Total:" + "with 100%");
					} else {
						MainActivity.splitNotes.setText("All settle with " + Amount);
					}
				} else {
					MainActivity.splitNotes.setText("All Amount is not Split Correctly");
				}*/
			}
		});
	}

	public Boolean getSplit_Correctly() {
		return split_Correctly;
	}

	public List<Split_Contact_model> getList() {
		return list;
	}

	@Override public int getItemCount() {
		return list.size();
	}

	public Boolean Check() {
		if (TextUtils.equals(Slider, "ratio")) {
			int Total = 0;
			for (int i = 0; i < Amount_List.size(); i++) {
				Total = Total + Integer.parseInt(Amount_List.get(i).toString());
			}
			//if(list.size()%2!=0){Total++;}
			Log.d("Check: ", Amount + "  " + Total);
			if (Total==100){
				split_Correctly=true;
				BillsFormFragment.splitNotes.setText(Html.fromHtml("<font color=#000000>Total: "+Total+"% of 100%</font> <font color=#90A4AE> <br>left "+(100-Total)+"%</font>"));
				BillsFormFragment.positive_Button.setEnabled(true);
				return true;
			}else {split_Correctly=false;
				BillsFormFragment.splitNotes.setText(Html.fromHtml("<font color=#000000>Total: "+Total+"% of 100%</font> <font color=#90A4AE> <br>left "+(100-Total)+"%</font>"));
				BillsFormFragment.positive_Button.setEnabled(false);
			return false;}

		} else {
			int Total = 0;
			for (int i = 0; i < Amount_List.size(); i++) {
				Total = Total + Integer.parseInt(Amount_List.get(i).toString());
			}
			//if(list.size()%2!=0){Total++;}
			Log.d("Check: ", Amount + "  " + Total);

			double amount = Double.parseDouble(Amount+"");
			DecimalFormat formatter = new DecimalFormat("#,###");
			double total = Double.parseDouble(Total+"");
			DecimalFormat formattert = new DecimalFormat("#,###");
			double left = Double.parseDouble((Amount-Total)+"");
			//DecimalFormat formatterl = new DecimalFormat("#,###");
			if (Total==Amount){
				split_Correctly=true;
				BillsFormFragment.splitNotes.setText(Html.fromHtml("<font color=#000000>Total: Rs "+formattert.format(total)+" of Rs "+formatter.format(amount)+"</font> <font color=#90A4AE> <br>left Rs "+formatter.format(left)+"</font>"));
				BillsFormFragment.positive_Button.setEnabled(true);
				return true;
			}else {split_Correctly=false;
				BillsFormFragment.splitNotes.setText(Html.fromHtml("<font color=#000000>Total: Rs "+formattert.format(total)+" of Rs "+formatter.format(amount)+"</font> <font color=#90A4AE> <br>left Rs "+formatter.format(left)+"</font>"));
				BillsFormFragment.positive_Button.setEnabled(false);
			return false;}
		}
	}

	public class Split_ViewHolder extends RecyclerView.ViewHolder {
		public TextView Contact_Name;
		public EditText Split_Amount;
		public TextView Currency, Currency2;

		public Split_ViewHolder(View itemView) {
			super(itemView);
			Contact_Name = itemView.findViewById(R.id.RecyclerView_Contact_Name);
			Split_Amount = itemView.findViewById(R.id.RecyclerView_Split_Amount);
			Currency = itemView.findViewById(R.id.Currency);
			Currency2 = itemView.findViewById(R.id.Currency2);
		}

		public EditText getSplit_Amount() {
			return Split_Amount;
		}
	}
}
