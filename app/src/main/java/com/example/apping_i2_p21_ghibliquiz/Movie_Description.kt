package com.example.apping_i2_p21_ghibliquiz

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_movie__description.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Movie_Description : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie__description)
        val base_url = "https://ghibliapi.herokuapp.com/"
        val json_converter = GsonConverterFactory.create(GsonBuilder().create())

        val implicit_intent = Intent(Intent.ACTION_VIEW)

        val retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(json_converter)
            .build()

        val service:WebService = retrofit.create(WebService::class.java)

        val preview_data = intent.getSerializableExtra("RESPONSE_STATUS") as? Activity_Data

        if (preview_data != null) {
            val info_film = preview_data.info_film
            textViewDirector.text   = "Director: " + info_film[Info.DIRECTOR.value]
            textViewYear.text       = "Year: " + info_film[Info.YEAR.value]
            textViewMovieTitle.text  = "Title: " + info_film[Info.TITLEMOVIE.value]
            textViewSynopsis.text   = "Synopsis: " + info_film[Info.SYNOPSIS.value]
            textViewMovieCharacter.text  = "Characters: " + info_film[Info.CHARACTER.value]

            val uri: Uri = Uri.parse("http://www.google.com/#q=" + info_film[Info.TITLEMOVIE.value])
            implicit_intent.data = uri

            if (!preview_data.is_good_response) {
                textViewResponse.text = "WRONG !"
                textViewResponse.setTextColor(Color.RED)

                val ws_callback: Callback<MovieItem> = object : Callback<MovieItem> {
                    override fun onFailure(call: Call<MovieItem>, t: Throwable) {
                        Log.d("Info", "Error When the Api was Call")
                    }

                    override fun onResponse(call: Call<MovieItem>, response: Response<MovieItem>) {
                        if (response.code() == 200) {
                            textViewCharacterPlays.text = "This character can be seen in '" + response.body()!!.title + "'"
                        }
                    }
                }
                service.filmItem(preview_data.film_url).enqueue(ws_callback)
            } else {
                textViewResponse.text = "RIGHT !"
                textViewResponse.setTextColor(Color.GREEN)
            }
            button.setOnClickListener {
                startActivity(implicit_intent)
            }
        }
    }
}
