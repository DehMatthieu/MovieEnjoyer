package fr.epf.movieenjoyer.Others

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("{Id}/similar?api_key=8d10df48aa66174ddcec6bed18d3aa18&language=fr-FR&page=1")
    fun similarmovies(@Path("Id")id:Int): Call<TmdbList>


    @GET("{Id}?api_key=8d10df48aa66174ddcec6bed18d3aa18&language=fr-FR")
    fun overview(@Path("Id")id:Int):Call<Overview>

    @GET("search/movie?api_key=8d10df48aa66174ddcec6bed18d3aa18&language=fr-FR&query&page=1")
    fun search(@Query("query")q:String):Call<Searcharray>

    @GET("now_playing?api_key=8d10df48aa66174ddcec6bed18d3aa18&language=fr-FR&page=1")
    fun nowshowing(): Call<TmdbList>

    @GET("popular?api_key=8d10df48aa66174ddcec6bed18d3aa18&language=fr-FR&page=1")
    fun popularmovies(): Call<TmdbList>

    @GET("upcoming?api_key=8d10df48aa66174ddcec6bed18d3aa18&language=fr-FR&region=FR&page=1")
    fun upcoming(): Call<TmdbList>

    @GET("top_rated?api_key=8d10df48aa66174ddcec6bed18d3aa18&language=fr-FR&page=1")
    fun toprated(): Call<TmdbList>


}
