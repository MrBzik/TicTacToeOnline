package com.example.tictactoeonline.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoeonline.data.GameState
import com.example.tictactoeonline.data.MakeTurn
import com.example.tictactoeonline.data.Player
import com.example.tictactoeonline.data.RealtimeMessagingClient
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class TicTacToeViewModel @Inject constructor(
    private val client : RealtimeMessagingClient
) : ViewModel() {


    val playerName = MutableStateFlow("")

    fun onNameChanged(name : String){
        playerName.value = name
    }


    val isGameStarted = MutableStateFlow(false)


    fun startGame(){
        if(playerName.value.isNotBlank())
        isGameStarted.value = true
    }



    val state : StateFlow<GameState> by lazy {
        client.getGameStateFlow()
            .filter {
            if(it.readText() == "connected"){
                _isConnecting.value = false
                client.updateName(Player('x', playerName.value))
            }
            it.readText() != "connected" }
            .mapNotNull {
                Log.d("CHECKTAGS", "parsing : ${it.readText()}")
                Json.decodeFromString<GameState>(it.readText())
            }
        .catch { e -> _showConnectionError.value = e is ConnectException}
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GameState())
    }


    private val _isConnecting = MutableStateFlow(true)

    val isConnecting = _isConnecting.asStateFlow()

    private val _showConnectionError = MutableStateFlow(false)
    val showConnectionError = _showConnectionError.asStateFlow()


    fun finishTurn(x : Int, y : Int) {

        if(state.value.field[y][x] != null || state.value.winningPlayer != null)
            return

        viewModelScope.launch{
            client.sendAction(MakeTurn(x, y))
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            client.close()
        }
    }
}

