package net.mbs.ybma.retrofit

import io.reactivex.Observable
import net.mbs.ybma.models.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface IUserClients {
    @POST("login")
    @FormUrlEncoded
    fun  UserLogin(@Field("key") key:String,
                        @Field("phone") phone:String): Observable<User>
}