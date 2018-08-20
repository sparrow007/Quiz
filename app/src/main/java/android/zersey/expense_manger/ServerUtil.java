package android.zersey.expense_manger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.zersey.expense_manger.Data.TransactionDbHelper;
import com.google.gson.JsonObject;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerUtil {

	private Context context;
	private TransactionDbHelper mDbHelper;

	ServerUtil(Context context) {
		this.context = context;
		mDbHelper = new TransactionDbHelper(context);
	}

	public void createGroup(final GroupModel groupModel) {
		Call<JsonObject> result = JsonHandler.createGroup(context, groupModel);
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(@NonNull Call<JsonObject> call,
				@NonNull Response<JsonObject> response) {
				JsonObject object = response.body();
				//long id = object.get("uid").getAsLong();
				//incomeModel.setOnlineId(id);
				//mDbHelper.addOnlineId(incomeModel);
			}

			@Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
				t.printStackTrace();
			}
		});
	}

	public void createEntry(final IncomeModel incomeModel) {
		Call<JsonObject> result = JsonHandler.createEntry(context, incomeModel);
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(@NonNull Call<JsonObject> call,
				@NonNull Response<JsonObject> response) {
				JsonObject object = response.body();
				long id = object.get("uid").getAsLong();
				incomeModel.setOnlineId(id);
				mDbHelper.addOnlineId(incomeModel);
			}

			@Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
				t.printStackTrace();
			}
		});
	}

	public void updateEntry(final IncomeModel incomeModel) {
		//Call<JsonObject> result = JsonHandler.updateEntry(context, incomeModel);
		//result.enqueue(new Callback<JsonObject>() {
		//	@Override public void onResponse(@NonNull Call<JsonObject> call,
		//		@NonNull Response<JsonObject> response) {
		//		JsonObject object = response.body();
		//	}
		//
		//	@Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
		//		t.printStackTrace();
		//	}
		//});
	}

	public void deleteEntry(final long id) {
		Call<JsonObject> result = JsonHandler.deleteEntry(context, id);
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(@NonNull Call<JsonObject> call,
				@NonNull Response<JsonObject> response) {
				JsonObject object = response.body();
			}

			@Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
				t.printStackTrace();
			}
		});
	}

	public Custom_Contact_items getUserIdFromServer(String keyword, String code) {

		final Custom_Contact_items customContactItem = new Custom_Contact_items();

		Call<List<Custom_Contact_items>> result =
			NetworkUtil.getRestAdapter(context).getUserIdFromServer(keyword, code);
		result.enqueue(new Callback<List<Custom_Contact_items>>() {

			@Override public void onResponse(@NonNull Call<List<Custom_Contact_items>> call,
				@NonNull Response<List<Custom_Contact_items>> response) {
				List<Custom_Contact_items> item = response.body();
				//Log.d("hueh", "onResponse: " + response.body());
				if (item != null && item.size() > 0) {
					//Log.d("hueh", "onResponse: " + item.get(0).toString());
					customContactItem.setId(item.get(0).getId());
					customContactItem.setContact_Person_Name(item.get(0).getContact_Person_Name());
					customContactItem.setContact_Person_Number(
						item.get(0).getContact_Person_Number());
				}
			}

			@Override public void onFailure(@NonNull Call<List<Custom_Contact_items>> call,
				@NonNull Throwable t) {
				t.printStackTrace();
			}
		});

		return customContactItem;
	}
}
