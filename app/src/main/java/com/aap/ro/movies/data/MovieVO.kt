package com.aap.ro.movies.data

import androidx.annotation.StringRes

data class MovieVO(
                 val id: Int,
                 val name: String,
                 val yearOfRelease: Int,
                 val genre: List<Genre>,
                 val directors: List<Artist>,
                 val actors: List<Artist>)


enum class Genre {
    ACTION,
    THRILLER,
    DRAMA,
    WAR,
    FANTASY,
    ROMANCE,
    ADVENTURE,
    SCI_FI,
    FAMILY,
    CRIME,
    COMEDY
}