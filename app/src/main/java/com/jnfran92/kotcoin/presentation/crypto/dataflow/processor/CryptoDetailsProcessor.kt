package com.jnfran92.kotcoin.presentation.crypto.dataflow.processor

import com.jnfran92.domain.crypto.GetCryptoDetailsUseCase
import com.jnfran92.kotcoin.presentation.crypto.dataflow.action.CryptoDetailsAction
import com.jnfran92.kotcoin.presentation.crypto.dataflow.result.CryptoDetailsResult
import com.jnfran92.kotcoin.crypto.presentation.mapper.DomainCryptoDetailsToUIMapper
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class CryptoDetailsProcessor @Inject constructor(
    private val getCryptoDetailsUseCase: GetCryptoDetailsUseCase,
    private val toUIMapper: DomainCryptoDetailsToUIMapper
) : ObservableTransformer<CryptoDetailsAction, CryptoDetailsResult> {

    override fun apply(upstream: Observable<CryptoDetailsAction>): ObservableSource<CryptoDetailsResult> {
        Timber.d("apply")
        return upstream.flatMap { action ->
            Timber.d("apply: action $action")
            when (action) {
                is CryptoDetailsAction.GetCryptoDetails -> buildGetCryptoDetailsUseCase(action.itemId)
            }
        }
    }


    private fun buildGetCryptoDetailsUseCase(itemId: Long): Observable<CryptoDetailsResult>? {
        return getCryptoDetailsUseCase(itemId)
            .map(toUIMapper::transform)
            .toObservable()
            .map(CryptoDetailsResult.GetCryptoDetailsResult::OnSuccess)
            .cast(CryptoDetailsResult::class.java)
            .startWith(CryptoDetailsResult.GetCryptoDetailsResult.InProgress)
            .onErrorReturn(CryptoDetailsResult.GetCryptoDetailsResult::OnError)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}