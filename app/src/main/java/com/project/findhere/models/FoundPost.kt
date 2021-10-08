package com.project.findhere.models

data class FoundPost(
    var user: User? = null,
    var userid: String= "",
    var time_ms: Long = 0,
    var founddate: String ="无时间",
    var name : String = "无名称",
    var imgurl: String = "无图片",
    var place: String = "无地点",
    var description: String = "无描述")