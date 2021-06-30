package model

import com.google.gson.annotations.SerializedName

data class Comments (

   @SerializedName("postId") var postId : Int,
   @SerializedName("email") var email : String,
   @SerializedName("name") var name : String,
   @SerializedName("body") var body : String,
   @SerializedName("id") var id : Int

)