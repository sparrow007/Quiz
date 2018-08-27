package android.zersey.expense_manger;

import java.io.Serializable;
import java.util.List;

public class IncomeModel implements Serializable {

	private int Income_day,Income_month,Income_year;
	private long id, onlineId, groupId;
	private String type, title, description, uuid, catId;
	private String totalAmount, paidAtDate, amountDue, payerId, invoiceId;
	private List<IncomeModel> subList;


	public void setId(long id) {
		this.id = id;
	}

	public void setOnlineId(long onlineId) {
		this.onlineId = onlineId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setPaidAtDate(String paidAtDate) {
		this.paidAtDate = paidAtDate;
		String[] temp=paidAtDate.split("-");
		Income_day=Integer.parseInt(temp[2]);
		Income_month=Integer.parseInt(temp[1]);
		Income_year=Integer.parseInt(temp[0]);
	}

	public int getIncome_day() {
		return Income_day;
	}

	public int getIncome_month() {
		return Income_month;
	}

	public int getIncome_year() {
		return Income_year;
	}

	public void setAmountDue(String amountDue) {
		this.amountDue = amountDue;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public long getId() {
		return id;
	}

	public long getOnlineId() {
		return onlineId;
	}

	public String getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getUuid() {
		return uuid;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public String getPaidAtDate() {
		return paidAtDate;
	}

	public String getAmountDue() {
		return amountDue;
	}

	public String getPayerId() {
		return payerId;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public List<IncomeModel> getSubList() {
		return subList;
	}

	public void setSubList(List<IncomeModel> subList) {
		this.subList = subList;
	}

	@Override public String toString() {
		return "IncomeModel{"
			+ "id="
			+ id
			+ ", onlineId="
			+ onlineId
			+ ", groupId="
			+ groupId
			+ ", type='"
			+ type
			+ '\''
			+ ", title='"
			+ title
			+ '\''
			+ ", description='"
			+ description
			+ '\''
			+ ", uuid='"
			+ uuid
			+ '\''
			+ ", catId='"
			+ catId
			+ '\''
			+ ", totalAmount='"
			+ totalAmount
			+ '\''
			+ ", paidAtDate='"
			+ paidAtDate
			+ '\''
			+ ", amountDue='"
			+ amountDue
			+ '\''
			+ ", payerId='"
			+ payerId
			+ '\''
			+ ", invoiceId='"
			+ invoiceId
			+ '\''
			+ ", subList="
			+ subList
			+ '}';
	}
}