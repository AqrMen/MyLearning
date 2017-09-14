package com.aqrlei.graduation.yueting.model.local

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/14 Time: 14:42
 */
data class MusicInfo(
        var id: Int = 0,
        var albumUrl: String = "",
        var album: ByteArray? = null,
        var title: String = "",
        var artist: String = "",
        var duration: Int = 0,
        var createTime: String = ""
) : Comparable<MusicInfo>, Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.createByteArray(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString())


    override fun writeToParcel(out: Parcel?, flags: Int) {
        out?.writeString(albumUrl)
        out?.writeByteArray(album)
        out?.writeString(title)
        out?.writeString(createTime)
        out?.writeInt(id)
        out?.writeInt(duration)
    }


    override fun describeContents(): Int = 0


    override fun compareTo(other: MusicInfo): Int {
        val thisArtist = this.artist.toLowerCase()
        val otherArtist = other.artist.toLowerCase()
        if (otherArtist >= "一" || thisArtist >= "一") {
            if (thisArtist < otherArtist)
                return 1
            if (thisArtist > otherArtist)
                return -1
            return 0
        } else {
            if (thisArtist < otherArtist)
                return -1
            if (thisArtist > otherArtist)
                return 1
            return 0
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MusicInfo

        if (id != other.id) return false
        /*if (albumUrl != other.albumUrl) return false
        if (!Arrays.equals(album, other.album)) return false
        if (title != other.title) return false
        if (artist != other.artist) return false
        if (duration != other.duration) return false
        if (createTime != other.createTime) return false*/
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + albumUrl.hashCode()
        result = 31 * result + (album?.let { Arrays.hashCode(it) } ?: 0)
        result = 31 * result + title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + duration
        result = 31 * result + createTime.hashCode()
        return result
    }

    companion object CREATOR : Parcelable.Creator<MusicInfo> {
        override fun createFromParcel(parcel: Parcel): MusicInfo =
                MusicInfo(parcel)

        override fun newArray(size: Int): Array<MusicInfo?> =
                arrayOfNulls(size)

    }
}