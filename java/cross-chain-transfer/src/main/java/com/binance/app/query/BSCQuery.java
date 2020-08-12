package com.binance.app.query;

import com.binance.app.contract.BTCB;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

public class BSCQuery {

    public static final String BTCBContract = "0x807D4de360d1FE2132AB778797984E0615193644";

    public Web3j web3b;

    public BSCQuery(Web3j web3b) {
        this.web3b = web3b;
    }

    public BigInteger queryBNBBalance(String address) throws Exception {
        EthGetBalance balance = web3b.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        return balance.getBalance();
    }

    public BigInteger queryBTCBBalance(String address) throws Exception {
        EthGasPrice gasPrice = web3b.ethGasPrice().send();
        TransactionManager transactionManager = new ReadonlyTransactionManager(web3b, address);
        StaticGasProvider staticGasProvider = new StaticGasProvider(gasPrice.getGasPrice(), BigInteger.valueOf(4700000));

        BTCB btcb = BTCB.load(BTCBContract, web3b, transactionManager, staticGasProvider);
        return btcb.balanceOf(address).send();
    }

}
