# SafePay Telco Application

‘SafePay’ maximizes the security of credit/debit cards. A registered customer can send an USSD request to activate his credit card, before purchasing. Backend bank process can be found at [here](https://github.com/OmalPerera/SafePay-Registration-Interface)

<h3>Operation Process</h3>

* Customer should request the service from the bank (link the mobile number to the credit card)
* By default creadit card is deactivated.
* when a customer ready for a payment using Credit/debit card, he should activate his card by dialing a USSD code (ex : #555#) from his mobile number.
* Once card is activated. immediately customer gets a text message 'Your card is now activated'
* card willbe actibted for a given period of time (Ex : 5 min | 10 min |15 min etc)
* Now transaction can be done.


<hr>
<h3>Used API/ Libraries</h3>

* hSenid APIs
* Ideamart APIs
