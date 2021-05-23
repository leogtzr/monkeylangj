.DEFAULT_GOAL := build

build:
	mvn clean package

run:
	java --enable-preview -jar target/monkeyj-jar-with-dependencies.jar

clean:
	mvn clean

test:
	mvn --threads 8C test

repl:
	java --enable-preview -jar target/monkeyj-jar-with-dependencies.jar
