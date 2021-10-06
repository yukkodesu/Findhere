package com.project.findhere.models

data class FindPost(var user: User? = null,
                    var userid : String = "",
                    var time_ms : Long = 0,
                    var lostdate : String ="无时间",
                    var imgurl : String = "无图片",
                    var place : String = "无地点",
                    var description : String = "无描述")