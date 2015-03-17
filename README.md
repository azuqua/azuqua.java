azuqua.java
===========

Azuqua java library

Changes in the fix-enhancement-quy branch:

Created a seperate java file for the inner class Flo. The reason for this was that whenever someone would call the azuqua.getFlos()[0].invoke(data) method, they would get a null pointer exception from trying to read the invokeRoute string. The null pointer exception comes from the fact that the Flo class was not instantiated based on an instantiated outer class. Since the outer object wasn't instatiated, the inner class was basically referencing null. See this link for more details: http://docs.oracle.com/javase/tutorial/java/javaOO/nested.html

To instantiate an inner class, you must first instantiate the outer class. Then, create the inner object within the outer object with this syntax:

OuterClass.InnerClass innerObject = outerObject.new InnerClass();
 
With that change, I had to also seperate out the AzuquaException class since it's used by both the Flo and Azuqua class. 

As far as the azuqua.makeRequest() and azuqua.signData() method goes, I just basically made them act the same as the node.js methods. The makeRequest method now differentiates between GET and POST requests. I think for GET requests, the HttpUrlConnection object confines you to only read the response from the endpoint and not write to it. Probably assumes that you're sending parameters via the URL string. The POST requests writes to and reads from the end point.
