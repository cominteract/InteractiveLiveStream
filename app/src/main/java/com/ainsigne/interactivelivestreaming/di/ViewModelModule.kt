package com.ainsigne.interactivelivestreaming.di

import com.ainsigne.interactivelivestreaming.viewmodel.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
//    single {
////        StreamUserRepository(get(), get())
//        StreamItemDetailRepository(get())
//
////        StreamItemRepository(get())
//    }


    single {
        StreamItemDetailRepository(get())
    }

//    factory<StreamDetailRepository> { (StreamItemDetailRepository(get())) }
//    factory<StreamDetailRepository> { (FakeStreamItemDetailRepository(get())) }

//    single {
//        StreamDetailRepository
//    }

    single {
        StreamItemRepository(get(), get())
    }
    single {
        StreamUserRepository(get(), get())
    }
    viewModel {
//        UserViewModel(get())
        StreamItemDetailViewModel(get())
//        StreamDashboardViewModel(get())
    }



    
    viewModel {
        StreamUserViewModel(get())
    }
    viewModel {
        StreamItemViewModel(get())
    }
//    StreamDashboardViewModel(get())
//    StreamDetailViewModel(get())

}