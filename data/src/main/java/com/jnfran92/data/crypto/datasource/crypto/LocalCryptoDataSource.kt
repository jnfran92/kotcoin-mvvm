package com.jnfran92.data.crypto.datasource.crypto

import com.jnfran92.data.crypto.model.crypto.Crypto
import com.jnfran92.data.crypto.model.crypto.Currency
import com.jnfran92.data.crypto.model.crypto.Quote
import com.jnfran92.data.crypto.model.crypto.local.CryptoLocal
import com.jnfran92.data.crypto.model.crypto.local.CryptoWithHistoricUsdPrice
import com.jnfran92.data.crypto.model.crypto.local.UsdPrice
import com.jnfran92.data.crypto.supplier.crypto.local.CryptoDao
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Single
import io.reactivex.internal.operators.completable.CompletableDefer
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class LocalCryptoDataSource(private val cryptoDao: CryptoDao) : CryptoDataSource {

    override fun getCryptoById(cryptoId: Long): Single<Crypto> {
        Timber.d("getCryptoById")
        throw NotImplementedError()
    }

    override fun getCryptoList(): Single<List<Crypto>> {
        Timber.d("getCryptoList")
        return Single.create { emitter ->
            val results = this.cryptoDao.getAllCrypto()
            Timber.d("getCryptoList: results $results")
            val mappedResults = results.map{ cryptoLocal ->
                val cryptoPrices = cryptoDao
                    .getUsdPricesByCryptoId(cryptoLocal.cryptoId)
                    .sortedBy { it.usdPriceId }
                Timber.d("getCryptoList: prices of crypto ${cryptoLocal.name}: $cryptoPrices")
                Crypto(
                    cryptoId = cryptoLocal.cryptoId,
                    name = cryptoLocal.name,
                    quoteEntity = Quote(Currency(12.0,12.0,"asdas")),
                    slug = cryptoLocal.slug,
                    symbol = cryptoLocal.symbol)
            }
            emitter.onSuccess(mappedResults)
        }
    }

    override fun saveCrypto(crypto: Crypto): Completable {
        Timber.d("saveCrypto")
//        return cryptoDao.addCrypto(
//            CryptoLocal(
//                cryptoId = crypto.cryptoId,
//                symbol = crypto.symbol,
//                slug = crypto.slug,
//                name = crypto.name
//            )
//        )
        TODO()
    }

    override fun saveCryptoList(cryptoList: List<Crypto>): Completable {
        Timber.d("saveCryptoList")
        return Completable.create {emitter ->
            Timber.d("saveCryptoList: saving data")
            val mappedInput = cryptoList.map {
                CryptoLocal(
                    cryptoId = it.cryptoId,
                    symbol = it.symbol,
                    slug = it.slug,
                    name = it.name
                )
            }

            val prices = cryptoList.map {
                UsdPrice(
                    null,
                    it.quoteEntity.usd.price,
                    it.quoteEntity.usd.lastUpdated,
                    it.quoteEntity.usd.marketCap,
                    it.cryptoId
                )
            }

            this.cryptoDao.addCryptoList(mappedInput)
            this.cryptoDao.addUsdPriceList(prices)
            emitter.onComplete()
        }
    }
}