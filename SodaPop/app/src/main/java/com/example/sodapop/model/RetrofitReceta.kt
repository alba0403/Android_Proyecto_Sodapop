package com.example.sodapop.model

import com.example.sodapop.services.RecetasService
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
                    .baseUrl("http://129.80.23.224:8080/")//.baseUrl("https://oracleitic.mooo.com/") url de la api en aplication.properties
                    .addConverterFactory(GsonConverterFactory.create(gsondateformat))
                    .build()
                    .create(RecetasService::class.java)
            }
            return mItemAPI!!
        }
    }
}