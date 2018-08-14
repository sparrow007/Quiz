package android.zersey.expense_manger;

public class IncomeModel {

	private int id;
	private String type, title, description, totalAmount, paidAtDate, amountDue, payerId, invoiceId, catId;

	public IncomeModel(){}

	public IncomeModel(int id, String type, String title, String description, String totalAmount,
		String paidAtDate, String amountDue, String payerId, String invoiceId, String catId) {
		this.id = id;
		this.type = type;
		this.title = title;
		this.description = description;
		this.totalAmount = totalAmount;
		this.paidAtDate = paidAtDate;
		this.amountDue = amountDue;
		this.payerId = payerId;
		this.invoiceId = invoiceId;
		this.catId = catId;
	}

	public String getType() {
		return type;
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

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setPaidAtDate(String paidAtDate) {
		this.paidAtDate = paidAtDate;
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

	public String getTitle() {

		return title;
	}

	public String getDescription() {
		return description;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override public String toString() {
		return "IncomeModel{"
			+ "id="
			+ id
			+ ", type='"
			+ type
			+ '\''
			+ ", title='"
			+ title
			+ '\''
			+ ", description='"
			+ description
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
			+ ", catId='"
			+ catId
			+ '\''
			+ '}';
	}
}
