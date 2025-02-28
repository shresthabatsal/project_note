package com.example.note.model

import android.os.Parcel
import android.os.Parcelable

data class Note(
    var id: String = "",
    val userId: String = "",
    var title: String = "",
    var content: String = "",
    var isFavorite: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userId)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note = Note(parcel)
        override fun newArray(size: Int): Array<Note?> = arrayOfNulls(size)
    }
}
