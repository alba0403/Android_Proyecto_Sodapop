package com.example.sodapop

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitReceta {
    companion object {
        private var mItemAPI: RecetasService? = null

        @Synchronized
        fun API(): RecetasService {
            if (mItemAPI == null) {

                val gsondateformat = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create()

                mItemAPI = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gsondateformat))
                    //.baseUrl("https://oracleitic.mooo.com/") url de la api en aplication.properties
                    .build()
                    .create(RecetasService::class.java)
            }
            return mItemAPI!!
        }
    }
}