package com.project.findhere.models

data class FoundPost(
    var user: User? = null,
    var userid: String= "",
    var time_ms: Long = 0,
    var founddate: String ="",
    var imgurl: String = "",
    var place: String = "",
    var description: String = "")