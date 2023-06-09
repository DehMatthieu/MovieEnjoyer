package fr.epf.movieenjoyer.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.movieenjoyer.Adapters.ViewMoreAdapter
import fr.epf.movieenjoyer.Others.APIService
import fr.epf.movieenjoyer.Others.TmdbList
import fr.epf.movieenjoyer.R
import kotlinx.android.synthetic.main.activity_view_more.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewMore : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_more)

        setSupportActionBar(toolbarViewMore)
        supportActionBar?.title = "Movies"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val retrofitClient = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofitClient.create(APIService::class.java)

        val p = intent.getStringExtra("category")

        when(p) {
            "nowshowing" ->
                service.nowshowing().enqueue(object : Callback<TmdbList> {
                    override fun onFailure(call: Call<TmdbList>, t: Throwable) {
                        //tv.text="Loading failed!"
                        progressBar.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<TmdbList>,
                        response: Response<TmdbList>
                    ) {
                        runOnUiThread {
                            recyclerViewMore.layoutManager = GridLayoutManager(this@ViewMore, 3, RecyclerView.VERTICAL, false)
                            recyclerViewMore?.adapter = ViewMoreAdapter(this@ViewMore, response.body()!!.results)
                            progressBar.visibility = View.GONE
                        }
                    }
                })

            "popular" ->
                service.popularmovies().enqueue(object : Callback<TmdbList> {
                    override fun onFailure(call: Call<TmdbList>, t: Throwable) {
                        progressBar.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<TmdbList>,
                        response: Response<TmdbList>
                    ) {
                        runOnUiThread {
                            recyclerViewMore.layoutManager = GridLayoutManager(this@ViewMore, 3, RecyclerView.VERTICAL, false)
                            recyclerViewMore?.adapter = ViewMoreAdapter(this@ViewMore, response.body()!!.results)
                            progressBar.visibility = View.GONE
                        }
                    }
                })

            "upcoming" ->
                service.upcoming().enqueue(object : Callback<TmdbList> {
                    override fun onFailure(call: Call<TmdbList>, t: Throwable) {
                        progressBar.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<TmdbList>,
                        response: Response<TmdbList>
                    ) {
                        runOnUiThread {
                            recyclerViewMore.layoutManager = GridLayoutManager(this@ViewMore, 3, RecyclerView.VERTICAL, false)
                            recyclerViewMore?.adapter = ViewMoreAdapter(this@ViewMore, response.body()!!.results)
                            progressBar.visibility = View.GONE
                        }
                    }
                })

            "toprated" ->
                service.toprated().enqueue(object : Callback<TmdbList> {
                    override fun onFailure(call: Call<TmdbList>, t: Throwable) {
                        progressBar.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<TmdbList>,
                        response: Response<TmdbList>
                    ) {
                        runOnUiThread {
                            recyclerViewMore.layoutManager = GridLayoutManager(this@ViewMore, 3, RecyclerView.VERTICAL, false)
                            recyclerViewMore?.adapter = ViewMoreAdapter(this@ViewMore, response.body()!!.results)
                            progressBar.visibility = View.GONE
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
