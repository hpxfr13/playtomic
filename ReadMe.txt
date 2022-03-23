I saved this transaction on my collection, after the transaction in the 3rd party service was successful. 
I put it in the try catch block for the possibility of getting an error during these operations. 
Actually I put all service operation on service layer. I could call stripeService in WalletService refund method. I noticed after sending:)
Controller is Singleton. But it doesn't mean they process your request sequentially. 
They are able to handle parallel request. 
It will create singleton instance of WalletController but Spring can call charge method multiple times at the same time as parallel.
I would load to WalletRequest object with dummy data in a method; furthermore, 
I call charge method with this object. If httpstatus is ok, I would write successful with Assert.