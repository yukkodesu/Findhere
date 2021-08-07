package com.project.findhere.models

data class FoundPost(var user: User? = null,
                     var userid : String = "",
                     var time_ms : Long = 0,
                     var foundyear : String = "",
                     var foundmonth : String = "",
                     var foundday : String ="",
                     var imgurl : String = "",
                     var description : String = "")