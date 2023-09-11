package com.aap.ro.movies.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity
@Fts4
data class ArtistSearch(@PrimaryKey
                        @ColumnInfo(name = "row_id") val rowId: Int, val name: String)
