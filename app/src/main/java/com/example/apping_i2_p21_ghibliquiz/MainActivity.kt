package com.example.apping_i2_p21_ghibliquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.character_list_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    private fun fill_array_with_random(list: IntArray) {
        val l = (list.indices).toList()
        Collections.shuffle(l)
        for (i in 0..5)
            list[i] = l[i]
    }

    private fun call_second_request(service:WebService,
                                    info_films:MutableList<String>,
                                    people_list_to_display:MutableList<CharacterItem>,
                                    good_response:Int) {
        val ws_callback_film_item: Callback<MovieItem> = object : Callback<MovieItem>{
            override fun onFailure(call: Call<MovieItem>, t: Throwable) {
                Log.d("Info", "Error on get films")
            }

            override fun onResponse(call: Call<MovieItem>, response: Response<MovieItem>) {
                val codeSecondRequest = response.code()
                if (codeSecondRequest == 200) {
                    val body = response.body()
                    if (body != null) {
                        val msg = "Wich one of these character can be found in "
                        textViewQuestion.text = msg + "'" + body!!.title + "' " + "?"
                        info_films.add(body!!.director)
                        info_films.add(body!!.release_date.toString())
                        info_films.add(body!!.title)
                        info_films.add(body!!.description)
                        info_films.add(people_list_to_display[good_response].name)
                    }
                }
            }
        }

        val first_film = people_list_to_display[good_response].films[0]
        service.filmItem(first_film).enqueue(ws_callback_film_item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val explicit_intent = Intent(this, Movie_Description::class.java)
        var people_list: List<CharacterItem>;
        var character_list: MutableList<CharacterItem> = mutableListOf<CharacterItem>()
        val random_indexes =  IntArray(20)
        fill_array_with_random(random_indexes)
        var good_response: Int = (0..4).random()

        val info_films: MutableList<String> = mutableListOf<String>()
        val base_url = "https://ghibliapi.herokuapp.com/"
        val json_converter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(json_converter)
            .build()

        val service: WebService = retrofit.create(WebService::class.java)
        val wsCallback: Callback<List<CharacterItem>> = object : Callback<List<CharacterItem>> {
            override fun onFailure(call: Call<List<CharacterItem>>, t: Throwable) {
                Log.d("Info", "Error on get people")
            }

            override fun onResponse(
                call: Call<List<CharacterItem>>,
                response: Response<List<CharacterItem>>
            ) {
                val code_first_request = response.code()
                if (code_first_request == 200) {
                    people_list = response.body()!!
                    for (i in 0..5) { //GET RANDOM PEOPLE INDEXES
                        val rand_index = random_indexes[i]
                        val people = people_list[rand_index]
                        character_list.add(people )
                    }

                    call_second_request(service, info_films, character_list, good_response)

                    val onItemClickListener : View.OnClickListener = View.OnClickListener { rowView ->
                        if (rowView.tag == good_response) {
                            explicit_intent.putExtra("RESPONSE_STATUS",
                                Activity_Data(true,
                                    character_list[rowView.tag as Int].films[0],
                                    info_films)
                            )
                            startActivity(explicit_intent)
                        }
                        else {
                            explicit_intent.putExtra("RESPONSE_STATUS", Activity_Data(false,
                                character_list[rowView.tag as Int].films[0], info_films))
                            startActivity(explicit_intent)
                        }
                    }

                    answer_list.setHasFixedSize(true)
                    answer_list.layoutManager = LinearLayoutManager(this@MainActivity)
                    answer_list.adapter = CharacterAdepter(
                        character_list,
                        this@MainActivity,
                        onItemClickListener)
                    answer_list.addItemDecoration(
                        DividerItemDecoration(
                            this@MainActivity,
                            DividerItemDecoration.VERTICAL)
                    )
                }
            }
        }
        service.listAllPeople().enqueue(wsCallback)

    }
}
