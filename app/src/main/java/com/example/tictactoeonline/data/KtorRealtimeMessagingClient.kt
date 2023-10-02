package com.example.tictactoeonline.data

import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KtorRealtimeMessagingClient (
    private val client : HttpClient
        ) : RealtimeMessagingClient {

        private var session : WebSocketSession? = null


    override fun getGameStateFlow(): Flow<Frame.Text> {

        return flow {
            session = client.webSocketSession {
                url("ws://192.168.1.127:8080/play")
            }
            val frames = session!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()

            emitAll(frames)

        }
    }

    override suspend fun sendAction(action: MakeTurn) {

        session?.outgoing?.send(
            Frame.Text(
                "make_turn#${Json.encodeToString(action)}"
            )
        )
    }

    override suspend fun updateName(player: Player) {
        session?.outgoing?.send(
            Frame.Text(
                "name#${Json.encodeToString(player)}"
            )
        )
    }

    override suspend fun close() {
        session?.close()
        session = null
    }


}