package com.binance.app.query;

import com.binance.dex.api.client.BinanceDexApiNodeClient;
import com.binance.dex.api.client.domain.Account;
import com.binance.dex.api.client.domain.Balance;
import com.binance.dex.api.client.encoding.message.Token;

import java.util.List;

public class BCQuery {

    public BinanceDexApiNodeClient client;

    public BCQuery(BinanceDexApiNodeClient client) {
        this.client = client;
    }

    public Token getBalance(String denom, String address) {
        Account account = client.getAccount(address);
        List<Balance> balances = account.getBalances();
        for (Balance bal : balances) {
            if (bal.getSymbol().equals(denom)) {
                return new Token(denom, Long.parseLong(bal.getFree()));
            }
        }
        return new Token();
    }

    public List<Balance> getTotalBalance(String address) {
        Account account = client.getAccount(address);
        return account.getBalances();
    }

}
