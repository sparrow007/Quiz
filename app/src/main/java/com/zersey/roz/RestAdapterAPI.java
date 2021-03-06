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
	String END_POINT_ROZ = "http://okroz.com";

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
		@Field("invoice_id[]") String[] invoiceId, @Field("amount_paid[]") String[] amountPaid);

	@FormUrlEncoded @POST("/Split_bills/create_edit_group") Call<JsonObject> createGroup(
		@Field("group_name") String groupName, @Field("group_description") String groupDescription,
		@Field("users") String users, @Field("type_id") int typeId,
		@Field("mobile_no") String mobile_no, @Field("group_image") String groupImage,
		@Field("group_settings") String groupSettings);

	@FormUrlEncoded @POST("/Split_bills/create_edit_group/{id}") Call<JsonObject> editGroup(
		@Path("id") long id, @Field("group_name") String groupName,
		@Field("group_description") String groupDescription, @Field("users") String users,
		@Field("type_id") int typeId, @Field("group_image") String groupImage);

	@GET("/Split_bills/fetch_groups") Call<JsonObject> fetchGroups(@Query("userid") String userId,
		@Query("id") String id);

	@GET("/Split_bills/fetch_income_expense") Call<JsonObject> fetchAllUserEntry(
		@Query("id") String id, @Query("group_id") String groupId);

	/*@GET("/Split_bills/fetch_income_expense/{id}") Call<JsonObject> fetchSpecificUserEntry(
		@Path("id") long id);*/

	@GET("/Split_bills/delete_income_expense/{id}") Call<JsonObject> deleteSpecificUserEntry(
		@Path("id") long id);

	@GET("/Split_bills/search_contacts") Call<List<ContactModel>> getUserIdFromServer(
		@Query("keyword") String keyword, @Query("code") String code);

	@FormUrlEncoded @POST("/Split_bills/report_income_expense_by_filter")
	Call<JsonObject> reportIncomeExpense(@Field("payer_id") String payerId);

	@GET("/Invites/send_income_expense_invite") Call<JsonObject> inviteContact(
		@Query("link") String link, @Query("mobile_no") String mobile,
		@Query("country_code") String code);

	@FormUrlEncoded @POST("/mobile/contacts/verify_contact_list") Call<JsonObject> verifyContacts(
		@Field("contact[]") String contact[]);

	@FormUrlEncoded @POST("/Split_bills/create_edit_group_item") Call<JsonObject> createGroupNote(
		@Field("item_title") String itemTitle, @Field("item_description") String itemDescription,
		@Field("group_id") long groupId, @Field("assigned_to") long assignedTo,
		@Field("reminder_date") String reminderDate);

	@GET("/Split_bills/fetch_group_items") Call<JsonObject> fetchGroupNotes(@Query("id") String id,
		@Query("group_id") String groupId);
}
