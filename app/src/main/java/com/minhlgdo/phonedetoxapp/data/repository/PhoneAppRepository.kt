package com.minhlgdo.phonedetoxapp.data.repository

import com.minhlgdo.phonedetoxapp.data.local.PhoneApp
import com.minhlgdo.phonedetoxapp.data.local.PhoneAppDao
import com.minhlgdo.phonedetoxapp.data.local.PhoneAppEntity
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class PhoneAppRepository @Inject constructor
    (private val dao: PhoneAppDao) {

    suspend fun insertBlockedApps(phoneApp: PhoneAppEntity) {
        dao.insertBlockedApps(phoneApp)
    }

    suspend fun deleteBlockedApps(phoneApp: PhoneAppEntity) {
        dao.deleteBlockedApps(phoneApp)
    }

    fun getBlockedApps() = dao.getBlockedApps()

}