package com.example.tictactoeonline.data

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val char : Char, val name : String
)