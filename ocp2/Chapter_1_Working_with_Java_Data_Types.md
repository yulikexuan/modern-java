# Chapter 1 Working with Java Data Types


1. ### Which of the following are not valid variable names? (Choose two)
- A. _
- B. _blue
- C. 2blue
- D. blue$
- E. Blue

2. ### What is the value of ``` tip ``` after executing the following code snippet? 
    ```
    int meal = 5;
    int tip = 2;
    var total = meal + (meal > 6 ? tip++ : tip--);
    ```
- A. 1
- B. 2
- C. 3
- D. 7
- E. None of the above

3. ### Which is equivalent to ``` var q = 4.0f; ```?
- A. float q = 4.0f;
- B. Float q = 4.0f;
- C. double q = 4.0f;
- D. Double q = 4.0f;
- E. Object q = 4.0f;

4. ### What is the output of the following?
    ```
    var b = "12";
    b += "3";
    b.reverse();
    System.out.println(b.toString());
    ```
- A. 12
- B. 123
- C. 321
- D. The code does not compile 

5. ### What is the output of the following?
    ```
    var line = new StringBuilder("-");
    var anotherLine = line.append("-");
    System.out.print(line == anotherLine);
    System.out.print(" ");
    System.out.print(line.length());
    ```
- A. false 1
- B. false 2
- C. true 1
- D. true 2
- E. It does not compile

6. ### Given the following Venn diagram and the boolean variables
   #### ``` apples ```, ``` oranges ```, and ``` bananas ```
   #### which expression most closely represents the filled‐in region of the diagram?

   ![venn diagram](images/chap_01_06.png)
- A. apples && oranges && !bananas
- B. orange || (oranges && !bananas)
- C. (apples || bananas) && oranges
- D. oranges && apples
- E. (apples || oranges) && !bananas
- F. apples ^ oranges

7. ### What is the output of the following?

    ``` 
    var line = new String("-");
    var anotherLine = line.concat("-");
    System.out.print(line == anotherLine);
    System.out.print(" ");
    System.out.print(line.length());
    ```
- A. false 1
- B. false 2
- C. true 1
- D. true 2
- E. Does not compile

8. ### Which can fill in the blank? (Choose two)
    ```
    public void math() {
        _____ pi = 3.14;
    }
    ```
- A. byte
- B. double
- C. float
- D. short
- E. var

9. ### Fill in the blanks: 
   #### The operators !=, _______, _______, _______, and ++ are listed in 
   #### the same or increasing level of operator precedence. (Choose two)

- A. ==, *, !
- B. /, %, *
- C. *, ‐‐, /
- D. !, *, %
- E. +=, &&, *
- F. *, <, /

10. ### How many of these compile?

    ``` 
    Comparator<String> c1 = (j, k) -> 0; // 18
    Comparator<String> c2 = (String j, String k) -> 0; // 19
    Comparator<String> c3 = (var j, String k) -> 0; // 20
    Comparator<String> c4 = (var j, k) -> 0; // 21
    Comparator<String> c5 = (var j, var k) -> 0; // 22
    ```

- A. 0
- B. 1
- C. 2
- D. 3
- E. 4
- F. 5

11. ### The author of this method forgot to include the data type 
    #### Which of the following reference types can best fill in the blank to complete this method?

    ``` 
    public static void secret(___________ mystery) {
        char ch = mystery.charAt(3);
        mystery = mystery.insert(1, "more");
        int num = mystery.length();
    }
    ```
- A. String
- B. StringBuilder
- C. Both
- D. Neither

12. ### What is the output of the following?

    ``` 
    var teams = new StringBuilder("333");
    teams.append(" 806");
    teams.append(" 1601");
    System.out.print(teams);
    ```
- A. 333
- B. 333 806 1601
- C. The code compiles but outputs something else.
- D. The code does not compile.

13. ### Which of the following declarations does not compile?
- A. double num1, int num2 = 0;
- B. int num1, num2;
- C. int num1, num2 = 0;
- D. int num1 = 0, num2 = 0;
- E. All of the above
- F. None of the above

14. ### Given the file Magnet.java shown 
    #### which of the marked lines can you independently insert the line ```var color; ``` into and still have the code compile?

    ``` 
    // line a1
    public class Magnet {
        // line a2
        public void attach() {
            // line a3
        }
        // line a4
    }
    ```
- A. a2
- B. a3
- C. a2 and a3
- D. a1, a2, a3, and a4
- E. None of the above

15. ### Which is one of the lines output by this code?

    ```
    var list = new ArrayList<Integer>(); // 10
    list.add(10); // 11
    list.add(9); // 12
    list.add(8); // 13
    // 14
    var num = 9; // 15
    list.removeIf(x -> {int keep = num; return x != keep;}); // 16
    System.out.println(list); // 17
    // 18
    list.removeIf(x -> {int keep = num; return x == keep;}); // 19
    System.out.println(list); // 20
    ```
- A. []
- B. [8, 10]
- C. [8, 9, 10]
- D. [10, 8]
- E. The code does not compile.
