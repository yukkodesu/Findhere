package com.project.findhere.models

data class FindPost(var user: User? = null,
                    var userid : String = "",
                    var time_ms : Long = 0,
                    var lostyear : String = "",
                    var lostmonth : String = "",
                    var lostday : String ="",
                    var imgurl : String = "",
                    var description : String = "")