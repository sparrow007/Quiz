package android.zersey.expense_manger;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import java.io.IOException;
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
}
