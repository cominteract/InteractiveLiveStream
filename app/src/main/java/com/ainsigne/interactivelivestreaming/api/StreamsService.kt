package com.ainsigne.interactivelivestreaming.api;


import com.ainsigne.interactivelivestreaming.model.RTCToken
import com.ainsigne.interactivelivestreaming.model.StreamToken
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.model.Streams
import retrofit2.Response
import retrofit2.http.*


/**
 * [StreamsService] service for the api calls on the Streams API
 */
interface StreamsService {
    /**
     * gets the response to retrieve the logged user as [Response<StreamUser>]
     * @param userName as [String] the username of user
     * @return Response<StreamUser>
     */
    @GET("user")
    suspend fun getLogged(@Query("userName") ll: String) : Response<List<StreamUser>>

    /**
     * posts to register user as [Response<StreamUser>]
     * @param body as [StreamUser] the user to sign up
     * @return Response<StreamUser>
     */
    @POST("user")
    suspend fun signupUser(@Body body : StreamUser) : Response<StreamUser>

    /**
     * gets the response to retrieve the streams as [Response<List<Streams>>]
     * @return Response<List<Streams>>
     */
    @GET("stream")
    suspend fun getAllStreams() : Response<List<Streams>>

    /**
     * TODO : unused
     */
    @POST("rtc_token")
    suspend fun postRTCToken(@Body body : RTCToken) : Response<StreamToken>

    /**
     * posts to create a stream as [Response<Streams>]
     * @param body as [Streams] the stream to create
     * @return Response<Streams>
     */
    @POST("stream")
    suspend fun postStream(@Body body : Streams) : Response<Streams>


    /**
     * posts to create a stream as [Response<Streams>]
     * @param body as [StreamUser] the stream to create
     * @return Response<Streams>
     */
    @PUT("stream/{Id}")
    suspend fun updateStream(@Body body : Streams, @Path("Id") id : String) : Response<Streams>
}