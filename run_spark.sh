#!/bin/bash

if [ $# -eq 0 ]; then
    echo "Usage: $0 <scala_file_name_without_extension> [options]"
    echo "Options:"
    echo "  --rebuild    Force a full project rebuild"
    echo "  --debug      Run Maven with -e option for more error details"
    echo "  --verbose    Run Maven with -X option for full debug logs"
    echo "Example: $0 Rdd_test --debug"
    exit 1
fi

SCALA_FILE=$1
shift

REBUILD=false
DEBUG=""

while [ "$#" -gt 0 ]; do
    case "$1" in
        --rebuild)
            REBUILD=true
            ;;
        --debug)
            DEBUG="-e"
            ;;
        --verbose)
            DEBUG="-X"
            ;;
    esac
    shift
done

if [ ! -f "src/main/scala/${SCALA_FILE}.scala" ]; then
    echo "Error: File src/main/scala/${SCALA_FILE}.scala does not exist."
    exit 1
fi

if [ "$REBUILD" = true ]; then
    echo "Full project rebuild..."
    mvn clean package $DEBUG
else
    echo "Incremental project compilation..."
    mvn compile $DEBUG
fi

if [ $? -ne 0 ]; then
    echo "Compilation error."
    echo "For more information, rerun with --debug or --verbose option:"
    echo "$0 $SCALA_FILE --debug"
    exit 1
fi

echo "Updating manifest file to set main class..."
sed -i "s/<mainClass>.*<\/mainClass>/<mainClass>${SCALA_FILE}<\/mainClass>/g" pom.xml

echo "Packaging project..."
mvn package -DskipTests $DEBUG

if [ $? -ne 0 ]; then
    echo "Packaging error."
    echo "For more information, rerun with --debug or --verbose option:"
    echo "$0 $SCALA_FILE --debug"
    exit 1
fi

echo "Running ${SCALA_FILE}..."
java --add-opens=java.base/java.util=ALL-UNNAMED \
     --add-opens=java.base/java.util.concurrent=ALL-UNNAMED \
     --add-opens=java.base/java.lang=ALL-UNNAMED \
     --add-opens=java.base/java.net=ALL-UNNAMED \
     --add-opens=java.base/java.nio=ALL-UNNAMED \
     --add-opens=java.base/java.lang.invoke=ALL-UNNAMED \
     -cp target/spark-1.0-SNAPSHOT-jar-with-dependencies.jar ${SCALA_FILE}

echo "Execution completed."