package com.example.entregafundamentosandroid.data.model

data class Character (
    val id: String,
    val name: String,
    val photo: String,
    val description: String,
    val maxLife: Int = 100,
    val actualLife: Int = 100

        )