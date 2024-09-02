package com.example.myplaylist.player.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long?,
    val artworkUrl100: String,
    val previewUrl: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val addedTimestamp: Long?

) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong().takeIf { it != 0L },
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong().takeIf { it != 0L }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackId)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeLong(trackTimeMillis ?: 0L)
        parcel.writeString(artworkUrl100)
        parcel.writeString(previewUrl)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
        parcel.writeString(primaryGenreName)
        parcel.writeString(country)
        parcel.writeLong(addedTimestamp ?: 0L)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {

        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }

        fun fromString(trackString: String): Track {
            val parts = trackString.split("|")

            return Track(
                trackId = parts.getOrNull(0) ?: "",
                trackName = parts.getOrNull(1) ?: "Unknown",
                artistName = parts.getOrNull(2) ?: "Unknown",
                trackTimeMillis = parts.getOrNull(3)?.toLongOrNull(),
                artworkUrl100 = parts.getOrNull(4) ?: "",
                previewUrl = parts.getOrNull(5) ?: "",
                collectionName = parts.getOrNull(6) ?: "",
                releaseDate = parts.getOrNull(7) ?: "",
                primaryGenreName = parts.getOrNull(8) ?: "",
                country = parts.getOrNull(9) ?: "",
                addedTimestamp = parts.getOrNull(10)?.toLongOrNull()
            )
        }
    }
}