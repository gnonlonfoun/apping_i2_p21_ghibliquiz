package com.example.apping_i2_p21_ghibliquiz

data class CharacterItem(
    val id:String,
    val name:String,
    val gender:String,
    val age:String,
    val eye_color:String,
    val hair_color:String,
    val films:List<String>,
    val species:String,
    val url:String
)