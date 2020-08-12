package com.binance.app.query;

import com.binance.dex.api.client.BinanceDexApiNodeClient;
import com.binance.dex.api.client.domain.Account;
import com.binance.dex.api.client.domain.Balance;

import java.util.List;

public class BCQuery {

    public BinanceDexApiNodeClient client;

    public BCQuery(BinanceDexApiNodeClient client) {
        this.client = client;
    }

    public List<Balance> getBalance(String address){
        Account account = client.getAccount(address);
        return account.getBalances();
    }

}
