package com.binance.app;

import com.binance.app.contract.BEP2E;
import com.binance.app.contract.TokenHub;
import com.binance.app.query.BCQuery;
import com.binance.app.query.BSCQuery;
import com.binance.dex.api.client.BinanceDexApiNodeClient;
import com.binance.dex.api.client.Wallet;
import com.binance.dex.api.client.domain.TransactionMetadata;
import com.binance.dex.api.client.domain.broadcast.TransactionOption;
import com.binance.dex.api.client.encoding.message.Token;
import com.binance.dex.api.client.encoding.message.common.Bech32AddressValue;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.List;

import static java.lang.String.format;
import static org.web3j.crypto.Keys.toChecksumAddress;

public class Transfer {
    public static final String BTCBContract = "0x807D4de360d1FE2132AB778797984E0615193644";
    public static final String TokenHubContract = "0x0000000000000000000000000000000000001004";
    public static final String BNBContract = "0x0000000000000000000000000000000000000000";
    public static final long relayFeeToBC = 10000000000000000L;

    private static final Log logger = LogFactory.getLog(Transfer.class);

    BinanceDexApiNodeClient bcClient;

    Wallet wallet;

    Credentials credentials;

    Web3j web3j;

    public Transfer(BinanceDexApiNodeClient bcClient, Wallet wallet, Credentials credentials, Web3j web3j) {
        this.bcClient = bcClient;
        this.wallet = wallet;
        this.credentials = credentials;
        this.web3j = web3j;
    }

    public void transferBNBToBSC() throws Exception {
        BCQuery bcQuery = new BCQuery(bcClient);
        Token bnbBalanceBC = bcQuery.getBalance("BNB", wallet.getAddress());
        logger.info(format("Binance Chain, Address %s, BNB Balance: %s", wallet.getAddress(), bnbBalanceBC.getAmount()));

        BSCQuery bscQuery = new BSCQuery(web3j);
        BigInteger bnbBalanceBSC = bscQuery.queryBNBBalance(credentials.getAddress());
        logger.info(format("Binance Smart Chain, Address %s, BNB Balance: %s", credentials.getAddress(), bnbBalanceBSC.toString()));

        //Transfer BNB from Binance Chain to Binance Smart Chain
        Token token = new Token("BNB", 10000L);
        long expireTime = System.currentTimeMillis() + 1000 * 60 * 10; // 10 minutes later
        TransactionOption options = new TransactionOption("", 0, null);
        List<TransactionMetadata> result = bcClient.transferOut(toChecksumAddress(credentials.getAddress()), token, expireTime / 1000, wallet, options, true);
        logger.info(format("Binance Chain, txHash: https://testnet-explorer.binance.org/tx/%s", result.get(0).getHash()));

        Thread.sleep(5 * 1000);

        bnbBalanceBC = bcQuery.getBalance("BNB", wallet.getAddress());
        logger.info(format("Binance Chain, Address %s, BNB Balance: %s", wallet.getAddress(), bnbBalanceBC.getAmount()));

        Thread.sleep(5 * 1000);

        bnbBalanceBSC = bscQuery.queryBNBBalance(credentials.getAddress());
        logger.info(format("On Binance Smart Chain, Address %s, BNB Balance: %s", credentials.getAddress(), bnbBalanceBSC.toString()));
    }

    public void transferBEP2EToBSC() throws Exception {
        BCQuery bcQuery = new BCQuery(bcClient);
        Token btcbBalanceBC = bcQuery.getBalance("BTCB-AFD", wallet.getAddress());
        logger.info(format("Binance Chain, Address %s, BNB Balance: %s", wallet.getAddress(), btcbBalanceBC.getAmount()));

        BSCQuery bscQuery = new BSCQuery(web3j);
        BigInteger btcbBalanceBSC = bscQuery.queryBNBBalance(credentials.getAddress());
        logger.info(format("Binance Smart Chain, Address %s, BNB Balance: %s", credentials.getAddress(), btcbBalanceBSC.toString()));

        //Transfer BNB from Binance Chain to Binance Smart Chain
        Token token = new Token("BTCB-AFD", 10000L);
        long expireTime = System.currentTimeMillis() + 1000 * 60 * 10; // 10 minutes later
        TransactionOption options = new TransactionOption("", 0, null);
        List<TransactionMetadata> result = bcClient.transferOut(toChecksumAddress(credentials.getAddress()), token, expireTime / 1000, wallet, options, true);
        logger.info(format("Binance Chain txHash: https://testnet-explorer.binance.org/tx/%s", result.get(0).getHash()));

        Thread.sleep(5 * 1000);

        btcbBalanceBC = bcQuery.getBalance("BTCB-AFD", wallet.getAddress());
        logger.info(format("Binance Chain, Address %s, BNB Balance: %s", wallet.getAddress(), btcbBalanceBC.getAmount()));

        Thread.sleep(5 * 1000);

        btcbBalanceBSC = bscQuery.queryBNBBalance(credentials.getAddress());
        logger.info(format("On Binance Smart Chain, Address %s, BNB Balance: %s", credentials.getAddress(), btcbBalanceBSC.toString()));
    }

