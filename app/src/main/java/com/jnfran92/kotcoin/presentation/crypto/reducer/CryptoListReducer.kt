package com.jnfran92.kotcoin.presentation.crypto.reducer

import com.jnfran92.kotcoin.presentation.crypto.result.CryptoListResult
import com.jnfran92.kotcoin.presentation.crypto.uistate.CryptoListUIState
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class CryptoListReducer @Inject constructor() {

    val reducer = BiFunction(::resolveNextState)

    private fun resolveNextState(previousState: CryptoListUIState, currentResult: CryptoListResult): CryptoListUIState {
        return when(previousState){
            is CryptoListUIState.ShowDefaultView -> previousState resolveWith currentResult
            is CryptoListUIState.ShowLoadingView -> previousState resolveWith currentResult
            is CryptoListUIState.ShowErrorRetryView -> previousState resolveWith currentResult
            is CryptoListUIState.ShowDataView -> previousState resolveWith currentResult
        }
    }

    private infix fun CryptoListUIState.ShowDefaultView.resolveWith(result: CryptoListResult): CryptoListUIState {
        return when(result){
            CryptoListResult.GetCryptoListResult.InProgress -> CryptoListUIState.ShowLoadingView
            is CryptoListResult.GetCryptoListResult.OnError -> throw Exception("invalid path")
            is CryptoListResult.GetCryptoListResult.OnSuccess -> throw Exception("invalid path")
        }
    }

    private infix fun CryptoListUIState.ShowLoadingView.resolveWith(result: CryptoListResult): CryptoListUIState {
        return when(result){
            CryptoListResult.GetCryptoListResult.InProgress -> throw Exception("invalid path")
            is CryptoListResult.GetCryptoListResult.OnError -> CryptoListUIState.ShowErrorRetryView
            is CryptoListResult.GetCryptoListResult.OnSuccess -> CryptoListUIState.ShowDataView(result.data)
        }
    }

    private infix fun CryptoListUIState.ShowErrorRetryView.resolveWith(result: CryptoListResult): CryptoListUIState {
        return when(result){
            CryptoListResult.GetCryptoListResult.InProgress -> throw Exception("invalid path")
            is CryptoListResult.GetCryptoListResult.OnError -> throw Exception("invalid path")
            is CryptoListResult.GetCryptoListResult.OnSuccess -> throw Exception("invalid path")
        }
    }

    private infix fun CryptoListUIState.ShowDataView.resolveWith(result: CryptoListResult): CryptoListUIState {
        return when(result){
            CryptoListResult.GetCryptoListResult.InProgress -> throw Exception("invalid path")
            is CryptoListResult.GetCryptoListResult.OnError -> throw Exception("invalid path")
            is CryptoListResult.GetCryptoListResult.OnSuccess -> throw Exception("invalid path")
        }
    }

}