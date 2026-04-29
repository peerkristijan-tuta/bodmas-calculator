For a guide on how to run this program, consult the input output sample image. For execution in Windows, remove the quote marks from then expression.

For the class diagram, I have come up with my own variant because to my understanding, the UML does not set a standard for depicting inner classes within methods. However, I went with this inner class design choice because the program was initially structured as a main program and subroutines, but I decided to group related computation tasks under one higher level method each, and set up a helper class for each to contain the lower level computation tasks with the higher level methods coordinating the lower level computation tasks. 

For the overall flow diagram, each box is labelled with method name. Black lines indicate that main is invoking the method. Red and green indicate that the the methods of the so-colored boxes are. 

For the bracket computation flow diagram, likewise for the box labels as well as black and other colored lines. Some lines are annotated to further contextualize the nature of the method call such as the condition that led to it. Do note that this flow diagram depicts recursion, as seen in multiple possible events where computeExpressionWithBrackets() is invoked. 
