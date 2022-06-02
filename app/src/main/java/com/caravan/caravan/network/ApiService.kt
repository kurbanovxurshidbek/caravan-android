package com.caravan.caravan.network

import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.Profile
import com.caravan.caravan.model.auth.LoginRespond
import com.caravan.caravan.model.auth.LoginSend
import com.caravan.caravan.model.auth.RegisterRespond
import com.caravan.caravan.model.auth.RegisterSend
import com.caravan.caravan.model.create_trip.FirstSend
import com.caravan.caravan.model.create_trip.PhotoRespond
import com.caravan.caravan.model.create_trip.SecondSend
import com.caravan.caravan.model.home.HomeRespond
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.model.upgrade.UpgradeSend
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Login
    @POST("/api/v1/login")
    suspend fun sendSmsCode(@Body loginSend: LoginSend): Response<ActionMessage>

    @PUT("/api/v1/check")
    suspend fun checkSmsCode(@Body loginSend: LoginSend): Response<LoginRespond>

    @POST("/api/v1/registration")
    suspend fun registerUser(@Body registerSend: RegisterSend): Response<RegisterRespond>

    // Profile
    @GET("/api/v1/profile/{profileId}")
    suspend fun getProfile(@Path("profileId") profileId: String): Response<Profile>

    @PUT("/api/v1/profile/{profileId}")
    suspend fun updateProfile(@Path("profileId") profileId: String, @Body profile: Profile): Profile

    @POST("/api/v1/guide")
    suspend fun upgradeToGuide(@Body upgradeSend: UpgradeSend): GuideProfile

    @GET("/api/v1/profile/language/{profileId}")
    suspend fun getAppLanguage(@Path("profileId") profileId: String): Response<String>

    @PUT("/api/v1/profile/language/{profileId}")
    suspend fun updateAppLanguage(@Path("profileId") profileId: String, @Query("lang") lang: String): Response<ActionMessage>

    @GET("/api/v1/home")
    suspend fun getHomeData(): Response<HomeRespond>

    // Guide Account
    @GET("/api/v1/guide/{guideId}")
    suspend fun getGuideProfile(@Path("guideId") guideId: String): Response<GuideProfile>

    @PUT("/api/v1/guide/{guideId}")
    suspend fun updateGuideProfile(@Path("guideId") guideId: String, @Body guideProfile: GuideProfile): Response<ActionMessage>

    @DELETE("/api/v1/guide/{guideId}")
    suspend fun deleteGuideProfile(@Path("guideId") guideId: String): Response<ActionMessage>

    @GET("/api/v1/guide/status/{guideId}")
    suspend fun getGuideStatus(@Path("guideId") guideId: String): Response<Boolean>

    @PUT("/api/v1/guide/status/{guideId}")
    suspend fun changeGuideStatus(@Path("guideId") guideId: String): Response<Boolean>

    // Trip
    @POST("/api/v1/trip/{guideId}")
    suspend fun createTrip(@Path("guideId") guideId: String, @Body firstSend: FirstSend): Response<String> // Trip Id

    // Customise for file
    @POST("/api/v1/photo/upload")
    suspend fun uploadPhoto(@Body file: Multipart): Response<PhotoRespond>

    @PUT("/api/v1/trip/finish/{tripId}")
    suspend fun completeTrip(@Path("tripId") tripId: String, secondSend: SecondSend): Response<ActionMessage>

}