/*
 * Copyright (c) 2019 Kakadu Development
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bz.kakadu.sociallogin

import android.app.Activity
import android.content.Intent
import android.util.Log
import bz.kakadu.sociallogin.SocialLogin.LoginType.FB
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import android.os.Bundle
import org.json.JSONException
import com.facebook.GraphRequest
import com.facebook.AccessToken


internal class FacebookModule(override val listener: ILoginListener) : ISocialModule {
    private val fbCallbackManager = CallbackManager.Factory.create()
    private val fbCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(loginResult: LoginResult) {
            handleFbLoginToken(loginResult.accessToken)
            Log.d("loginResult", "loginResult: " + loginResult.toString())

            Log.d("loginResult", "loginResult: " + "onSuccess" + "--------" + loginResult.accessToken)
            Log.d("loginResult", "loginResult: " + "Token" + "--------" + loginResult.accessToken.token)
            Log.d("loginResult", "loginResult: " + "Permision" + "--------" + loginResult.recentlyGrantedPermissions)
            val profile = Profile.getCurrentProfile()
            Log.d("loginResult", "loginResult: " + "profile" + "--------" + profile)

            getGraphLoginData(loginResult.accessToken)
        }

        override fun onCancel() {
            listener.onLoginResult(null)
        }

        override fun onError(error: FacebookException) {
            listener.onLoginResult(
                    LoginResult(
                            false,
                            FB,
                            errorMessage = error.localizedMessage
                    )
            )
        }
    }

    // Method to access Facebook User Data.
    protected fun getGraphLoginData(accessToken: AccessToken) {
        val graphRequest = GraphRequest.newMeRequest(accessToken
        ) { jsonObject, graphResponse ->
            try {
                Log.d("loginResult", "loginResult>>GraphLoginRequest: " + jsonObject.toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        val bundle = Bundle()
        bundle.putString(
                "fields",
                "id,name,link,email,birthday,gender,location,last_name,first_name,locale,timezone,updated_time,verified"
        )
        graphRequest.parameters = bundle
        graphRequest.executeAsync()

    }

    override fun login(activity: Activity) {
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            handleFbLoginToken(accessToken)
            getGraphLoginData(accessToken)
        } else {
            LoginManager.getInstance().registerCallback(fbCallbackManager, fbCallback)
            LoginManager.getInstance()
//                .logInWithReadPermissions(activity, listOf("public_profile", "email"))
                    .logInWithReadPermissions(activity, listOf("email", "user_birthday", "user_photos", "user_gender", "public_profile"))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return fbCallbackManager.onActivityResult(
                requestCode,
                resultCode,
                data
        )
    }

    private fun handleFbLoginToken(token: AccessToken) {
        listener.onLoginResult(
                LoginResult(
                        true,
                        FB,
                        id = token.userId,
                        token = token.token
                )
        )
    }

    companion object {
        fun logout() {
            LoginManager.getInstance().logOut()
        }
    }
}
