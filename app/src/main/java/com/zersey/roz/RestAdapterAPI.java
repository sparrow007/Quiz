package com.zersey.roz;

import com.google.gson.JsonObject;
import java.util.List;
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
		@Field("type") String type, @Field("title") String title, @Field("group_id") long groupId,
		@Field("description") String description, @Field("uuid") String uuid,
		@Field("payer_id[]") String[] payerId, @Field("total_amount[]") String[] totalAmount,
		@Field("amount_due[]") String[] amountDue, @Field("paid_at[]") String[] paidAt,
		@Field("invoice_id[]") String[] invoiceId);

	@FormUrlEncoded @POST("/Split_bills/create_edit_group") Call<JsonObject> createGroup(
		@Field("group_name") String groupName, @Field("group_description") String groupDescription,
		@Field("users") String users, @Field("type_id") int typeId);

	@GET("/Split_bills/fetch_groups") Call<JsonObject> fetchGroups(@Query("userid") String userId,
		@Query("id") String id);

	@GET("/Split_bills/fetch_income_expense") Call<JsonObject> fetchAllUserEntry();

	@GET("/Split_bills/fetch_income_expense/{id}") Call<JsonObject> fetchSpecificUserEntry(
		@Path("id") long id);

	@GET("/Split_bills/delete_income_expense/{id}") Call<JsonObject> deleteSpecificUserEntry(
		@Path("id") long id);

	@GET("Split_bills/search_contacts") Call<List<Custom_Contact_items>> getUserIdFromServer(
		@Query("keyword") String keyword, @Query("code") String code);

	@FormUrlEncoded @POST("/Split_bills/report_income_expense_by_filter")
	Call<JsonObject> reportIncomeExpense(@Field("payer_id") String payerId);
}
