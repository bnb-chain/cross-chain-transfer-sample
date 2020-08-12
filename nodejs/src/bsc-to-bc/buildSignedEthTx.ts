import Web3 from 'web3';
import Common from 'ethereumjs-common';
import { Transaction } from 'ethereumjs-tx';
import { Big, BigSource } from 'big.js';

const ETH_GAS_LIMIT = '200000';

export const buildSignedBscTx = async ({
  privateKey,
  toAddress,
  amount,
  data = '',
}: {
  privateKey: string;
  toAddress: string;
  amount: BigSource;
  data?: string;
}) => {
  const ethConfig = {
    baseChain: 'mainnet',
    commonConfig: { chainId: 97 },
    hardfork: 'petersburg',
    isCustom: true,
  };

  const web3 = new Web3(
    new Web3.providers.HttpProvider(
      'https://data-seed-prebsc-1-s1.binance.org:8545'
    )
  );

  const { address: fromAddress } = web3.eth.accounts.privateKeyToAccount(
    privateKey
  );

  console.log(fromAddress);

  const txCount = await web3.eth.getTransactionCount(fromAddress);
  const gasPrice = await web3.eth.getGasPrice();
  const rawTx = {
    nonce: web3.utils.toHex(txCount),
    gasPrice: web3.utils.toHex(gasPrice),
    gasLimit: web3.utils.toHex(ETH_GAS_LIMIT),
    to: toAddress,
    value: web3.utils.toHex(
      web3.utils.toWei(new Big(amount).toFixed(), 'ether')
    ),
    data: web3.utils.toHex(data),
  };

  const tx = new Transaction(
    rawTx,
    ethConfig.isCustom
      ? {
          common: Common.forCustomChain(
            ethConfig.baseChain,
            ethConfig.commonConfig,
            ethConfig.hardfork
          ),
        }
      : ethConfig
  );
  tx.sign(Buffer.from(privateKey, 'hex'));

  return `0x${tx.serialize().toString('hex')}`;
};
