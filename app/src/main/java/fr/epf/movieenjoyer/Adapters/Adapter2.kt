package fr.epf.movieenjoyer.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.movieenjoyer.Activities.Details
import fr.epf.movieenjoyer.Others.TmdbResponse
import fr.epf.movieenjoyer.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.thincardlayout.view.*


class Adapter2(val context: Context, private val arrayList: ArrayList<TmdbResponse>)
    : RecyclerView.Adapter<Adapter2.GithubViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubViewHolder {
        val inflater = LayoutInflater.from(context)
        return GithubViewHolder(inflater.inflate(R.layout.thincardlayout, parent, false))
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: GithubViewHolder, position: Int) {
        val user = arrayList[position]
        holder.bind(user, position)
    }

    inner class GithubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentuser: TmdbResponse? = null
        private var currentposition = 0
        init {
            itemView.setOnClickListener {
                val detail= Intent(context, Details::class.java)
                detail.putExtra("ID", currentuser!!.id)
                context.startActivity(detail)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(user: TmdbResponse, position: Int) {
            this.currentuser = user
            this.currentposition = position
            with(itemView) {
                titlesearch.text = user.title
                ratingtv.text = "⭐ " + user.vote_average.toString() + "/10"
                Picasso.get().load("https://image.tmdb.org/t/p/w500" + user.poster_path).fit().centerCrop().into(img2)

                val sharedPreferences = context.getSharedPreferences("favorites", MODE_PRIVATE)
                var editor = sharedPreferences.edit()

                fav_button.setOnClickListener {
                    unfav_button.visibility = View.VISIBLE
                    fav_button.visibility = View.GONE
                    editor.putString(user.id.toString(), System.currentTimeMillis().toString())
                    editor.commit()
                }
                unfav_button.setOnClickListener {
                    unfav_button.visibility = View.GONE
                    fav_button.visibility = View.VISIBLE
                    editor.remove(user.id.toString())
                    editor.commit()
                }
                val allEntries: Map<String, *> = sharedPreferences.getAll()
                val arrayFav = arrayListOf<String>()

                for (entry in allEntries) {
                    arrayFav.add(entry.key as String)
                }
                if(arrayFav.indexOf(user.id.toString()) != -1){
                    unfav_button.visibility = View.VISIBLE
                    fav_button.visibility = View.GONE
                } else {
                    unfav_button.visibility = View.GONE
                    fav_button.visibility = View.VISIBLE
                }
            }
        }
    }

}
