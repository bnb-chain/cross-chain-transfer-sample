package com.binance.app.transfer;

import com.binance.dex.api.client.BinanceDexApiNodeClient;
import com.binance.dex.api.client.BinanceDexEnvironment;
import com.binance.dex.api.client.Wallet;
import com.binance.dex.api.client.domain.TransactionMetadata;
import com.binance.dex.api.client.domain.broadcast.TransactionOption;
import com.binance.dex.api.client.encoding.message.Token;
import org.web3j.protocol.Web3j;

import java.util.Arrays;
import java.util.List;

public class TransferToBSC {

    public BinanceDexApiNodeClient client;

    public Wallet wallet;

    public TransferToBSC(BinanceDexApiNodeClient client, Wallet wallet) {
        this.client = client;
        this.wallet = wallet;
    }

    public void transfer(String recipientOnBSC, Token token, long expireTime) throws Exception {
        TransactionOption options = new TransactionOption("", 0, null);
        List<TransactionMetadata> result = this.client.transferOut(recipientOnBSC, token, expireTime, this.wallet, options, true);
        System.out.println(result);
    }
}
