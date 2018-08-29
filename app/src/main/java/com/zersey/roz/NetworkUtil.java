package com.zersey.roz;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ShareEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtil {

	private static String cookie;

	private static class ReceivedCookiesInterceptor implements Interceptor {
		Context context;

		@Override public Response intercept(@NonNull Chain chain) throws IOException {
			Response originalResponse = chain.proceed(chain.request());

			if (!originalResponse.headers("Set-Cookie").isEmpty()) {
				HashSet<String> cookies = new HashSet<>();
				String cookie = "";
				for (String header : originalResponse.headers("Set-Cookie")) {
					cookies.add(header);
					cookie = header;
				}

				context.getSharedPreferences("login", Context.MODE_PRIVATE)
					.edit()
					.putString("cookies", cookie)
					.apply();
			}

			return originalResponse;
		}
	}

	public static RestAdapterAPI getRestAdapter(@NonNull Context context) {
		cookie =
			context.getSharedPreferences("login", Context.MODE_PRIVATE).getString("cookies", "");
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		// set your desired log level
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);

		// set header
		Interceptor header = new Interceptor() {
			@Override public Response intercept(Chain chain) throws IOException {
				Request original = chain.request();

				// Request customization: add request headers
				Request.Builder requestBuilder = original.newBuilder().addHeader("Cookie", cookie);

				Request request = requestBuilder.build();
				return chain.proceed(request);
			}
		};

		OkHttpClient.Builder httpClient =
			new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
				.writeTimeout(20, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS);

		ReceivedCookiesInterceptor receivedCookiesInterceptor = new ReceivedCookiesInterceptor();
		receivedCookiesInterceptor.context = context;

		// add logging as last interceptor
		httpClient.addInterceptor(receivedCookiesInterceptor)
			.addInterceptor(header)
			.addInterceptor(logging);

		Retrofit retrofit = new Retrofit.Builder().baseUrl(RestAdapterAPI.END_POINT_ZERSEY)
			.addConverterFactory(GsonConverterFactory.create())
			.client(enableTls12OnPreLollipop(httpClient).build())
			.build();

		return retrofit.create(RestAdapterAPI.class);
	}

	private static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
		if (Build.VERSION.SDK_INT < 22) {
			try {
				SSLContext sc = SSLContext.getInstance("TLSv1.2");
				sc.init(null, null, null);
				client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

				ConnectionSpec cs =
					new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(
						TlsVersion.TLS_1_2).build();

				List<ConnectionSpec> specs = new ArrayList<>();
				specs.add(cs);
				specs.add(ConnectionSpec.COMPATIBLE_TLS);
				specs.add(ConnectionSpec.CLEARTEXT);

				client.connectionSpecs(specs);
			} catch (Exception exc) {
				Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
			}
		}

		return client;
	}

	private static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo acNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return acNetworkInfo != null;
	}


	public static boolean hasInternetConnection(Context context) {
		if (isNetworkAvailable(context)) {
			try {
				HttpURLConnection urlc = (HttpURLConnection) (new URL("http://client3.google.com/generate_204").openConnection());
				urlc.setRequestProperty("User-Agent", "Test");
				urlc.setRequestProperty("Connection", "close");
				urlc.setReadTimeout(1500);
				urlc.connect();
				return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
			} catch (Exception e) {

			}
			return true;
		} else {
			return false;
		}
	}

	public static void shareNote(Context context, String onlineId, String title) {
		String text = "Hey! Found something interesting on Cerebro, check it out. You will simply love it!\n\n";

		Toast.makeText(context, "preparing share...", Toast.LENGTH_SHORT).show();
		String extra = text + title + "\n\n";
		String domain = "https://k24ek.app.goo.gl/";
		String apn = "?apn=com.zersey.roz";


		String link = "&link=" + RestAdapterAPI.END_POINT_ZERSEY + "/" + onlineId;

		String shareUrl = domain + apn + link;
		//shortLink(extra, context, shareUrl);

		//Answers.getInstance().logShare(new ShareEvent()
		//	.putMethod("Notes share")
		//	.putContentName(title + "")
		//	.putContentType("Anything shared")
		//	.putContentId(onlineId + ""));

		Intent i2 = new Intent(Intent.ACTION_SEND);
		i2.setType("text/plain");
		i2.putExtra(Intent.EXTRA_TEXT, shareUrl);
		context.startActivity(i2);
	}

}
