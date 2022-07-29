package com.example.reflexgame.model

data class ReflexUiModel(val reflexUiButtonList: List<List<ReflexUiButton>>)

data class ReflexUiButton(var isValidTap: Boolean = false)