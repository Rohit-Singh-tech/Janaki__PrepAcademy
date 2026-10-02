package com.example.janakiprepacademy.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton Retrofit Client configured for Janaki PrepAcademy API.
 * Supports switching between local development server (10.0.2.2:5000)
 * and the production Render Web Service URL (https://your-service.onrender.com/).
 */
object RetrofitClient {
    // Live production endpoint on Render
    private var baseUrl = "https://janaki-prepacademy.onrender.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    var apiService: ApiService = createService(baseUrl)
        private set

    private fun createService(url: String): ApiService {
        return Retrofit.Builder()
            .baseUrl(if (url.endsWith("/")) url else "$url/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    /**
     * Point the Android app directly to your Render Web Service URL
     * Example: RetrofitClient.setRenderUrl("https://janakiprep-api.onrender.com")
     */
    fun setRenderUrl(renderUrl: String) {
        baseUrl = if (renderUrl.endsWith("/")) renderUrl else "$renderUrl/"
        apiService = createService(baseUrl)
    }
}
