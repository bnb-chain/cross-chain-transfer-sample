import { decode } from 'bech32-buffer';
import Web3 from 'web3';
import { Big, BigSource } from 'big.js';

import { tokenHubAbi, tokenHubContractAddress } from './abis';

import { buildSignedBscTx } from './buildSignedEthTx';

export interface Asset {
  id: string;
  displaySymbol: string;
  networkSymbol: string;
  contractAddress: string | null;
  decimals: number;
}

export const formatAmount = ({
  amount,
  asset,
}: {
  amount: BigSource;
  asset: Asset;
}): string => {
  return new Big(amount).times(`1e${asset.decimals}`).toFixed();
};

export const transferFromBscToBbc = async ({
  privateKey,
  toAddress,
  amount,
  expireTime,
  asset,
}: {
  privateKey: string;
  toAddress: string;
  amount: BigSource;
  expireTime: number;
  asset: Asset;
}) => {
  if (!asset.contractAddress) {
    throw new Error(`Asset "${asset.id} does not have a contract address"`);
  }

  const web3 = new Web3(
    new Web3.providers.HttpProvider(
      'https://data-seed-prebsc-1-s1.binance.org:8545'
    )
  );

  const decodeData = decode(toAddress);
  const decodeAddress = Buffer.from(decodeData.data).toString('hex');

  const contract = new web3.eth.Contract(tokenHubAbi, tokenHubContractAddress);
  const encodedABI = contract.methods
    .transferOut(
      asset.contractAddress,
      `0x${decodeAddress}`,
      web3.utils.toHex(formatAmount({ amount, asset })),
      expireTime
    )
    .encodeABI();

  return web3.eth.sendSignedTransaction(
    await buildSignedBscTx({
      data: encodedABI,
      privateKey,
      toAddress: asset.contractAddress,
      amount: 0,
    })
  );
};
