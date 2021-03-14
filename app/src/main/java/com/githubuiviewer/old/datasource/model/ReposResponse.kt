package com.githubuiviewer.old.datasource.model

import com.google.gson.annotations.SerializedName

data class ReposResponse (
    @SerializedName("name") val name: String,
)