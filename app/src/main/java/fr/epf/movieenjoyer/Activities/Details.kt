package fr.epf.movieenjoyer.Activities

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.movieenjoyer.R
import fr.epf.movieenjoyer.*
import fr.epf.movieenjoyer.Adapters.Adapter2
import fr.epf.movieenjoyer.Others.*
import com.squareup.picasso.Picasso
import fr.epf.movieenjoyer.Others.APIService
import fr.epf.movieenjoyer.Others.Overview
import fr.epf.movieenjoyer.Others.TmdbList
import kotlinx.android.synthetic.main.activity_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("Registered")
class Details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbarDetails)
        supportActionBar?.title = "Détails"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pos = intent.getIntExtra("ID", 55)
        val retrofitClient = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofitClient.create(APIService::class.java)

        service.overview(pos).enqueue(object : Callback<Overview> {
            override fun onFailure(call: Call<Overview>, t: Throwable) {
                textviewmore.text="Erreur lors du chargement, veuillez réessayer"
            }
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Overview>, response: Response<Overview>) {
                runOnUiThread {
                    Picasso.get().load("https://image.tmdb.org/t/p/original" + response.body()?.backdrop_path).fit().centerCrop().into(toolbarimage)
                    progressBar.visibility = View.GONE
                    if(response.body()?.backdrop_path == null){
                        Picasso.get().load("https://fasterthemes.com/demo/foodrecipespro-wordpress-theme/wp-content/themes/foodrecipespro/images/no-image.jpg").fit().centerCrop().into(toolbarimage)
                    }
                }
            }
        })

        service.overview(pos).enqueue(object : Callback<Overview> {
            override fun onFailure(call: Call<Overview>, t: Throwable) {
                textviewmore.text="Erreur lors du chargement, veuillez réessayer"
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<Overview>,
                response: Response<Overview>
            ) {
                runOnUiThread {
                    val sharedPreferences = getSharedPreferences("favorites",
                        Context.MODE_PRIVATE
                    )
                    var editor = sharedPreferences.edit()
                    fav_button.setOnClickListener {
                        unfav_button.visibility = View.VISIBLE
                        fav_button.visibility = View.GONE
                        editor.putString(response.body()!!.id.toString(), System.currentTimeMillis().toString())
                        editor.commit()
                    }
                    unfav_button.setOnClickListener {
                        unfav_button.visibility = View.GONE
                        fav_button.visibility = View.VISIBLE
                        editor.remove(response.body()!!.id.toString())
                        editor.commit()
                    }
                    val allEntries: Map<String, *> = sharedPreferences.getAll()
                    val arrayFav = arrayListOf<String>()

                    for (entry in allEntries) {
                        arrayFav.add(entry.key as String)
                    }
                    if(arrayFav.indexOf(response.body()!!.id.toString()) != -1){
                        unfav_button.visibility = View.VISIBLE
                        fav_button.visibility = View.GONE
                    } else {
                        unfav_button.visibility = View.GONE
                        fav_button.visibility = View.VISIBLE
                    }


                    textviewmore.text="\n"+response.body()!!.title+
                            "\n\n⭐ "+ response.body()!!.vote_average+
                            "/10\n\nSynopsis:  "+ response.body()!!.overview+"\n" +
                            "\nDate de sortie: "+response.body()!!.release_date+
                            "\n\nDurée: "+
                            response.body()!!.runtime/60 +"h" +response.body()!!.runtime%60+
                            "\n\nGenres: "

                    for(i in 0 until response.body()!!.genres.size ){
                        textviewmore.text= textviewmore.text.toString() +response.body()!!.genres[i].name+", "
                    }
                    textviewmore.text=textviewmore.text.substring(0,textviewmore.text.length-2)
                }
            }
        })


        service.similarmovies(pos).enqueue(object : Callback<TmdbList> {
            override fun onFailure(call: Call<TmdbList>, t: Throwable) {
                textviewmore.text="Erreur lors du chargement, veuillez réessayer"
            }

            override fun onResponse(
                call: Call<TmdbList>,
                response: Response<TmdbList>
            ) {
                runOnUiThread {
                    recycler2.layoutManager = LinearLayoutManager(this@Details,LinearLayoutManager.HORIZONTAL,false)
                    recycler2.adapter =
                            Adapter2(this@Details, response.body()!!.results)
                }
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
