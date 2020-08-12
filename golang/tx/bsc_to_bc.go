package tx

import (
	"context"
	"crypto/ecdsa"
	"math/big"
	"time"

	"github.com/binance-chain/go-sdk/common/types"
	"github.com/ethereum/go-ethereum/accounts/abi/bind"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/crypto"
	"github.com/ethereum/go-ethereum/ethclient"

	"github.com/binance-chain/cross-chain-transfer-sample/abi"
)

const (
	DefaultGasPrice      = 18000000000
	DefaultGasLimit      = uint64(5e6)
	DefaultExpireTime    = 3600 // 3600 s
	BNBContractAddr      = "0x0000000000000000000000000000000000000000"
	TokenHubContractAddr = "0x0000000000000000000000000000000000001004"
	BscProvider          = "https://data-seed-prebsc-1-s1.binance.org:8545"
	BscRelayFee          = int64(1e16)
)

var (
	BscPrivateKey = "" // Replace private key with your private key
)

func getBscPrivateKey(privateKeyStr string) (*ecdsa.PrivateKey, error) {
	privKey, err := crypto.HexToECDSA(privateKeyStr)
	if err != nil {
		return nil, err
	}
	return privKey, nil
}

func GetBscTransactOps(client *ethclient.Client, value *big.Int) (*bind.TransactOpts, error) {
	gasPrice := big.NewInt(DefaultGasPrice)
	privateKey, err := getBscPrivateKey(BscPrivateKey)
	if err != nil {
		return nil, err
	}
	publicKey := privateKey.Public()
	publicKeyECDSA, ok := publicKey.(*ecdsa.PublicKey)
	if !ok {
		panic("get public key error")
	}
	fromAddress := crypto.PubkeyToAddress(*publicKeyECDSA)

	nonce, err := client.PendingNonceAt(context.Background(), fromAddress)
	if err != nil {
		return nil, err
	}
	ops := bind.NewKeyedTransactor(privateKey)
	ops.Nonce = big.NewInt(int64(nonce))
	ops.GasLimit = DefaultGasLimit
	ops.GasPrice = gasPrice
	ops.Value = value
	return ops, nil
}

func ApproveBEP2E(client *ethclient.Client, cAddr common.Address, amountStr string, spender common.Address) error {
	bep2eC, err := abi.NewERC20(cAddr, client)
	if err != nil {
		return err
	}
	transactOps, err := GetBscTransactOps(client, nil)
	if err != nil {
		return err
	}
	amount := new(big.Int)
	amount.SetString(amountStr, 10)
	//
	trans, err := bep2eC.Approve(transactOps, spender, amount)
	if err != nil {
		return err
	}
	println("approve tx hash: ", trans.Hash().String())
	return nil
}

func TransferFromBscToBc(tokenContract string, toBcAddr string, amount string) error {
	client, err := ethclient.Dial(BscProvider)
	if err != nil {
		return err
	}

	if tokenContract != BNBContractAddr {
		err = ApproveBEP2E(client, common.HexToAddress(tokenContract), amount, common.HexToAddress(TokenHubContractAddr))
		if err != nil {
			return err
		}
	}

	tokenHub, err := abi.NewTokenHub(common.HexToAddress(TokenHubContractAddr), client)
	if err != nil {
		return err
	}

	amountBigInt := new(big.Int)
	amountBigInt.SetString(amount, 10)

	totalAmount := big.NewInt(BscRelayFee)
	if tokenContract == BNBContractAddr {
		totalAmount = totalAmount.Add(totalAmount, amountBigInt)
	}
	transactOps, err := GetBscTransactOps(client, totalAmount)
	if err != nil {
		return err
	}

	bcAddrBytes, err := types.GetFromBech32(toBcAddr, "tbnb")
	if err != nil {
		return err
	}
	recipient := common.BytesToAddress(bcAddrBytes)

	expire := uint64(time.Now().Unix() + DefaultExpireTime)
	trans, err := tokenHub.TransferOut(
		transactOps, common.HexToAddress(tokenContract), recipient, amountBigInt, expire)
	if err != nil {
		return err
	}
	println("transaction hash: ", trans.Hash().String())
	return nil
}
