package com.example.tictactoeonline.dagger

import com.example.tictactoeonline.data.KtorRealtimeMessagingClient
import com.example.tictactoeonline.data.RealtimeMessagingClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.websocket.WebSockets
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideHttpClient() =
        HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
        }


    @Singleton
    @Provides
    fun provideKtorClient(httpClient: HttpClient) : RealtimeMessagingClient =
        KtorRealtimeMessagingClient(httpClient)



}