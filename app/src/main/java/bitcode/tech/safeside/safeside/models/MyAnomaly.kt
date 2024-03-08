package bitcode.tech.safeside.safeside.models

import java.io.Serializable

data class MyAnomaly (
    val title: String,
    val countThumbsUp: Int,
    val countThumbsDown: Int,
    val reportedOn : String,
) : Serializable
