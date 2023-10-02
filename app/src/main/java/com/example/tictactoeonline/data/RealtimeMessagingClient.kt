package com.example.tictactoeonline.data

import io.ktor.http.cio.websocket.Frame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface RealtimeMessagingClient {

    fun getGameStateFlow() : Flow<Frame.Text>

    suspend fun sendAction(action : MakeTurn)

    suspend fun updateName(player : Player)

    suspend fun close()

}