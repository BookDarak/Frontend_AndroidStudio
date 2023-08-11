package model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
    data class FindBookListDTO(
        @SerializedName("Title") val title : String = "",
        @SerializedName("Author") val author : String = "",
        @SerializedName("description") val description:String= "",
        @SerializedName("coverurl") val coverurl:String= "",



        ): Parcelable

