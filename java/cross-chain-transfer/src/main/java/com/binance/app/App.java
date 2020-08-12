package com.binance.app;

import com.binance.app.query.BCQuery;
import com.binance.app.query.BSCQuery;
import com.binance.app.transfer.TransferToBSC;
import com.binance.dex.api.client.BinanceDexApiClientFactory;
import com.binance.dex.api.client.BinanceDexApiNodeClient;
import com.binance.dex.api.client.BinanceDexEnvironment;
import com.binance.dex.api.client.Wallet;
import com.binance.dex.api.client.domain.Balance;
import com.binance.dex.api.client.domain.TransactionMetadata;
import com.binance.dex.api.client.domain.broadcast.TransactionOption;
import com.binance.dex.api.client.encoding.message.Token;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import static java.lang.String.format;

/**
 * Cross chain transfer between Binance Chain and Binance Smart Chain
 */
public class App {

    private static final Log logger = LogFactory.getLog(App.class);

    public static void transferBNBToBSC(BinanceDexApiNodeClient bcClient, Wallet wallet, Web3j web3j, Credentials credentials) throws Exception {
        BCQuery bcQuery = new BCQuery(bcClient);
        List<Balance> balances = bcQuery.getBalance(wallet.getAddress());
        logger.info("On Binance Chain, Balance: ");
        for(Balance bal : balances) {
            logger.info(format("%s:%s, ", bal.getFree(), bal.getSymbol()));
        }

        BSCQuery bscQuery = new BSCQuery(web3j);
        BigInteger bnbBalance = bscQuery.queryBNBBalance(credentials.getAddress());
        logger.info(format("On Binance Smart Chain, Address %s, BNB Balance: %s", credentials.getAddress(), bnbBalance.toString()));

        BigInteger btcbBalance = bscQuery.queryBTCBBalance(credentials.getAddress());
        logger.info(format("On Binance Smart Chain, Address %s, BTCB Balance: %s", credentials.getAddress(), btcbBalance.toString()));

        //Transfer BNB from Binance Chain to Binance Smart Chain
        TransferToBSC transferToBSC = new TransferToBSC(bcClient, wallet);
        Token token = new Token("BNB", 10000L);
        long expireTime = System.currentTimeMillis() + 1000 * 60 * 10; // 10 minutes later
        transferToBSC.transfer("0x4Cd8941450130C70F81F720dB468fC9345C83E85", token, expireTime/1000);

        Thread.sleep(5 * 1000);

        balances = bcQuery.getBalance(wallet.getAddress());
        logger.info("On Binance Chain, Balance: ");
        for(Balance bal : balances) {
            logger.info(format("%s:%s, ", bal.getFree(), bal.getSymbol()));
        }

        Thread.sleep(5 * 1000);

        bnbBalance = bscQuery.queryBNBBalance(credentials.getAddress());
        logger.info(format("On Binance Smart Chain, Address %s, BNB Balance: %s", credentials.getAddress(), bnbBalance.toString()));
    }

    public static void main(String[] args) throws Exception  {
        BinanceDexApiNodeClient bcClient = BinanceDexApiClientFactory.newInstance().newNodeRpcClient(BinanceDexEnvironment.TEST_NET.getNodeUrl(),
                BinanceDexEnvironment.TEST_NET.getHrp(), BinanceDexEnvironment.TEST_NET.getValHrp());
        String mnemonic = "cricket sort dilemma general minor hamster rare door improve receive warfare snack thumb elite coral crunch dune pole hire fly broccoli aerobic ivory neither";
        Wallet wallet = Wallet.createWalletFromMnemonicCode(Arrays.asList(mnemonic.split(" ")), BinanceDexEnvironment.TEST_NET);

        String privatekey = "0ae5d3594b158f7de8988a0628d97ef234e958c5bf95e5eb5b7c26562a11d70d";
        BigInteger privkey = new BigInteger(privatekey, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(privkey);
        Credentials credentials = Credentials.create(ecKeyPair);
        Web3j web3j = Web3j.build(new HttpService("https://data-seed-prebsc-1-s1.binance.org:8545"));

        transferBNBToBSC(bcClient, wallet, web3j, credentials);
    }
}
