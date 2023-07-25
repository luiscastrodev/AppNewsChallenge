package br.com.example.criticaltechworks.challenge.model

import br.com.example.criticaltechworks.challenge.data.entity.SourceEntity

data class SourceResponse(
    val sources: List<SourceEntity>,
    val status: String = "",
)