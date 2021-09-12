# Java Modules

## The Three Tenets of Modularity

> tenet n. /ˈtenɪt/ 
> one of the principles or beliefs that a theory or larger set of beliefs is 
> based on 宗旨

1.  String Encapsulation: Hide your internals, be strict about what is puboic API


2.  Well-Defined Interfaces: When modules interact, use stable and well-defined
    interfaces


3.  Explicit Dependencies: A module lists what it needs form other modules


## Creating a Module in Java

### The Module File: ``` module-info.java ```

- Where does this module-info.java live
    - The root of the source tree that comprises the module we're describing
    - Completely outside of hte package hierarchy
    - Other source files inside the module containing classed or interfaces must 
      always be part of a package
    - You cannot have classes in the default package, without a package 
      declaration at the top, inside of a module
    - By default, a module strongly encapsulates every package that is inside of 
      it


### The Module Name

- Separate namespace

- Module Naming Convention
    - One or more Java identifiers separated by '.'
    - Module names live in a completely separate namespace from any other 
      identifiers and names in Java
        - This means there will never be a class between clash names and module 
          names or package names and module names because these all inhabit
          their own namespace 
    - A module name can be a simple, valid Java identifier
    - a module name can consist of multiple java identifiers separated by a dot, 
      much  like a package name 
    - Package names and module names live in completely separate namespace so 
      they can overlap if you want to


- Good practices for nameing modules
    - Avoid terminal digits in module names
    - Root package as module name
        - Choose the module name to be the root package or the longest common 
          prefix of all the packages that are inside of the module, for example, 
          ``` com.pluralsight ```


### Compile ``` module-info.java ``` to ``` module-info.class ```

- This means that any information that we define for our module carries over 
  from the source code, so the compile time environment, to the runtime 
  environment
    - It means that the JVM wil also have knowledge of our module and the 
      name of the module and possibly more info that we will define later 
      inside of the module declaration 


### Package the compilation output into a JAR file

- Youi can give the JAR file any namne you want, because in the end what matters 
  is the name for the module that we defined inside of the module declaration


- A JAR file that contains a top-level module-info.class is called a modular JAR 
  file


- A modular JAR file behaves differently than a regular JAR file


## Running Module in Java

``` 
java -p target/classes -m com.yulikexuan.modularity/com.yulikexuan.modularity.greeter.Main
```

