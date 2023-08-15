package model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
    data class FindBookListDTO(
        @SerializedName("title") val title : String = "",
        @SerializedName("authors") val authors:Array<String>,
        @SerializedName("contents") val contents:String= "",
        @SerializedName("thumbnail") val thumbnail:String= "",



        ): Parcelable