    public void transferBNBToBC() throws Exception {
        BCQuery bcQuery = new BCQuery(bcClient);
        Token bnbBalanceBC = bcQuery.getBalance("BNB", wallet.getAddress());
        logger.info(format("Binance Chain, Address %s, BNB Balance: %s", wallet.getAddress(), bnbBalanceBC.getAmount()));

        BSCQuery bscQuery = new BSCQuery(web3j);
        BigInteger bnbBalanceBSC = bscQuery.queryBNBBalance(credentials.getAddress());
        logger.info(format("Binance Smart Chain, Address %s, BNB Balance: %s", credentials.getAddress(), bnbBalanceBSC.toString()));

        //Transfer BNB from Binance Smart Chain to Binance Chain
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = BigInteger.valueOf(4700000);
        StaticGasProvider staticGasProvider = new StaticGasProvider(gasPrice, gasLimit);

        // Call transferOut of tokenhub contract
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
        TokenHub tokenHub = TokenHub.load(TokenHubContract, web3j, credentials, staticGasProvider);
        Bech32AddressValue bech32AddressValue = Bech32AddressValue.fromBech32String(wallet.getAddress());
        String recpient = "0x"+ Hex.encodeHexString(bech32AddressValue.getRaw());
        BigInteger amount = BigInteger.valueOf(10000000000000000L);
        BigInteger expireTime = BigInteger.valueOf(System.currentTimeMillis() / 1000 + 60 * 10); // 10 minutes later
        String transactionData = tokenHub.transferOut(BNBContract, recpient, amount, expireTime).encodeFunctionCall();
        EthSendTransaction ethSendTransaction = transactionManager.sendTransaction(gasPrice, gasLimit, TokenHubContract, transactionData, BigInteger.valueOf(relayFeeToBC).add(amount));
        logger.info(format("Binance Smart Chain, transferOut txHash: https://explorer.binance.org/smart-testnet/tx/%s", ethSendTransaction.getTransactionHash()));

        Thread.sleep(5 * 1000);

        bnbBalanceBSC = bscQuery.queryBNBBalance(credentials.getAddress());
        logger.info(format("Binance Smart Chain, Address %s, BNB Balance: %s", credentials.getAddress(), bnbBalanceBSC.toString()));

        Thread.sleep(20 * 1000);

        bnbBalanceBC = bcQuery.getBalance("BNB", wallet.getAddress());
        logger.info(format("Binance Chain, Address %s, BNB Balance: %s", wallet.getAddress(), bnbBalanceBC.getAmount()));

    }

    public void transferBEP2EToBC() throws Exception {
        BCQuery bcQuery = new BCQuery(bcClient);
        Token btcbBalanceBC = bcQuery.getBalance("BTCB-AFD", wallet.getAddress());
        logger.info(format("Binance Chain, Address %s, BTCB-AFD Balance: %s", wallet.getAddress(), btcbBalanceBC.getAmount()));

        BSCQuery bscQuery = new BSCQuery(web3j);
        BigInteger btcbBalanceBSC = bscQuery.queryBEP2EBBalance(BTCBContract, credentials.getAddress());
        logger.info(format("Binance Smart Chain, Address %s, BTCB-AFD Balance: %s", credentials.getAddress(), btcbBalanceBSC.toString()));

        //Transfer BTCB from Binance Smart Chain to Binance Chain
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = BigInteger.valueOf(4700000);
        StaticGasProvider staticGasProvider = new StaticGasProvider(gasPrice, gasLimit);
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);

        // Step 1: call approve of BEP2E contract to gran allowance to tokenhub contract
        BEP2E bep2e = new BEP2E(BTCBContract, web3j, credentials, staticGasProvider);
        BigInteger amount = BigInteger.valueOf(10000000000L);
        String approveTxData = bep2e.approve(TokenHubContract, amount).encodeFunctionCall();
        EthSendTransaction ethSendApproveTx = transactionManager.sendTransaction(gasPrice, gasLimit, BTCBContract, approveTxData, BigInteger.valueOf(0));
        logger.info(format("Binance Smart Chain, BEP2E contract approve txHash: https://explorer.binance.org/smart-testnet/tx/%s", ethSendApproveTx.getTransactionHash()));

        // Step 2: call transferOut of tokenhub contract
        TokenHub tokenHub = TokenHub.load(TokenHubContract, web3j, credentials, staticGasProvider);
        Bech32AddressValue bech32AddressValue = Bech32AddressValue.fromBech32String(wallet.getAddress());
        String recpient = "0x"+Hex.encodeHexString(bech32AddressValue.getRaw());
        BigInteger expireTime = BigInteger.valueOf(System.currentTimeMillis() / 1000 + 60 * 10); // 10 minutes later
        String transferOutTxData = tokenHub.transferOut(BTCBContract, recpient, amount, expireTime).encodeFunctionCall();
        EthSendTransaction ethSendTransferOutTx = transactionManager.sendTransaction(gasPrice, gasLimit, TokenHubContract, transferOutTxData, BigInteger.valueOf(relayFeeToBC));
        logger.info(format("Binance Smart Chain, BEP2E contract transferOut txHash: https://explorer.binance.org/smart-testnet/tx/%s", ethSendTransferOutTx.getTransactionHash()));

        Thread.sleep(5 * 1000);

        btcbBalanceBSC = bscQuery.queryBEP2EBBalance(BTCBContract, credentials.getAddress());
        logger.info(format("Binance Smart Chain, Address %s, BTCB-AFD Balance: %s", credentials.getAddress(), btcbBalanceBSC.toString()));

        Thread.sleep(20 * 1000);

        btcbBalanceBC = bcQuery.getBalance("BTCB-AFD", wallet.getAddress());
        logger.info(format("Binance Chain, Address %s, BTCB-AFD Balance: %s", wallet.getAddress(), btcbBalanceBC.getAmount()));

    }

}
