package com.jnfran92.kotcoin.presentation.crypto.processor

import com.jnfran92.domain.crypto.usecase.GetCryptoListUseCase
import com.jnfran92.kotcoin.presentation.crypto.action.CryptoListAction
import com.jnfran92.kotcoin.presentation.crypto.mapper.DomainCryptoToUIMapper
import com.jnfran92.kotcoin.presentation.crypto.result.CryptoListResult
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import timber.log.Timber
import javax.inject.Inject

class CryptoListProcessor @Inject constructor(
    private val getCryptoListUseCase: GetCryptoListUseCase,
    private val domainCryptoToUIMapper: DomainCryptoToUIMapper): ObservableTransformer<CryptoListAction, CryptoListResult> {

//    val observableTransformer = ObservableTransformer<CryptoListAction, CryptoListResult> { actions ->
//        actions.flatMap { action ->
//            when(action){
//                CryptoListAction.GetCryptoList -> {
//                    getCryptoListUseCase.useCase
//                        .map (domainCryptoToUIMapper::transform)
//                        .toObservable()
//                        .map(CryptoListResult.GetCryptoListResult::OnSuccess)
//                        .cast(CryptoListResult::class.java)
//                        .startWith(CryptoListResult.GetCryptoListResult.InProgress)
//                        .onErrorReturn(CryptoListResult.GetCryptoListResult::OnError)
//                }
//            }
//        }
//    }

    override fun apply(upstream: Observable<CryptoListAction>): ObservableSource<CryptoListResult> {
        Timber.d("apply")
        return upstream.flatMap { action ->
            when(action){
                CryptoListAction.GetCryptoList -> {
                    getCryptoListUseCase.useCase
                        .map (domainCryptoToUIMapper::transform)
                        .toObservable()
                        .map(CryptoListResult.GetCryptoListResult::OnSuccess)
                        .cast(CryptoListResult::class.java)
                        .startWith(CryptoListResult.GetCryptoListResult.InProgress)
                        .onErrorReturn(CryptoListResult.GetCryptoListResult::OnError)
                }
            }
        }
    }
}