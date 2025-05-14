package com.jdulfer.di

import com.jdulfer.dao.InvestorDAO
import com.jdulfer.dao.jdbc.JdbcInvestorDAO
import com.jdulfer.services.InvestorService
import org.koin.dsl.module

val appModule = module {
    single<InvestorDAO> { JdbcInvestorDAO() }
    single { InvestorService(get()) }
}