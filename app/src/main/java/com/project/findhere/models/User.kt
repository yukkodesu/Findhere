package com.project.findhere.models

data class User(var username : String = "新用户",
                var stuid : String = "未设置",
                var grade : Int = 0,
                var avatarurl : String = "",
                var qq : String = "未设置",
                var phone : String = "未设置",
                var display_email :String = "未设置")