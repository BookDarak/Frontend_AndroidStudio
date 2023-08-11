package model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class FindBookDTO(
    @SerializedName("title") val title:String,
    @SerializedName("item") val books: List<Book>,


    )


