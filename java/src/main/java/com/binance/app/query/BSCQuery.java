package com.binance.app.query;

import com.binance.app.contract.BEP2E;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

public class BSCQuery {

    public Web3j web3b;

    public BSCQuery(Web3j web3b) {
        this.web3b = web3b;
    }

    public BigInteger queryBNBBalance(String address) throws Exception {
        EthGetBalance balance = web3b.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        return balance.getBalance();
    }

    public BigInteger queryBEP2EBBalance(String contractAddr, String address) throws Exception {
        EthGasPrice gasPrice = web3b.ethGasPrice().send();
        TransactionManager transactionManager = new ReadonlyTransactionManager(web3b, address);
        StaticGasProvider staticGasProvider = new StaticGasProvider(gasPrice.getGasPrice(), BigInteger.valueOf(4700000));
        BEP2E bep2e = BEP2E.load(contractAddr, web3b, transactionManager, staticGasProvider);
        return bep2e.balanceOf(address).send();
    }

}
