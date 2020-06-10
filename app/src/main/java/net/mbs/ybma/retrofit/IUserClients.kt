package net.mbs.ybma.retrofit

import io.reactivex.Observable
import net.mbs.ybma.models.User
import net.mbs.ybma.retrofit.response.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface IUserClients {
    @POST("users/login")
    @FormUrlEncoded
    fun  userLogin(@Field("key") key:String,
                        @Field("phone") phone:String):Call<UserResponse>
}