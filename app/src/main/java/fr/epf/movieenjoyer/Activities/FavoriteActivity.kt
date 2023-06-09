package fr.epf.movieenjoyer.Activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.movieenjoyer.Adapters.ViewMoreAdapter
import fr.epf.movieenjoyer.Others.APIService
import fr.epf.movieenjoyer.Others.Overview
import fr.epf.movieenjoyer.Others.TmdbResponse
import fr.epf.movieenjoyer.R
import kotlinx.android.synthetic.main.activity_view_more.progressBar
import kotlinx.android.synthetic.main.activity_view_more.recyclerViewMore
import kotlinx.android.synthetic.main.activity_view_more.toolbarViewMore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat


class FavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_more)

        setSupportActionBar(toolbarViewMore)
        supportActionBar?.title = "Favoris"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val retrofitClient = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE)
        val allEntries: Map<String, *> = sharedPreferences.getAll()
        val arrayFav = arrayListOf<String>()
        val arrayFinal = arrayListOf<TmdbResponse>()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val iterator: Iterator<Map.Entry<String, Any?>> = allEntries.iterator()

        for (entry in allEntries) {
            iterator.next()
            arrayFav.add(entry.key as String)
            val service = retrofitClient.create(APIService::class.java)
            service.overview((entry.key as String).toInt()).enqueue(object : Callback<Overview> {
                override fun onFailure(call: Call<Overview>, t: Throwable) {
                    progressBar.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<Overview>,
                    response: Response<Overview>
                ) {
                    runOnUiThread {
                        val res = response.body();
                        val movie = res?.let {
                            TmdbResponse((entry.key as String).toInt(),
                                it.title,
                                formatter.parse(res.release_date),
                                res.vote_average,
                                res.poster_path,
                                res.backdrop_path
                            )
                        }
                        movie?.let { if (arrayFinal.indexOf(it)==-1) arrayFinal.add(it) }
                        if (!iterator.hasNext()) {
                            recyclerViewMore.layoutManager = GridLayoutManager(
                                this@FavoriteActivity,
                                3,
                                RecyclerView.VERTICAL,
                                false
                            )
                            recyclerViewMore?.adapter = ViewMoreAdapter(this@FavoriteActivity, arrayFinal)
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            })
        }
        }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    }
