import { transferFromBcToBSc, Asset, transferFromBscToBbc } from '../src/';
import { privateKey } from '../src/helpers';

describe('blah', () => {
  beforeEach(() => {
    jest.setTimeout(50000);
  });

  it('transfer from bc to bsc', async () => {
    const toAddress = '0xc1c87c37be3Ef20273A4E8982293EEb6E08C620C';
    const from = 'tbnb1hgm0p7khfk85zpz5v0j8wnej3a90w709zzlffd';
    const result = await transferFromBcToBSc({
      toAddress,
      from,
      amount: 1,
      expireTime: 1597543193,
      symbol: 'BNB',
    });

    expect(result.status).toBe(200);
  });

  it('transfer bnb from bsc to bc', async () => {
    const BNB_ON_BBC_TESTNET_ASSET: Asset = {
      id: 'BNB',
      displaySymbol: 'BNB',
      networkSymbol: 'BNB',
      contractAddress: '0x0000000000000000000000000000000000000000',
      decimals: 8,
    };

    const toAddress = 'tbnb1hgm0p7khfk85zpz5v0j8wnej3a90w709zzlffd';
    const amount = 1.2;
    const expireTime = Math.ceil(Date.now() / 1000 + 600);
    console.log(privateKey);
    const result = await transferFromBscToBbc({
      privateKey,
      toAddress,
      amount,
      expireTime,
      asset: BNB_ON_BBC_TESTNET_ASSET,
    });

    expect(result).toHaveProperty('transactionHash');
  });
});
