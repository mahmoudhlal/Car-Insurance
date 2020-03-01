package com.mahmoud.carsinsurance

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.mahmoud.carsinsurance.Utils.SharedPrefManager
import com.mahmoud.carsinsurance.ui.user.home.ViewModel.CompanyViewModelFactory
import com.mahmoud.carsinsurance.ui.user.home.ViewModel.OrderViewModelFactory
import com.mahmoud.carsinsurance.ui.user.home.ViewModel.NotificationViewModelFactory
import com.mahmoud.carsinsurance.ui.user.home.repository.*
import java.util.concurrent.Executors

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    // thread pool used for network requests
    @Suppress("PrivatePropertyName")
    private val NETWORK_IO = Executors.newFixedThreadPool(5)

    /**
     * Creates an instance of [CompanyRepository] based on the [provideToken]
     */
    private fun provideCompanyRepository(context: Context): CompanyRepository {
        return CompanyRepositoryImpl(provideToken(context))
    }


    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return CompanyViewModelFactory(provideCompanyRepository(context))
    }


    /**
     * Creates an instance of [OrderRepository] based on the [provideToken]
     */
    private fun provideOrderRepository(context: Context): OrderRepository {
        return OrderRepositoryImpl(provideToken(context))
    }


    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideOrderViewModelFactory(context: Context): ViewModelProvider.Factory {
        return OrderViewModelFactory(provideOrderRepository(context))
    }



    /**
     * Creates an instance of [NotificationRepository] based on the [provideToken]
     */
    private fun provideNotificationRepository(context: Context): NotificationRepository {
        return NotificationRepositoryImpl(provideToken(context))
    }
    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideNotificationViewModelFactory(context: Context): ViewModelProvider.Factory {
        return NotificationViewModelFactory(provideNotificationRepository(context))
    }



    private fun provideToken(context: Context): String? {
        return SharedPrefManager.getInstance(context)?.getToken()
    }

}