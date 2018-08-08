package android.zersey.expense_manger;

import android.content.Context;
import com.google.gson.JsonObject;
import retrofit2.Call;

public class JsonHandler {

	public static Call<JsonObject> createEntry(Context context, IncomeModel incomeModel) {
		Call<JsonObject> result = NetworkUtil.getRestAdapter(context)
			.createEntry(incomeModel.getType(), incomeModel.getTitle(),
				incomeModel.getDescription(), incomeModel.getTotalAmount(),
				incomeModel.getAmountDue(), incomeModel.getPayerId(), incomeModel.getPaidAtDate(),
				incomeModel.getInvoiceId(), incomeModel.getCatId());
		return result;
	}

	public static Call<JsonObject> updateEntry(Context context, IncomeModel incomeModel) {
		Call<JsonObject> result = NetworkUtil.getRestAdapter(context)
			.updateEntry(incomeModel.getId(), incomeModel.getType(), incomeModel.getTitle(),
				incomeModel.getDescription(), incomeModel.getTotalAmount(),
				incomeModel.getAmountDue(), incomeModel.getPayerId(), incomeModel.getPaidAtDate(),
				incomeModel.getInvoiceId(), incomeModel.getCatId());
		return result;
	}

	public static IncomeModel handleSingleReminder(Context context, JsonObject obj) {
		int id;
		String type, title, description, totalAmount, paidAtDate, amountDue, payerId, invoiceId,
			catId;

		IncomeModel model = new IncomeModel();

		try {
			model.setId(obj.get("id").getAsInt());
			model.setType(obj.get("type").getAsString());
			if (!obj.get("title").isJsonNull()) {
				model.setTitle(obj.get("title").getAsString());
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
			if (!obj.get("catid").isJsonNull()) {
				model.setCatId(obj.get("catid").getAsString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}
}
