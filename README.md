# monkey - Programming Language implementation in Java

This is a Java implementation of Thorsten Ball's Monkey Programming Language from his great book 
[Writing An Interpreter in Go](https://interpreterbook.com/).

## Features

* Tokenizer
* Lexer
* Parser
* AST
* Builtin Functions
* if/else
* Arrays & Maps
* Function Literals
* Closures
* REPL ([Read Eval Print Loop](Read Eval Print Loop))

## Examples

### Precedence and Operators

```
5 * 5 + 10;
3 + 4 * 5 == 3 * 1 + 4 * 5;
5 * 10 > 40 + 5;
((10 + 2) * 30) == (300 + 20 * 3);
(10 + 2) * 30 == 300 + 20 * 3;
500 / 2 != 250;

```

### Bool type

```js
puts(true == true);
puts(false == true);
```

### if
```
if (5 * 5 + 10 > 34) { puts(99) } else { puts(100) }
```

```
let a = 5;
let b = a > 3;
let c = a * 99;
if (b) { 10 } else { 1 };
let d = if (c > a) { 99 } else { 100 };
d * c * a;
```

### String type
You can also see how function literals can be used.
```
let hello = "Hello there, fellow Monkey users and fans!"
let giveMeHello = fn() { "Hello!" }
giveMeHello();

```

### Arrays

```
let myArray = ["one", "two", "three"];
len(myArray);
first(myArray);
rest(myArray);
last(myArray);
push(myArray, "four")
```

## Hash

```
let myHash = {"name": "Jimmy", "age": 72, "band": "Led Zeppelin"};
myHash["name"];
myHash["age"];
myHash["band"];
```

### Functions

```js
let two = "two";
let h = { "one": 10 - 9, two: 1 + 1, "thr" + "ee": 6 / 2, 4: 4, true: 5, false: 6};
h["one"];
```

```js
let addTwo = fn(x) { x + 2; };
addTwo(2);
let multiply = fn(x, y) { x * y };
multiply(50 / 2, 1 * 2);

fn(x) { x == 10 }(5);
false;
fn(x) { x == 10 }(10);
true;

```

## Requirements

- [Java](https://openjdk.java.net/projects/jdk/16/), this project uses the JDK 16.
- Maven.

## Build

To build the .jar file, just type:
```bash
$ make
```
or you could just run:
```bash
$ mvn clean package
```

## Run

To run the REPL (Read Evaluate Print Loop):

```bash
make run
```

or:
```bash
java --enable-preview -jar target/monkeyj-jar-with-dependencies.jar
```

## Running the examples

There are a few code examples of the language in the [examples](./tree/main/examples) directory. To run them, you
can use the following command:

```bash
$ java --enable-preview -jar target/monkeyj-jar-with-dependencies.jar < examples/strings/concat1.mnk
```

## Donation / Sponsorship ❤️ 👍

This code was brought to you by [Leo Gutiérrez](https://github.com/leogtzr) in his free time. If you want to thank me and support the development of this project, please make a small donation on [PayPal](https://www.paypal.me/leogtzr). In case you also like my other open source contributions and articles, please consider motivating me by becoming a sponsor/patron on [Patreon](https://www.patreon.com/leogtzr). Thank you! ❤️

