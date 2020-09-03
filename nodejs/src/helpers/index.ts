import { BncClient, crypto } from '@binance-chain/javascript-sdk';

export const mnemonic =
  'ankle duty race robust more charge elder enact hill logic material female';

export const getClient = async (
  url = 'https://testnet-dex-asiapacific.binance.org'
) => {
  const client = new BncClient(url);
  await client.initChain();
  const privateKey = crypto.getPrivateKeyFromMnemonic(mnemonic);
  await client.setPrivateKey(privateKey);

  // use default delegates (signing, broadcast)
  client.useDefaultSigningDelegate();
  client.useDefaultBroadcastDelegate();
  return client;
};

//address: 0x337c57cc302B50Dd11EB3e6e3311E539421a7bD4
export const privateKey = crypto.getPrivateKeyFromMnemonic(mnemonic);
