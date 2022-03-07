# Chapter 2 Controlling Program Flow

- Create and use loops 
- if / else 
- switch statements

1. Variables declared as which of the following are never permitted in a
   switch statement? (Choose two.)
- A. var
- B. double
- C. int
- D. String
- E. char
- F. Object

2. What happens when running the following code snippet?
   ```
   3: var gas = true;
   4: do (
   5:     System.out.println("helium");
   6:     gas = gas ^ gas;
   7:     gas = !gas;
   8: ) while (!gas);
   ```
- A. It completes successfully without output.
- B. It outputs helium once.
- C. It outputs helium repeatedly.
- D. Line 6 does not compile.
- E. None of the above.

3. What is output by the following?
   ```
   10: int m = 0, n = 0;
   11: while (m < 5) {
   12:     n++;
   13:     if (m == 3)
   14:         continue;
   15:
   16:     switch (m) {
   17:         case 0:
   18:         case 1:
   19:             n++;
   20:         default:
   21:             n++;
   22:     }
   23:     m++;
   24: }
   25: System.out.println(m + " " + n);
   ```
- A. 3 10
- B. 3 12
- C. 5 10
- D. 5 12
- E. The code does not compile.
- F. None of the above.

4. Given the following, which can fill in the blank and allow the code to
   compile? (Choose three.)
   ``` 
   var quest =       ;
   for (var zelda : quest) {
       System.out.print(zelda);
   }
   ```
- A. 3
- B. new int[] {3}
- C. new StringBuilder("3")
- D. List.of(3)
- E. new String[3]
- F. "Link"

5. Which of the following rules about a default branch in a switch
   statement are correct? (Choose two.)
- A. A switch statement is required to declare a default statement.
- B. A default statement must be placed after all case statements.
- C. A default statement can be placed between any case statements.
- D. Unlike a case statement, a default statement does not take a
     parameter value.
- E. A switch statement can contain more than one default
     statement.
- F. A default statement can be used only when at least one case
     statement is present.

6. What does the following method output?
   ```
   void dance() {
       var singer = 0;
       while (singer)
           System.out.print(singer++);
       }
   ```
- A. The code does not compile.
- B. The method completes with no output.
- C. The method prints 0 and then terminates.
- D. The method enters an infinite loop.
- E. None of the above.

7. Which are true statements comparing for‐each and traditional for
   loops? (Choose two.)
- A. Both can iterate through an array starting with the first element.
- B. Only the for‐each loop can iterate through an array starting with
     the first element.
- C. Only the traditional for loop can iterate through an array starting
     with the first element.
- D. Both can iterate through an array starting from the end.
- E. Only the for‐each loop can iterate through an array starting from
     the end.
- F. Only the traditional for loop can iterate through an array starting
     from the end.

8. What is the output of the following application?
   ```
   package planning;
   public class ThePlan {
           public static void main(String[] input) {
               var plan = 1;
               plan = plan++ + --plan;
               if (plan==1) {
                   System.out.print("Plan A");
               } else { if(plan==2) System.out.print("Plan B");
               } else System.out.print("Plan C"); }
           }
   }
   ```
- A. Plan A
- B. Plan B
- C. Plan C
- D. The class does not compile.
- E. None of the above.

9. What is true about the following code? (Choose two.)
   ```
   23: var race = "";
   24: loop:
   25: do {
   26:     race += "x";
   27:     break loop;
   28: } while (true);
   29: System.out.println(race);
   ```
- A. It outputs x.
- B. It does not compile.
- C. It is an infinite loop.
- D. With lines 25 and 28 removed, it outputs x.
- E. With lines 25 and 28 removed, it does not compile.
- F. With lines 25 and 28 removed, it is an infinite loop.

10. Which of the following can replace the body of the perform() method
    to produce the same output on any nonempty input? (Choose two.)
    ```
    public void perform(String[] circus) {
        for (int i=circus.length-1; i>=0; i--)
            System.out.print(circus[i]);
    }
    ```
- A. 
    ``` 
    for (int i=circus.length; i>0; i--)
        System.out.print(circus[i-1]);
    ```
- B. 
    ``` 
    for-reversed (String c = circus)
        System.out.print(c);
    ```
- C.
    ``` 
    for (var c : circus)
        System.out.print(c);
    ```
- D.
    ``` 
    for(var i=0; i<circus.length; i++)
        System.out.print(circus[circus.length-i-1]);
    ```
- E.
    ``` 
    for (int i=circus.length; i>0; i--)
        System.out.print(circus[i+1]);
    ```
- F.
    ``` 
    for-each (String c circus)
        System.out.print(c);
    ``` 

11. What does the following code snippet output?
    ```
    var bottles = List.of("glass", "plastic", "can");
    for (int type = 1; type < bottles.size();) {
        System.out.print(bottles.get(type) + "-");
        if(type < bottles.size()) break;
    }
    System.out.print("end");
    ```
- A. glass‐end
- B. glass‐plastic‐can‐end
- C. plastic‐end
- D. plastic‐can‐end
- E. The code does not compile.
- F. None of the above.

12. What is the result of executing the following code snippet?
    ``` 
    final var GOOD = 100;
    var score = 10;
    switch (score) {
        default:
        1 : System.out.print("1-");
        -1 : System.out.print("2-"); break;
        4,5 : System.out.print("3-");
        6 : System.out.print("4-");
        9 : System.out.print("5-");
    }
    ```
- A. 1‐
- B. 1‐2‐
- C. 2‐
- D. 3‐
- E. 4‐
- F. None of the above

13. What is the output of the following application?
    ``` 
    package dinosaur;
    public class Park {
        public final static void main(String… arguments) {
            int pterodactyl = 8;
            long triceratops = 3;
            if(pterodactyl % 3> 1 + 1)
                triceratops++;
                triceratops--;
           System.out.print(triceratops);
       }
    }
    ``` 
- A. 2
- B. 3
- C. 4
- D. The code does not compile.
- E. The code compiles but throws an exception at runtime.

14. What variable type of red allows the following application to compile?
    ``` 
    package tornado;
    public class Kansas {
        public static void main(String[] args) {
            int colorOfRainbow = 10;
            ___________ red = 5;
            switch(colorOfRainbow) {
                default:
                    System.out.print("Home");
                    break;
                case red:
                    System.out.print("Away");
            }
       }
    }
    ``` 
- A. long
- B. double
- C. int
- D. var
- E. String
- F. None of the above

15. How many lines of the magic() method contain compilation errors?
    ``` 
    10: public void magic() {
    11:     do {
    12:         int trick = 0;
    13:         LOOP: do {
    14:             trick++;
    15:         } while (trick < 2--);
    16:         continue LOOP;
    17:     } while (1> 2);
    18:     System.out.println(trick);
    19: }
    ``` 
- A. Zero
- B. One
- C. Two
- D. Three
- E. Four