package com.zersey.roz;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.gson.JsonObject;
import com.zersey.roz.Data.TransactionDbHelper;
import io.fabric.sdk.android.services.concurrency.AsyncTask;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerUtil {

	private Context context;
	private TransactionDbHelper mDbHelper;

	ServerUtil(Context context) {
		this.context = context;
		mDbHelper = TransactionDbHelper.getInstance(context);
	}

	public void createGroup(final GroupModel groupModel, final List<ContactModel> itemList) {
		Call<JsonObject> result = JsonHandler.createGroup(context, groupModel);
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(@NonNull Call<JsonObject> call,
				@NonNull Response<JsonObject> response) {
				JsonObject object = response.body();
				long id = object.get("id").getAsLong();
				groupModel.setGroupId(id);
				mDbHelper.addOnlineId(groupModel);
				if (itemList != null) {
					for (ContactModel m : itemList) {
						if (m.getUserId() == 0) {
							NetworkUtil.shareNote(context, Long.toString(id),
								groupModel.getGroupName(), m.getNumber());
						}
					}
				}
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
				long id = object.get("id").getAsLong();
				incomeModel.setOnlineId(id);
				mDbHelper.addTransactionOnlineId(incomeModel);
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

	public void getUserIdFromServer(final long rowId, final String keyword, final String code) {

		AsyncTask.execute(new Runnable() {
			@Override public void run() {
				final ContactModel customContactItem = new ContactModel();

				Call<List<ContactModel>> result =
					NetworkUtil.getRestAdapter(context).getUserIdFromServer(keyword, code);

				result.enqueue(new Callback<List<ContactModel>>() {

					@Override public void onResponse(@NonNull Call<List<ContactModel>> call,
						@NonNull Response<List<ContactModel>> response) {

						List<ContactModel> item = response.body();
						if (item != null && item.size() > 0) {
							customContactItem.setId(item.get(0).getId());
							customContactItem.setName(item.get(0).getName());
							customContactItem.setNumber(item.get(0).getNumber());
							customContactItem.setUserId(item.get(0).getUserId());
							mDbHelper.updateUserId(rowId, customContactItem);
						}
					}

					@Override public void onFailure(@NonNull Call<List<ContactModel>> call,
						@NonNull Throwable t) {
						t.printStackTrace();
					}
				});
			}
		});
	}

	public void inviteContact(String link, String mobile, String code) {
		Call<JsonObject> call =
			NetworkUtil.getRestAdapter(context).inviteContact(link, mobile, code);
		call.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(@NonNull Call<JsonObject> call,
				@NonNull Response<JsonObject> response) {

			}

			@Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

			}
		});
	}
}
