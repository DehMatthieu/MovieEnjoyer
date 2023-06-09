package fr.epf.movieenjoyer.Activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.movieenjoyer.Others.APIService
import fr.epf.movieenjoyer.R
import fr.epf.movieenjoyer.Adapters.SearchAdapter
import fr.epf.movieenjoyer.Others.Searcharray
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@SuppressLint("Registered")
class Search : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbarsearch)
        supportActionBar?.title = "Résultats"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val text=intent.getStringExtra("Recherche")
        val retrofitClient = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofitClient.create(APIService::class.java)
        service.search(text).enqueue(object : Callback<Searcharray> {
            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<Searcharray>, t: Throwable) {
                textviewmore.text = "Erreur lors du chargement, veuillez réessayer"
            }
            override fun onResponse(
                call: Call<Searcharray>,
                response: Response<Searcharray>
            ) {
                runOnUiThread {
                    if(response.body()!!.results.isEmpty()){
                        nothing.visibility=View.VISIBLE
                    }
                    recycler1.layoutManager = LinearLayoutManager(this@Search, RecyclerView.VERTICAL,false)
                    recycler1.adapter = SearchAdapter(this@Search, response.body()!!.results)
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}