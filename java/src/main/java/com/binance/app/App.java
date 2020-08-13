package com.binance.app;

import com.binance.dex.api.client.BinanceDexApiClientFactory;
import com.binance.dex.api.client.BinanceDexApiNodeClient;
import com.binance.dex.api.client.BinanceDexEnvironment;
import com.binance.dex.api.client.Wallet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Cross chain transfer samples between Binance Chain and Binance Smart Chain
 */
public class App {

    public static final String Mnemonic = "";

    public static final String Privatekey = "";

    private static final Log logger = LogFactory.getLog(App.class);

    public static void main(String[] args) throws Exception {
        BinanceDexApiNodeClient bcClient = BinanceDexApiClientFactory.newInstance().newNodeRpcClient(BinanceDexEnvironment.TEST_NET.getNodeUrl(),
                BinanceDexEnvironment.TEST_NET.getHrp(), BinanceDexEnvironment.TEST_NET.getValHrp());
        Wallet wallet = Wallet.createWalletFromMnemonicCode(Arrays.asList(Mnemonic.split(" ")), BinanceDexEnvironment.TEST_NET);

        BigInteger privkey = new BigInteger(Privatekey, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(privkey);
        Credentials credentials = Credentials.create(ecKeyPair);
        Web3j web3j = Web3j.build(new HttpService("https://data-seed-prebsc-1-s1.binance.org:8545"));

        Transfer transfer = new Transfer(bcClient, wallet, credentials, web3j);

        logger.info("Transfer BNB from Binance Chain to Binance Smart Chain");
        transfer.transferBNBToBSC();
        logger.info("---------------------------------------------------------------------------------");

        logger.info("Transfer BTCB from Binance Chain to Binance Smart Chain");
        transfer.transferBEP2EToBSC();
        logger.info("---------------------------------------------------------------------------------");

        logger.info("Transfer BNB from Binance Smart Chain to Binance Chain");
        transfer.transferBNBToBC();
        logger.info("---------------------------------------------------------------------------------");

        logger.info("Transfer BTCB from Binance Smart Chain to Binance Chain");
        transfer.transferBEP2EToBC();
        logger.info("---------------------------------------------------------------------------------");
    }
}
