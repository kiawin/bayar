Bayar - Payment Engine
======================

Bayar is a component that takes an Invoice as the input and determines which payment method (CreditCardProvider or Directdebit) to use to collect money from the invoice, based on some Rules.

An invoice contains the following information
- From
- To
- Payment Method [Eg: Credit/Direct Debit]
   - If Credit, which card is it - Visa/AMEX/Master
   - If Debit then the bank name
- Amount
- Country

A CreditCard provider has following attributes:
- Country
- Fee
- Card Type (Visa/AMEX/Master)


Bayar supports multiple payment providers. We have to determine which provider to use based on the amount and payment method.

Credit card provider:
- #1 Paypal
- #2 Maybank
- #3 HSBC

Some sample rules :
- "if amount is above $1000 then use CreditCardProvider#1 (Paypal)"
- "if country is Malaysia and Card is Amex, use CreditCardProvider#2 (Maybank)"
- "If country is Australia and Card is Visa, use CreditCardProvder#1 (Paypal) or CreditCardProvider#2 (Maybank) (whichever Fee is lowest) but not CreditCardProvider#3 (HSBC)"
- "If the payment method is bank, determine which bank to use"

Some assumptions:
- The rule involving amount currently always considered sharing the same as the currency code given by user.
- All inputs are valid

Libraries
=========

# Spring Boot : http://projects.spring.io/spring-boot/
# EasyRules : http://www.easyrules.org/
# CountryCode: http://takahikokawasaki.github.io/nv-i18n/
# JavaMoney: https://github.com/JavaMoney

To Run
=======

Parameter options:

```
from: any string
to: any string
method: [credit|direct]
type: [default|visa|master|amex] (optional)
amount: any float number
country: 2-character country code
bank: any string (optional)
```

Run using spring boot

```
mvn spring-boot:run -Drun.arguments="--from=john,--to=doe,--method=credit,--type=default,--amount=105,--country=us"
mvn spring-boot:run -Drun.arguments="--from=john,--to=doe,--method=direct,--amount=105,--country=us"
```

Run using jar

```
mvn clean install
java -jar target/bayar-1.0.jar --from=john --to=doe --method=credit --type=default --amount=105 --country=us
java -jar target/bayar-1.0.jar --from=john --to=doe --method=direct --amount=1005 --country=sg
```

