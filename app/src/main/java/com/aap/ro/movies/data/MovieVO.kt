package com.aap.ro.movies.data

data class MovieVO(
    val id: Int,
    val name: String,
    val yearOfRelease: Int,
    val genre: List<Genre>,
    val directors: List<ArtistVO> = emptyList(),
    val actors: List<ArtistVO> = emptyList(),
    val thumbNail: String? = null,
    val moviePoster: String? = null
)


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
    COMEDY,
    WESTERN;

    override fun toString(): String {
        return this.name.replace('_', ' ')
    }
}