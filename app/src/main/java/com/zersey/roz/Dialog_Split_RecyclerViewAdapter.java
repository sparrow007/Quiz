package com.zersey.roz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Dialog_Split_RecyclerViewAdapter
	extends RecyclerView.Adapter<Dialog_Split_RecyclerViewAdapter.Split_ViewHolder> {
	private List<Split_Contact_model> list;
	private List<Integer> Amount_List;
	private String Slider;
	private int Amount;

	public Dialog_Split_RecyclerViewAdapter(List<Split_Contact_model> list, String Slider,
		int Amount) {
		this.list = list;
		this.Slider = Slider;
		Amount_List = new ArrayList<>();
		this.Amount = Amount;
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
			holder.Split_Amount.setText(100 / list.size() + "");
			Amount_List.add(position, 100 / list.size());
		}
		if (TextUtils.equals(Slider, "") || TextUtils.equals(Slider, "Equally")) {
			Amount_List.add(position, Integer.parseInt(list.get(position).getSplit_Amount()));
			holder.Currency2.setVisibility(View.GONE);
			holder.Currency.setVisibility(View.VISIBLE);
			holder.Currency.setText("Rs");
			holder.Contact_Name.setText(list.get(position).getContact_Name().getName());
			holder.Split_Amount.setText(list.get(position).getSplit_Amount());
			if (TextUtils.equals(Slider, "Equally")) {
				holder.Split_Amount.setInputType(InputType.TYPE_NULL);
			}
		}
		Log.d("Dialog: ", list.size() + "");
		String name = list.get(position).getSplit_Amount();
		Log.d("Dialog: ", name);

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

				if (Check()) {
					if (TextUtils.equals(Slider, "ratio")) {
						MainActivity.Split_Notes.setText("Total:" + "with 100%");
					} else {
						MainActivity.Split_Notes.setText("All settle with " + Amount);
					}
				} else {
					MainActivity.Split_Notes.setText("All Amount is not Split Correctly");
				}
			}

			@Override public void afterTextChanged(Editable s) {

			}
		});
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
			Log.d("Check: ", Amount + "  " + Total);
			return Total == 100;
		} else {
			int Total = 0;
			for (int i = 0; i < Amount_List.size(); i++) {
				Total = Total + Integer.parseInt(Amount_List.get(i).toString());
			}
			Log.d("Check: ", Amount + "  " + Total);
			return Total == Amount;
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
