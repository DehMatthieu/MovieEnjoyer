package fr.epf.movieenjoyer.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import fr.epf.movieenjoyer.*
import fr.epf.movieenjoyer.Adapters.Adapter
import fr.epf.movieenjoyer.Adapters.Adapter2
import fr.epf.movieenjoyer.Others.APIService
import fr.epf.movieenjoyer.Others.TmdbList
import kotlinx.android.synthetic.main.activity_launcher.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar_home.*
import kotlinx.android.synthetic.main.largecardlayout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity(){
    private val retrofitClient = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/movie/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofitClient.create(APIService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        setSupportActionBar(toolbar1)
        service.nowshowing().enqueue(object : Callback<TmdbList> {
            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<TmdbList>, t: Throwable) {
                textviewmore.text="Erreur lors du chargement, veuillez réessayer"
            }
            override fun onResponse(
                call: Call<TmdbList>,
                response: Response<TmdbList>
            ) {
                runOnUiThread {
                    recycler1.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL,false)
                    recycler1.adapter =
                            Adapter(this@HomeActivity, response.body()!!.results)
                    val snapItemHelper = PagerSnapHelper()
                    snapItemHelper.attachToRecyclerView(recycler1)
                    progressBar.visibility = View.GONE
                }
            }
        })

        service.popularmovies().enqueue(object : Callback<TmdbList> {
            override fun onFailure(call: Call<TmdbList>, t: Throwable) {
                textviewmore.text="Erreur lors du chargement, veuillez réessayer"
            }
            override fun onResponse(
                call: Call<TmdbList>,
                response: Response<TmdbList>
            ) {
                runOnUiThread {
                    recycler2.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL,false)
                    recycler2.adapter =
                            Adapter2(this@HomeActivity, response.body()!!.results)

                }
            }
        })
        service.upcoming().enqueue(object : Callback<TmdbList> {
            override fun onFailure(call: Call<TmdbList>, t: Throwable) {
                textviewmore.text="Erreur lors du chargement, veuillez réessayer"
            }
            override fun onResponse(
                call: Call<TmdbList>,
                response: Response<TmdbList>
            ) {
                runOnUiThread {
                    recycler3.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL,false)
                    recycler3.adapter =
                            Adapter(this@HomeActivity, response.body()!!.results)
                    val snapItemHelper = PagerSnapHelper()
                    snapItemHelper.attachToRecyclerView(recycler3)
                }
            }
        })
        service.toprated().enqueue(object : Callback<TmdbList> {
            override fun onFailure(call: Call<TmdbList>, t: Throwable) {
                textviewmore.text="Erreur lors du chargement, veuillez réessayer"
            }
            override fun onResponse(
                call: Call<TmdbList>,
                response: Response<TmdbList>
            ) {
                runOnUiThread {
                    recycler4.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL,false)
                    recycler4.adapter =
                            Adapter2(this@HomeActivity, response.body()!!.results)
                }
            }
        })

        viewMore1.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ViewMore::class.java).putExtra("category","nowshowing"))
        }
        viewMore2.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ViewMore::class.java).putExtra("category","popular"))
        }
        viewMore3.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ViewMore::class.java).putExtra("category","upcoming"))
        }
        viewMore4.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ViewMore::class.java).putExtra("category","toprated"))
        }



    }
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.clear();
        menuInflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView : SearchView = MenuItemCompat.getActionView(searchItem) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val a = Intent(this@HomeActivity, Search::class.java)
                a.putExtra("Recherche", query)
                if (query != "") {
                    startActivity(a)
                } else {
                    Toast.makeText(this@HomeActivity, "Veuillez écrire le contenu de votre recherche", Toast.LENGTH_SHORT)
                        .show()
                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.getItemId() == R.id.app_bar_favorite) {
            startActivity(Intent(this, FavoriteActivity::class.java))
        }
        return false
    }

}
