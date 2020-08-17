# Crypto app

### How to build and run
```shell script
./gradlew build
./gradlew run --console=plain
```


### Supported commands

- Get price table
```
-> prices
```
- Get margin assets
```
-> margin assets
-> margin asset {coin}
```
- Make asset
```
-> margin let {baseCoin} {counterCoin} {value}
```

### Examples

```bash
-> prices
XTZ-USDT => 4.16130000
ONT-USDT => 0.91420000
BCH-USDT => 312.70000000
LINK-USDT => 19.29310000
BTC-USDT => 11877.95000000
BUSD-USDT => 0.99970000
ZEC-USDT => 85.13000000
QTUM-USDT => 3.45000000
ETH-USDT => 427.30000000
DASH-USDT => 98.20000000
ATOM-USDT => 6.16400000
XMR-USDT => 91.05000000
BAT-USDT => 0.31480000
USDC-USDT => 0.99950000
VET-USDT => 0.02083000
TRX-USDT => 0.02975000
NEO-USDT => 15.96000000
EOS-USDT => 3.82390000
XRP-USDT => 0.30251000
IOST-USDT => 0.00749700
ADA-USDT => 0.14293000
ETC-USDT => 7.40420000
BNB-USDT => 23.09520000
MATIC-USDT => 0.03096000
LTC-USDT => 64.15000000
RVN-USDT => 0.02659000
XLM-USDT => 0.11877000
IOTA-USDT => 0.41870000

-> margin asset BTC
BTC
total: 4.6E-7
free: 4.6E-7
borrowed: 0E-8

-> margin let BTC USDT 0.0011
Ok.

-> margin asset BTC
BTC
total: 0.00109836
free: 0.00109836
borrowed: 0E-8

-> margin let BTC USDT -0.0012
Ok.

-> margin asset BTC
BTC
total: -0.00119966
free: 3.6E-7
borrowed: 0.00120000

-> 
```

