package android.zersey.expense_manger;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestAdapterAPI {
	String END_POINT_ZERSEY = "https://zersey.com";

	//login
	@FormUrlEncoded @POST("/authentication/check") Call<JsonObject> doLogin(
		@Field("country_code") String code, @Field("login") String phoneNo);

	//signUp
	@FormUrlEncoded @POST("/auth/signup") Call<JsonObject> signUp(
		@Field("password") String password, @Field("fullname") String fullname,
		@Field("otp") String otp, @Field("DOB") String dob, @Field("rewardId") String rewardId,
		@Field("device_id") String deviceId);

	//verify password
	@FormUrlEncoded @POST("/authentication/passlogin") Call<JsonObject> verifyPassword(
		@Field("password") String password, @Field("type") String type);

	@FormUrlEncoded @POST("/authentication/verify_password") Call<JsonObject> passwordVerify(
		@Field("password") String password);

	// resend OTP
	@FormUrlEncoded @POST("/authentication/reset_pass") Call<JsonObject> forgotPassword(
		@Field("password") String password, @Field("otp") String otp);

	//send OTP over voice
	@GET("/authentication/send_otp_over_phonecall") Call<Void> sendVoiceOTP();

	//resend OTP
	@GET("/authentication/otp_resend") Call<JsonObject> resendOtp();

	//resend OTP
	@GET("/authentication/app_version_check") Call<JsonObject> checkApp(
		@Query("version") String version);

	//logout
	@GET("/logout") Call<Void> logout();

	@FormUrlEncoded @POST("/Split_bills/create_income_expense") Call<JsonObject> createEntry(
		@Field("type") String type, @Field("title") String title,
		@Field("description") String description, @Field("total_amount") String totalAmount,
		@Field("amount__due") String amountDue, @Field("payer_id") String payerId,
		@Field("paid_at") String paidAt, @Field("invoice_id") String invoiceId,
		@Field("catid") String catId);

	@FormUrlEncoded @POST("/Split_bills/edit_income_expense/{id}") Call<JsonObject> updateEntry(
		@Path("id") long id, @Field("type") String type, @Field("title") String title,
		@Field("description") String description, @Field("total_amount") String totalAmount,
		@Field("amount__due") String amountDue, @Field("payer_id") String payerId,
		@Field("paid_at") String paidAt, @Field("invoice_id") String invoiceId,
		@Field("catid") String catId);

	@GET("/Split_bills/fetch_income_expense") Call<JsonObject> fetchAllUserEntry();

	@GET("/Split_bills/fetch_income_expense/{id}") Call<JsonObject> fetchSpecificUserEntry(
		@Path("id") long id);

	@GET("/Split_bills/delete_income_expense/{id}") Call<JsonObject> deleteSpecificUserEntry(
		@Path("id") long id);

	@FormUrlEncoded @POST("/Split_bills/report_income_expense_by_filter")
	Call<JsonObject> reportIncomeExpense(@Field("payer_id") String payerId);
}
