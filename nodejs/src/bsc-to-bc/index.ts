import { decode } from 'bech32-buffer';
import Web3 from 'web3';
import { Big } from 'big.js';
// import abi from 'human-standard-token-abi';
import { tokenHubAbi, tokenHubContractAddress, bep20Abi } from './abis';

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
  amount: number;
  asset: Asset;
}): string => {
  return new Big(amount).times(`1e${asset.decimals}`).toFixed();
};

// const hexToBytes = (hex: string) => {
//   let bytes = [];
//   for (let c = 0; c < hex.length; c += 2) {
//     bytes.push(parseInt(hex.substr(c, 2), 16));
//   }
//   return Buffer.from(bytes);
// };

export const approve = async ({
  web3,
  amount,
  contractAddress,
  privateKey,
}: {
  web3: Web3;
  amount: string;
  contractAddress: string;
  privateKey: string;
}) => {
  const erc20Contract = new web3.eth.Contract(bep20Abi, contractAddress);
  const approvedABI = erc20Contract.methods
    .approve(tokenHubContractAddress, amount)
    .encodeABI();

  const tx = await buildSignedBscTx({
    data: approvedABI,
    privateKey,
    toAddress: contractAddress,
    amount: 0,
  });

  await web3.eth.sendSignedTransaction(tx);
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
  amount: number;
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

  if (asset.networkSymbol !== 'BNB') {
    await approve({
      web3,
      amount: web3.utils.toHex(formatAmount({ amount, asset })),
      contractAddress: asset.contractAddress,
      privateKey,
    });
  }

  const transferOutABI = contract.methods
    .transferOut(
      asset.contractAddress,
      `0x${decodeAddress}`,
      web3.utils.toHex(formatAmount({ amount, asset })),
      expireTime
    )
    .encodeABI();

  const relayFeeWei = await contract.methods.getMiniRelayFee().call();
  let value = new Big(relayFeeWei);
  if (asset.networkSymbol === 'BNB') {
    value = value.add(formatAmount({ amount, asset }));
  }
  console.log(value.toString());
  const sendTx = await buildSignedBscTx({
    data: transferOutABI,
    privateKey,
    toAddress: tokenHubContractAddress,
    amount: value.toString(),
  });

  return await web3.eth.sendSignedTransaction(sendTx);
};
