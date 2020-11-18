package com.uxi.bambupaymerchant.repository

/**
 * Created by Eraño Payawal on 11/18/20.
 * hunterxer31@gmail.com
 */
//@Singleton
class HistoryRepository /*@Inject constructor(
    private val historyDao: HistoryDao,
    private val webService: WebService
) */{

//    fun loadHistory(
//        lastId: String?,
//        deleteOldData: Boolean
//    ): Flowable<Transactions> {
//        return webService.history(lastId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .map {
//                if (deleteOldData) {
//                    historyDao.saveNewData(it.response!!)
//                } else {
//                    historyDao.copyOrUpdate(it.response!!)
//                }
//                it.response
//            }
//    }

    /*fun history(): Flowable<List<Transaction>> {
        return historyDao.getGroups()
    }*/

}