package com.zersey.roz;

import android.content.Context;
import android.support.annotation.NonNull;
import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonObject;
import com.zersey.roz.Data.TransactionDbHelper;
import io.fabric.sdk.android.services.concurrency.AsyncTask;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
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
							NetworkUtil.inviteLink(context, Long.toString(id),
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

	public void editGroup(final GroupModel groupModel, final List<ContactModel> itemList) {
		Call<JsonObject> result = NetworkUtil.getRestAdapter(context)
			.editGroup(groupModel.getGroupId(), groupModel.getGroupName(),
				groupModel.getGroupDesc(), groupModel.getUsers(), groupModel.getTypeId(), "");
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(@NonNull Call<JsonObject> call,
				@NonNull Response<JsonObject> response) {
				JsonObject object = response.body();
				long id = object.get("id").getAsLong();
				groupModel.setGroupId(id);
				if (itemList != null) {
					for (ContactModel m : itemList) {
						if (m.getUserId() == 0) {
							NetworkUtil.inviteLink(context, Long.toString(id),
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

	public void createEntry(final BillModel billModel) {
		Call<JsonObject> result = JsonHandler.createEntry(context, billModel);
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(@NonNull Call<JsonObject> call,
				@NonNull Response<JsonObject> response) {
				JsonObject object = response.body();
				long id = object.get("id").getAsLong();
				billModel.setOnlineId(id);
				mDbHelper.addTransactionOnlineId(billModel);
			}

			@Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
				t.printStackTrace();
			}
		});
	}

	public void updateEntry(final BillModel billModel) {
		//Call<JsonObject> result = JsonHandler.updateEntry(context, billModel);
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
							//mDbHelper.updateUserId(rowId, customContactItem);
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

	public void verifyContacts(final String[] numbers) {

		final HashMap<String, Long> map = new HashMap<>();

		Call<JsonObject> call = NetworkUtil.getRestAdapter(context).verifyContacts(numbers);
		call.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(@NonNull Call<JsonObject> call,
				@NonNull Response<JsonObject> response) {
				JsonObject jsonObject = response.body();
				Crashlytics.log(jsonObject.toString());

				for (String number1 : numbers) {
					JsonObject number = jsonObject.getAsJsonObject(number1);

					if (number.has("userId")) {
						long userId = number.get("userId").getAsLong();
						map.put(number1, userId);
					}
				}
				mDbHelper.updateUserIds(map);
			}

			@Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
				StringWriter sw = new StringWriter();
				t.printStackTrace(new PrintWriter(sw));
				String exceptionAsString = sw.toString();
				Crashlytics.log(exceptionAsString);
				t.printStackTrace();
			}
		});
	}

	public void createSingleGroup(final GroupModel groupModel, final BillModel expenseModel,
		final List<ContactModel> itemList) {
		Call<JsonObject> result = JsonHandler.createGroup(context, groupModel);
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(@NonNull Call<JsonObject> call,
				@NonNull Response<JsonObject> response) {
				JsonObject object = response.body();
				long id = object.get("id").getAsLong();
				groupModel.setGroupId(id);
				expenseModel.setGroupId(id);
				mDbHelper.addOnlineId(groupModel);
				long row = mDbHelper.createEntry(expenseModel);
				expenseModel.setId(row);
				Groups.groupsAdapter.addItem(expenseModel);
				Call<JsonObject> res = JsonHandler.createEntry(context, expenseModel);
				res.enqueue(new Callback<JsonObject>() {
					@Override public void onResponse(@NonNull Call<JsonObject> call,
						@NonNull Response<JsonObject> response) {
						JsonObject object = response.body();
						long id = object.get("id").getAsLong();
						expenseModel.setOnlineId(id);
						mDbHelper.addTransactionOnlineId(expenseModel);
					}

					@Override
					public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
						t.printStackTrace();
					}
				});
				if (itemList != null) {
					for (ContactModel m : itemList) {
						if (m.getUserId() == 0) {
							NetworkUtil.inviteLink(context, Long.toString(id),
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

	public void createGroupTask(final TaskModel taskModel) {
		Call<JsonObject> result = NetworkUtil.getRestAdapter(context)
			.createGroupNote(taskModel.getTask_Title(), taskModel.getTask_Des(),
				taskModel.getGroup_Id(), taskModel.getAssignedTo(), taskModel.getTask_Date());
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(@NonNull Call<JsonObject> call,
				@NonNull Response<JsonObject> response) {
				JsonObject jsonObject = response.body();
				long id = jsonObject.get("id").getAsLong();
				taskModel.setTask_Id(id);
				mDbHelper.addOnlineIdForNote(taskModel);
			}

			@Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

			}
		});
	}
}
