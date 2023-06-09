package fr.epf.movieenjoyer.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.movieenjoyer.Activities.Details
import fr.epf.movieenjoyer.Others.TmdbResponse
import fr.epf.movieenjoyer.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_more_layout.view.fav_button
import kotlinx.android.synthetic.main.view_more_layout.view.unfav_button
import kotlinx.android.synthetic.main.view_more_layout.view.*

class ViewMoreAdapter(val context: Context, private val arrayList: ArrayList<TmdbResponse>)
    : RecyclerView.Adapter<ViewMoreAdapter.APIViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): APIViewHolder {
        val inflater = LayoutInflater.from(context)
        return APIViewHolder(inflater.inflate(R.layout.view_more_layout, parent, false))
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: APIViewHolder, position: Int) {
        val user = arrayList[position]
        holder.bind(user, position)
    }

    inner class APIViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var currentuser: TmdbResponse? = null
        init {
            itemView.setOnClickListener {
                val detail= Intent(context, Details::class.java)
                detail.putExtra("ID", currentuser!!.id)
                context.startActivity(detail)
            }
        }

        fun bind(user: TmdbResponse, position: Int) {
            this.currentuser = user
            with(itemView) {
                name.text = user.title
                rating.text = "⭐ " + user.vote_average.toString() + "/10"
                Picasso.get().load("https://image.tmdb.org/t/p/w500" + user.poster_path)
                    .fit().centerCrop()
                    .into(imgViewMore)
                val sharedPreferences = context.getSharedPreferences("favorites",
                    Context.MODE_PRIVATE
                )
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