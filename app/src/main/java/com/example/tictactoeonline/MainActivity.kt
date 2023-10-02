package com.example.tictactoeonline

import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tictactoeonline.presentation.DrawBoard
import com.example.tictactoeonline.presentation.TicTacToeViewModel
import com.example.tictactoeonline.ui.theme.TicTacToeOnlineTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeOnlineTheme {

                val gameViewModel = hiltViewModel<TicTacToeViewModel>()

                val isGameStarted by gameViewModel.isGameStarted.collectAsState()

                val nameField by gameViewModel.playerName.collectAsState()

                if(!isGameStarted){

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 30.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )  {

                        OutlinedTextField(
                            value = nameField,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = {
                                Text(text = "Your name")
                            },
                            onValueChange = (gameViewModel::onNameChanged)
                        )

                        Spacer(modifier = Modifier.size(30.dp))

                        Button(onClick =
                            gameViewModel::startGame
                        ) {

                            Text(
                                text = "Accept"
                            )

                        }
                    }
                } else {

                    val isConnecting by gameViewModel.isConnecting.collectAsState()
                    val gameState by gameViewModel.state.collectAsState()
                    val showConnectionError by gameViewModel.showConnectionError.collectAsState()

                    if(showConnectionError){
                        Box(modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        )  {
                            Text(
                                text = "Connection fail",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 20.sp
                            )
                        }

                    } else {

                        Box(modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {


                            Column(modifier = Modifier
                                .padding(32.dp)
                                .align(Alignment.TopCenter)
                            ) {

//                                if(!gameState.connectedPlayers.contains('X'))
//                                    Text(text = "Waiting for player X",
//                                        fontSize = 20.sp)
//
//                                else if (!gameState.connectedPlayers.contains('O'))
//                                    Text(text = "Waiting for player O",
//                                        fontSize = 20.sp)


                                if(gameState.connectedPlayers.size < 2){
                                    Text(text = "Waiting for players",
                                        fontSize = 20.sp)
                                }

                                else if(gameState.winningPlayer != null) {

                                    Text(text = "${gameState.winningPlayer?.name} wins")

                                }  else if(gameState.isBoardFull) {
                                    Text(text = "Tie")
                                } else {
                                    Text(text = "${gameState.playerAtTurn?.name} turn")
                                }


                                DrawBoard(state = gameState, onTapField = gameViewModel::finishTurn,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .padding(16.dp)
                                )

                                if(isConnecting){
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

