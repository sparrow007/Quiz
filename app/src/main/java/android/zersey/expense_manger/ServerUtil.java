package android.zersey.expense_manger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.zersey.expense_manger.Data.TransactionDbHelper;
import com.google.gson.JsonObject;
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

	public void createEntry(final IncomeModel incomeModel) {
		Call<JsonObject> result = JsonHandler.createEntry(context, incomeModel);
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(@NonNull Call<JsonObject> call,
				@NonNull Response<JsonObject> response) {
				JsonObject object = response.body();

				int id = object.get("uid").getAsInt();
				incomeModel.setId(id);
				mDbHelper.createEntry(incomeModel);
			}

			@Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
				t.printStackTrace();
			}
		});
	}

	public void updateEntry(final int pos, final IncomeModel incomeModel) {
		Call<JsonObject> result = JsonHandler.updateEntry(context, incomeModel);
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(@NonNull Call<JsonObject> call,
				@NonNull Response<JsonObject> response) {
				JsonObject object = response.body();
				mDbHelper.updateEntry(pos, incomeModel);
			}

			@Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
				t.printStackTrace();
			}
		});
	}

	public void deleteEntry(final int pos, final int id){
		Call<JsonObject> result = JsonHandler.deleteEntry(context, id);
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(@NonNull Call<JsonObject> call,
				@NonNull Response<JsonObject> response) {
				JsonObject object = response.body();
				mDbHelper.deleteEntry(pos, id);
			}

			@Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
				t.printStackTrace();
			}
		});
	}
}
