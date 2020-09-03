import { BncClient, crypto } from '@binance-chain/javascript-sdk';

export const mnemonic = 'own mnemonic';

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

export const privateKey = crypto.getPrivateKeyFromMnemonic(mnemonic);
