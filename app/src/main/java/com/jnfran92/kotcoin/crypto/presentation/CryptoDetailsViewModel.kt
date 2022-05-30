package com.jnfran92.kotcoin.crypto.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jnfran92.kotcoin.presentation.crypto.dataflow.intent.CryptoDetailsIntent
import com.jnfran92.kotcoin.presentation.crypto.dataflow.interpreter.CryptoDetailsInterpreter
import com.jnfran92.kotcoin.presentation.crypto.dataflow.processor.CryptoDetailsProcessor
import com.jnfran92.kotcoin.presentation.crypto.dataflow.reducer.CryptoDetailsReducer
import com.jnfran92.kotcoin.presentation.crypto.dataflow.result.CryptoDetailsResult
import com.jnfran92.kotcoin.crypto.presentation.uistate.CryptoDetailsUIState
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * View Model for handling UI interactions and present data of Crypto detail object.
 * MVI formatted! data flow
 */
@FragmentScoped
class CryptoDetailsViewModel @ViewModelInject constructor(
    private val processor: CryptoDetailsProcessor,
    private val interpreter: CryptoDetailsInterpreter,
    private val reducer: CryptoDetailsReducer
) : ViewModel() {

    /**
     * RxJava Disposable
     */
    private val compositeDisposable = CompositeDisposable()

    /**
     * TX: transmit UI events
     */
    val tx: MutableLiveData<CryptoDetailsUIState> = MutableLiveData()

    /**
     * RX: receive User intents
     */
    fun rx(intent: CryptoDetailsIntent) {
        Timber.d("rx: $intent")
        interpreter.processIntent(intent)
    }

    init {
        initDataFlow()
    }

    private fun initDataFlow() {
        Timber.d("initDataFlow: ")
        val dataFlow = interpreter flowTo processor flowTo reducer flowOn Schedulers.io()
        compositeDisposable += dataFlow.subscribe(tx::postValue) { Timber.d("initDataFlow: error $it") }
    }

    override fun onCleared() {
        Timber.d("onCleared: ")
        super.onCleared()
        compositeDisposable.dispose()
    }


    /**
     * infix helper: interpreter to processor
     */
    private infix fun CryptoDetailsInterpreter.flowTo(processor: CryptoDetailsProcessor): Observable<CryptoDetailsResult> {
        return this.toObservable().compose(processor)
    }

    /**
     * infix helper: processor to reducer
     */
    private infix fun Observable<CryptoDetailsResult>.flowTo(reducer: CryptoDetailsReducer): Observable<CryptoDetailsUIState> {
        return this.scan(CryptoDetailsUIState.ShowDefaultView, reducer)
    }

    /**
     * infix helper: processor to reducer
     */
    private infix fun Observable<CryptoDetailsUIState>.flowOn(scheduler: Scheduler):
            Observable<CryptoDetailsUIState> {
        return this.observeOn(scheduler).subscribeOn(scheduler)
    }

}
