package com.zersey.roz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
	private List<String> list;
	private int lastSelectedPosition = -1;
	private String lastCategory = "";
	private Context context;
	private String Category;
	OnCategoryClickListener onCategoryClickListener;
	public interface OnCategoryClickListener {
		void onClick(String category);
	}

	public CategoryAdapter(Context context, List<String> list, String Category) {
		this.context = context;
		this.list = list;
		this.Category = Category;
	}

	@NonNull @Override
	public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.modelcategorylayout, parent, false);
		return new CategoryViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {
		holder.category.setText(list.get(position));
		holder.category.setChecked(false);
		holder.category.setChecked(position == lastSelectedPosition);

		if (!TextUtils.isEmpty(Category)) {
			if (Category.equalsIgnoreCase(list.get(position))) {
				holder.category.setChecked(true);
			}
		}
	}

	@Override public int getItemCount() {
		return list.size();
	}

	public String getLastCategory() {
		return lastCategory;
	}

	public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener) {
		this.onCategoryClickListener = onCategoryClickListener;
	}

	public class CategoryViewHolder extends RecyclerView.ViewHolder {
		RadioButton category;

		public CategoryViewHolder(View itemView) {
			super(itemView);
			category = itemView.findViewById(R.id.Cat_View);
			category.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					Category = "";
					lastSelectedPosition = getAdapterPosition();
					notifyDataSetChanged();
					if (!lastCategory.equalsIgnoreCase(category.getText().toString())) {
						lastCategory = category.getText().toString();
					}
					if (onCategoryClickListener!= null) {
						onCategoryClickListener.onClick(category.getText().toString());
					}
					/*Toast.makeText(context, "selected offer is " + category.getText(),
						Toast.LENGTH_LONG).show();*/
				}
			});
		}
	}
}
