package com.zersey.roz;

import android.content.Context;
import com.google.gson.JsonObject;
import retrofit2.Call;

public class JsonHandler {

	public static Call<JsonObject> createEntry(Context context, IncomeModel incomeModel) {
		return NetworkUtil.getRestAdapter(context)
			.createEntry(incomeModel.getType(), incomeModel.getTitle(), incomeModel.getGroupId(),
				incomeModel.getDescription(), incomeModel.getUuid(),
				incomeModel.getPayerId().split(","), incomeModel.getTotalAmount().split(","),
				incomeModel.getAmountDue().split(","), incomeModel.getPaidAtDate().split(","),
				incomeModel.getInvoiceId().split(","), incomeModel.getAmountPaid().split(","));
	}

	//public static Call<JsonObject> updateEntry(Context context, IncomeModel incomeModel) {
	//	return NetworkUtil.getRestAdapter(context)
	//		.updateEntry(incomeModel.getOnlineId(), incomeModel.getType(), incomeModel.getTitle(),
	//			incomeModel.getDescription(), incomeModel.getTotalAmount(),
	//			incomeModel.getAmountDue(), incomeModel.getPayerId(), incomeModel.getPaidAtDate(),
	//			incomeModel.getInvoiceId(), incomeModel.getCatId());
	//}

	public static Call<JsonObject> createGroup(Context context, GroupModel groupModel) {
		return NetworkUtil.getRestAdapter(context)
			.createGroup(groupModel.getGroupName(), groupModel.getGroupDesc(),
				groupModel.getUsers(), groupModel.getTypeId(), null);
	}

	public static Call<JsonObject> deleteEntry(Context context, long id) {
		return NetworkUtil.getRestAdapter(context).deleteSpecificUserEntry(id);
	}

	public static IncomeModel handleSingleReminder(JsonObject obj) {

		IncomeModel model = new IncomeModel();

		try {
			model.setOnlineId(obj.get("id").getAsLong());
			model.setType(obj.get("type").getAsString());
			model.setUuid(obj.get("uuid").getAsString());
			if (!obj.get("title").isJsonNull()) {
				model.setTitle(obj.get("title").getAsString());
			}

			if (!obj.get("group_id").isJsonNull()) {
				model.setGroupId(obj.get("group_id").getAsLong());
			}

			if (!obj.get("description").isJsonNull()) {
				model.setDescription(obj.get("description").getAsString());
			}
			if (!obj.get("total_amount").isJsonNull()) {
				model.setTotalAmount(obj.get("total_amount").getAsString());
			}
			if (!obj.get("paid_at").isJsonNull()) {
				model.setPaidAtDate(obj.get("paid_at").getAsString());
			}
			if (!obj.get("amount_due").isJsonNull()) {
				model.setAmountDue(obj.get("amount_due").getAsString());
			}
			if (!obj.get("payer_id").isJsonNull()) {
				model.setPayerId(obj.get("payer_id").getAsString());
			}
			if (!obj.get("invoice_id").isJsonNull()) {
				model.setInvoiceId(obj.get("invoice_id").getAsString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}
}
