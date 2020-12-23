package com.jnfran92.kotcoin.presentation.crypto

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jnfran92.kotcoin.presentation.crypto.dataflow.intent.CryptoDetailsIntent
import com.jnfran92.kotcoin.presentation.crypto.dataflow.interpreter.CryptoDetailsInterpreter
import com.jnfran92.kotcoin.presentation.crypto.dataflow.processor.CryptoDetailsProcessor
import com.jnfran92.kotcoin.presentation.crypto.dataflow.reducer.CryptoDetailsReducer
import com.jnfran92.kotcoin.presentation.crypto.dataflow.uistate.CryptoDetailsUIState
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@FragmentScoped
class CryptoDetailsViewModel @ViewModelInject constructor(
    private val processor: CryptoDetailsProcessor,
    private val interpreter: CryptoDetailsInterpreter,
    private val reducer: CryptoDetailsReducer): ViewModel() {

    /**
     * RxJava Disposable
     */
    private val compositeDisposable = CompositeDisposable()

    /**
     * input parameter
     */
    var itemId: Int? = null

    /**
     * TX: transmit UI events
     */
    val tx: MutableLiveData<CryptoDetailsUIState> = MutableLiveData()

    /**
     * RX: receive User intents
     */
    fun rx(intent: CryptoDetailsIntent){
        Timber.d("rx: $intent")
        interpreter.processIntent(intent)
    }

    init {
        initDataFlow()
    }

    private fun initDataFlow() {
        Timber.d("initDataFlow: ")
        compositeDisposable.add(
            interpreter
                .toObservable()
                .compose(processor)
                .scan(CryptoDetailsUIState.ShowDefaultView, reducer)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(tx::postValue) { Timber.d("initDataFlow:  error $it") }
        )
        // lazy init
        val defaultParam = -1
        rx(CryptoDetailsIntent.GetCryptoDetailsIntent(itemId ?: defaultParam))
    }

    override fun onCleared() {
        Timber.d("onCleared: ")
        super.onCleared()
        compositeDisposable.dispose()
    }
}
