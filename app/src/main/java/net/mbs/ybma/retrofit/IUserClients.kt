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
    fun userLogin(
        @Field("key") key: String,
        @Field("phone") phone: String
    ): Call<UserResponse>

    @POST("users/register")
    @FormUrlEncoded
    fun userRegister(
        @Field("key") key: String,
        @Field("phone") phone: String,
        @Field("nom") nom: String,
        @Field("prenom") prenom: String,
        @Field("account_type") account_type: String,
        @Field("login_type") login_type: String,
        @Field("tonotify") tonotify: String
    ): Call<UserResponse>
}