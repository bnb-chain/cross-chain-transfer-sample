package main

import "github.com/binance-chain/cross-chain-transfer-sample/tx"

func init() {
	tx.BscPrivateKey = "" // Replace bsc private key with your private key

	tx.BCMnemonic = "" // Replace bc mnemonic with your mnemonic
}

func main() {
	// Transfer ABC on bsc
	err := tx.TransferFromBscToBc("0x88BF1A6a56D7bef5A8CB5aBdda0882E01C113132", "tbnb1ma4l5grxl58glrsu8p8206mydth3656jgka4ks", "100000000000")
	if err != nil {
		print(err.Error())
	}

	// Tranfer BNB on bsc
	err = tx.TransferFromBscToBc(tx.BNBContractAddr, "tbnb1ma4l5grxl58glrsu8p8206mydth3656jgka4ks", "100000000000")
	if err != nil {
		print(err.Error())
	}

	// Transfer token on bc
	err = tx.TransferFromBcToBsc("0x042ccc750E1099068622Bb521003F207297a40b0", "BNB", 100000000)
	if err != nil {
		print(err.Error())
	}
}
