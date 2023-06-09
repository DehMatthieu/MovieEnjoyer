package fr.epf.movieenjoyer.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.movieenjoyer.Activities.Details
import fr.epf.movieenjoyer.R
import fr.epf.movieenjoyer.Others.TmdbResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.largecardlayout.view.*
import java.util.*

class Adapter(val context: Context, private val arrayList: ArrayList<TmdbResponse>)
    : RecyclerView.Adapter<Adapter.APIViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): APIViewHolder {
        val inflater = LayoutInflater.from(context)
        return APIViewHolder(inflater.inflate(R.layout.largecardlayout, parent, false))

    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: APIViewHolder, position: Int) {
        val user = arrayList[position]
        holder.bind(user, position)
    }

    inner class APIViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentuser: TmdbResponse? = null
        private var currentposition = 0

        init {
            itemView.setOnClickListener {
                val detail= Intent(context, Details::class.java)
                detail.putExtra("ID", currentuser!!.id)
                context.startActivity(detail)
            }
        }

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(user: TmdbResponse, position: Int) {
            this.currentuser = user
            this.currentposition = position
            with(itemView) {
                titlesearch.text = user.title
                ratingtv.text = "⭐ " + user.vote_average.toString() + "/10"
                Picasso.get().load("https://image.tmdb.org/t/p/original" + user.backdrop_path).fit().centerCrop().into(img)

                if(user.backdrop_path == null){
                    Picasso.get().load("https://fasterthemes.com/demo/foodrecipespro-wordpress-theme/wp-content/themes/foodrecipespro/images/no-image.jpg").fit().centerCrop().into(img)
                }

                val sharedPreferences = context.getSharedPreferences("favorites",
                    Context.MODE_PRIVATE
                )
                var editor = sharedPreferences.edit()
                fav_button2.setOnClickListener {
                    unfav_button2.visibility = View.VISIBLE
                    fav_button2.visibility = View.GONE
                    editor.putString(user.id.toString(), System.currentTimeMillis().toString())
                    editor.commit()
                }
                unfav_button2.setOnClickListener {
                    unfav_button2.visibility = View.GONE
                    fav_button2.visibility = View.VISIBLE
                    editor.remove(user.id.toString())
                    editor.commit()
                }

                val allEntries: Map<String, *> = sharedPreferences.getAll()
                val arrayFav = arrayListOf<String>()

                for (entry in allEntries) {
                    arrayFav.add(entry.key as String)
                }
                if(arrayFav.indexOf(user.id.toString()) != -1){
                    unfav_button2.visibility = View.VISIBLE
                    fav_button2.visibility = View.GONE
                } else {
                    unfav_button2.visibility = View.GONE
                    fav_button2.visibility = View.VISIBLE
                }
            }
        }

    }
}