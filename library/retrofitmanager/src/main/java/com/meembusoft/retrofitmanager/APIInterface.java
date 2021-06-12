//package com.meembusoft.retrofitmanager;
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.http.Body;
//import retrofit2.http.GET;
//import retrofit2.http.POST;
//import retrofit2.http.Path;
//import com.meembusoft.iot.model.MAddDevice;
//import com.meembusoft.iot.model.MAddUserRoom;
//import com.meembusoft.iot.model.MParamAddDevice;
//import com.meembusoft.iot.model.MParamAddUserRoom;
//import com.meembusoft.iot.model.MParamUserLogin;
//import com.meembusoft.iot.model.MParamUserSignUp;
//import com.meembusoft.iot.model.MRooms;
//import com.meembusoft.iot.model.MUserRoom;
//import com.meembusoft.iot.model.MUserSignUp;
//
///**
// * @author Md. Rashadul Alam
// * Email: rashed.droid@gmail.com
// */
//public interface APIInterface {
//
////    @POST("user/sendVerificationCode")
////    Call<APIResponse> apiSendVerificationCode(@Body ParamSendVerificationCode paramSendVerificationCode);
////
//    @GET("rooms/lists/{user_id}")
//    Call<APIResponse<List<MRooms>>> apiGetAllRooms(@Path("user_id") String user_id);
//
//    @GET("devices/deviceLists/{user_id}")
//    Call<APIResponse<List<MUserRoom>>> apiGetRoomToDeviceList(@Path("user_id") String user_id);
//
//    @GET("rooms/deleteRoom/{id}")
//    Call<APIResponse> apiGetDeleteRoom(@Path("id") String id);
////
////    @GET("locations/lists")
////    Call<APIResponse<List<City>>> apiGetAllCitiesWithAreas();
//
////
//    @POST("rooms/add")
//    Call<APIResponse<List<MAddUserRoom>>> apiAddUserRoom(@Body MParamAddUserRoom paramAddUserRoom);
//
//    @POST("devices/add")
//    Call<APIResponse<List<MAddDevice>>> apiAddUserDevice(@Body MParamAddDevice paramAddDevice);
//
//    @POST("login/signup")
//    Call<APIResponse<List<MUserSignUp>>> apiUserLogin(@Body MParamUserLogin mParamUserLogin);
//
//    @POST("login/signup")
//    Call<APIResponse<List<MUserSignUp>>> apiUserSignUp(@Body MParamUserSignUp mParamUserSignUp);
////
////    @POST("kitchen/signup")
////    Call<APIResponse<List<KitchenUser>>> apiRequestForKitchenRegistration(@Body ParamKitchenRegistration paramKitchenRegistration);
////
////    @POST("kitchen/getByAreaAndCusine")
////    Call<APIResponse<List<Kitchen>>> apiGetAllKitchensBySearch(@Body ParamKitchenListBySearch paramKitchenListBySearch);
////
////    @POST("kitchen/getByUserLatLngAndCusine")
////    Call<APIResponse<List<Kitchen>>> apiGetAllKitchensByCuisine(@Body ParamKitchenListByCuisine paramKitchenListByCuisine);
////
////    @POST("kitchen/getByUserLatLngAndTime")
////    Call<APIResponse<List<Kitchen>>> apiGetAllKitchensByTime(@Body ParamKitchenListByTime paramKitchenListByTime);
////
////    @POST("kitchen/getByUserLatLngAndOffer")
////    Call<APIResponse<List<Kitchen>>> apiGetAllKitchensByOffer(@Body ParamKitchenListByOffer paramKitchenListByOffer);
////
////    @POST("kitchen/getByUserLatLngAndPopular")
////    Call<APIResponse<List<Kitchen>>> apiGetAllKitchensByPopular(@Body ParamKitchenListByPopular paramKitchenListByPopular);
////
////    @POST("kitchen/getByUserLatLngAndDeliveryTime")
////    Call<APIResponse<List<Kitchen>>> apiGetAllKitchensByDeliveryTime(@Body ParamKitchenListByFastDelivery paramKitchenListByFastDelivery);
////
////    @POST("kitchen/getByUserFavorites")
////    Call<APIResponse<List<Kitchen>>> apiGetAllKitchensByFavorite(@Body ParamKitchenListByFavorite paramKitchenListByFavorite);
////
////    @POST("food_item/add_review")
////    Call<APIResponse<List<Review>>> apiAddReview(@Body ParamReviewItem paramReviewItem);
////
////    @POST("food_item/do_favourite")
////    Call<APIResponse> apiDoFavouriteFoodItem(@Body ParamDoFavorite paramDoFavorite);
////
////    @POST("food_item/add")
////    Call<APIResponse<List<FoodItem>>> apiRequestForAddingFoodItemByKitchen(@Body ParamAddFoodItem paramAddFoodItem);
////
////    @POST("order/add")
////    Call<APIResponse<List<DoOrder>>> apiDoOrder(@Body ParamDoOrder order);
////
////    @POST("coupon_code/checkCouponCode")
////    Call<APIResponse> apiCheckPromoCode(@Body ParamPromoCode order);
////
////    @POST("drivers/login")
////    Call<APIResponse<List<DriverUser>>> apiDriverLogin(@Body ParamDriverLogin paramDriverLogin);
////
////    @POST("drivers/add")
////    Call<APIResponse<List<DriverUser>>> apiRequestForDriverRegistration(@Body ParamDriverRegistration paramDriverRegistration);
////
////    @POST("drivers/updateOrderStatus")
////    Call<APIResponse> apiUpdateOrderStatus(@Body ParamUpdateOrderStatus paramUpdateOrderStatus);
////
////    @POST("kitchen/logout")
////    Call<APIResponse> apiKitchenLogout(@Body ParamKitchenLogout paramKitchenLogout);
////
////    @POST("drivers/logout")
////    Call<APIResponse> apiDriverLogout(@Body ParamDriverLogout paramDriverLogout);
////
////    @POST("drivers/updateLatLng")
////    Call<APIResponse> apiUpdateDriverLocation(@Body ParamDriverLocationUpdate paramDriverLocationUpdate);
//}