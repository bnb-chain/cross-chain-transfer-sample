package tx

import (
	"fmt"
	"strings"
	"time"

	"github.com/binance-chain/go-sdk/types/msg"

	"github.com/binance-chain/go-sdk/client/rpc"
	"github.com/binance-chain/go-sdk/common/types"
	"github.com/binance-chain/go-sdk/keys"
)

const (
	BCProvider = "tcp://data-seed-pre-0-s3.binance.org:80"
)

var (
	BCMnemonic = "" // Replace mnemonic with your mnemonic
)

func init() {
	types.Network = types.GangesNetwork
}

func getKeyManager(mnemonic string) (keys.KeyManager, error) {
	return keys.NewMnemonicKeyManager(mnemonic)
}

func TransferFromBcToBsc(toBscAddr string, coin string, amount int64) error {
	rpcClient := rpc.NewRPCClient(BCProvider, types.Network)
	keyManager, err := getKeyManager(BCMnemonic)
	if err != nil {
		return fmt.Errorf("get key manager error, err=%s", err.Error())
	}
	rpcClient.SetKeyManager(keyManager)

	smartAddr := msg.NewSmartChainAddress(toBscAddr)
	res, err := rpcClient.TransferOut(smartAddr, types.Coin{
		Denom:  strings.ToUpper(coin),
		Amount: amount,
	}, time.Now().Unix()+DefaultExpireTime, rpc.Commit)
	if err != nil {
		return err
	}
	println("tx hash: ", res.Hash.String())
	return nil
}
