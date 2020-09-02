import Web3 from 'web3';
import Common from 'ethereumjs-common';
import { Transaction } from 'ethereumjs-tx';

export const approve = async ({}) => {};

export const buildSignedBscTx = async ({
  privateKey,
  toAddress,
  amount,
  data = '',
}: {
  privateKey: string;
  toAddress: string;
  amount: number | string;
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

  const txCount = await web3.eth.getTransactionCount(fromAddress);
  const gasPrice = await web3.eth.getGasPrice();
  const estimatedGas = await web3.eth.estimateGas({
    to: toAddress,
    data,
    value: amount,
    from: fromAddress,
  });

  const rawTx = {
    nonce: web3.utils.toHex(txCount),
    gasPrice: web3.utils.toHex(gasPrice),
    gasLimit: web3.utils.toHex(estimatedGas),
    to: toAddress,
    value: web3.utils.toHex(amount),
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
