#!/bin/bash

if [ $# -eq 0 ]; then
    echo "Usage: $0 <scala_file_name_without_extension>"
    echo "Example: $0 Rdd_test"
    exit 1
fi

SCALA_FILE=$1

if [ ! -f "src/main/scala/${SCALA_FILE}.scala" ]; then
    echo "Error: File src/main/scala/${SCALA_FILE}.scala does not exist."
    exit 1
fi

REBUILD=false
if [ "$2" == "--rebuild" ]; then
    REBUILD=true
fi

if [ "$REBUILD" = true ]; then
    echo "Full project rebuild..."
    mvn clean package
else
    echo "Incremental project compilation..."
    mvn compile
fi

if [ $? -ne 0 ]; then
    echo "Compilation error."
    exit 1
fi

echo "Updating manifest file to set main class..."
sed -i "s/<mainClass>.*<\/mainClass>/<mainClass>${SCALA_FILE}<\/mainClass>/g" pom.xml

echo "Packaging project..."
mvn package -DskipTests

if [ $? -ne 0 ]; then
    echo "Packaging error."
    exit 1
fi

echo "Running ${SCALA_FILE}..."
java -cp target/spark-1.0-SNAPSHOT-jar-with-dependencies.jar ${SCALA_FILE}

echo "Execution completed."