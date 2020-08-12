// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package abi

import (
	"math/big"
	"strings"

	ethereum "github.com/ethereum/go-ethereum"
	"github.com/ethereum/go-ethereum/accounts/abi"
	"github.com/ethereum/go-ethereum/accounts/abi/bind"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/core/types"
	"github.com/ethereum/go-ethereum/event"
)

// Reference imports to suppress errors if they are not otherwise used.
var (
	_ = big.NewInt
	_ = strings.NewReader
	_ = ethereum.NotFound
	_ = abi.U256
	_ = bind.Bind
	_ = common.Big1
	_ = types.BloomLookup
	_ = event.NewSubscription
)

// TokenHubABI is the input ABI used to generate the binding from.
const TokenHubABI = "[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"contractAddr\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"recipient\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"amount\",\"type\":\"uint256\"},{\"internalType\":\"uint64\",\"name\":\"expireTime\",\"type\":\"uint64\"}],\"name\":\"transferOut\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"

// TokenHub is an auto generated Go binding around an Ethereum contract.
type TokenHub struct {
	TokenHubCaller     // Read-only binding to the contract
	TokenHubTransactor // Write-only binding to the contract
	TokenHubFilterer   // Log filterer for contract events
}

// TokenHubCaller is an auto generated read-only Go binding around an Ethereum contract.
type TokenHubCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// TokenHubTransactor is an auto generated write-only Go binding around an Ethereum contract.
type TokenHubTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// TokenHubFilterer is an auto generated log filtering Go binding around an Ethereum contract events.
type TokenHubFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// TokenHubSession is an auto generated Go binding around an Ethereum contract,
// with pre-set call and transact options.
type TokenHubSession struct {
	Contract     *TokenHub         // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// TokenHubCallerSession is an auto generated read-only Go binding around an Ethereum contract,
// with pre-set call options.
type TokenHubCallerSession struct {
	Contract *TokenHubCaller // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts   // Call options to use throughout this session
}

// TokenHubTransactorSession is an auto generated write-only Go binding around an Ethereum contract,
// with pre-set transact options.
type TokenHubTransactorSession struct {
	Contract     *TokenHubTransactor // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts   // Transaction auth options to use throughout this session
}

// TokenHubRaw is an auto generated low-level Go binding around an Ethereum contract.
type TokenHubRaw struct {
	Contract *TokenHub // Generic contract binding to access the raw methods on
}

// TokenHubCallerRaw is an auto generated low-level read-only Go binding around an Ethereum contract.
type TokenHubCallerRaw struct {
	Contract *TokenHubCaller // Generic read-only contract binding to access the raw methods on
}

// TokenHubTransactorRaw is an auto generated low-level write-only Go binding around an Ethereum contract.
type TokenHubTransactorRaw struct {
	Contract *TokenHubTransactor // Generic write-only contract binding to access the raw methods on
}

// NewTokenHub creates a new instance of TokenHub, bound to a specific deployed contract.
func NewTokenHub(address common.Address, backend bind.ContractBackend) (*TokenHub, error) {
	contract, err := bindTokenHub(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &TokenHub{TokenHubCaller: TokenHubCaller{contract: contract}, TokenHubTransactor: TokenHubTransactor{contract: contract}, TokenHubFilterer: TokenHubFilterer{contract: contract}}, nil
}

// NewTokenHubCaller creates a new read-only instance of TokenHub, bound to a specific deployed contract.
func NewTokenHubCaller(address common.Address, caller bind.ContractCaller) (*TokenHubCaller, error) {
	contract, err := bindTokenHub(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &TokenHubCaller{contract: contract}, nil
}

// NewTokenHubTransactor creates a new write-only instance of TokenHub, bound to a specific deployed contract.
func NewTokenHubTransactor(address common.Address, transactor bind.ContractTransactor) (*TokenHubTransactor, error) {
	contract, err := bindTokenHub(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &TokenHubTransactor{contract: contract}, nil
}

// NewTokenHubFilterer creates a new log filterer instance of TokenHub, bound to a specific deployed contract.
func NewTokenHubFilterer(address common.Address, filterer bind.ContractFilterer) (*TokenHubFilterer, error) {
	contract, err := bindTokenHub(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &TokenHubFilterer{contract: contract}, nil
}

// bindTokenHub binds a generic wrapper to an already deployed contract.
func bindTokenHub(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(TokenHubABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_TokenHub *TokenHubRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _TokenHub.Contract.TokenHubCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_TokenHub *TokenHubRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, error) {
	return _TokenHub.Contract.TokenHubTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_TokenHub *TokenHubRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, error) {
	return _TokenHub.Contract.TokenHubTransactor.contract.Transact(opts, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_TokenHub *TokenHubCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _TokenHub.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_TokenHub *TokenHubTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, error) {
	return _TokenHub.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_TokenHub *TokenHubTransactorRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, error) {
	return _TokenHub.Contract.contract.Transact(opts, method, params...)
}

// TransferOut is a paid mutator transaction binding the contract method 0xaa7415f5.
//
// Solidity: function transferOut(address contractAddr, address recipient, uint256 amount, uint64 expireTime) returns(bool)
func (_TokenHub *TokenHubTransactor) TransferOut(opts *bind.TransactOpts, contractAddr common.Address, recipient common.Address, amount *big.Int, expireTime uint64) (*types.Transaction, error) {
	return _TokenHub.contract.Transact(opts, "transferOut", contractAddr, recipient, amount, expireTime)
}

// TransferOut is a paid mutator transaction binding the contract method 0xaa7415f5.
//
// Solidity: function transferOut(address contractAddr, address recipient, uint256 amount, uint64 expireTime) returns(bool)
func (_TokenHub *TokenHubSession) TransferOut(contractAddr common.Address, recipient common.Address, amount *big.Int, expireTime uint64) (*types.Transaction, error) {
	return _TokenHub.Contract.TransferOut(&_TokenHub.TransactOpts, contractAddr, recipient, amount, expireTime)
}

// TransferOut is a paid mutator transaction binding the contract method 0xaa7415f5.
//
// Solidity: function transferOut(address contractAddr, address recipient, uint256 amount, uint64 expireTime) returns(bool)
func (_TokenHub *TokenHubTransactorSession) TransferOut(contractAddr common.Address, recipient common.Address, amount *big.Int, expireTime uint64) (*types.Transaction, error) {
	return _TokenHub.Contract.TransferOut(&_TokenHub.TransactOpts, contractAddr, recipient, amount, expireTime)
}
