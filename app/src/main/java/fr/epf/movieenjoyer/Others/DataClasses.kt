package fr.epf.movieenjoyer.Others


data class Searchdetails(
    val title:String,
    val name: String,
    val first_air_date: String,
    val poster_path:String,
    val overview: String,
    val release_date:String,
    val id:Int
)
data class Genres(
    val name: String
)

data class Searcharray(val results:ArrayList<Searchdetails>)

data class Overview(
    val original_title:String,
    val overview:String,
    val first_air_date:String,
    val status:String,
    val release_date:String,
    val runtime:Int,
    val poster_path:String,
    val vote_average:Float,
    val backdrop_path:String,
    val title:String,
    val genres:ArrayList<Genres>,
    val id:Int
)