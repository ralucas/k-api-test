#!/bin/bash

data_file=$1
usage() {
    cat << EOF
    usage: $0 options
    Runs employees api
    OPTIONS:
    -h      Show this message
    -f      Data file to ingest (ex run.sh -f <path-to-file>)
    -g      Auto generate given # of employees (ex. run.sh -g 100)
EOF
}

while getopts “hyf:e:r:v:” OPTION
do
     case $OPTION in
         h)
             usage
             exit 1
             ;;
         f)
             datafile=$OPTARG
             ;;
         g)
             generatedata=$OPTARG
             ;;
         ?)
             usage
             exit
             ;;
     esac
done

server_opts=""
if [ ! -z $datafile ]; then
  server_opts+="--data-file=${datafile}"
fi

if [ ! -z $generatedata ]; then
  server_opts+="--generate-data=${generatedata}"
fi

docker build -t employees-api:latest .
docker run -p "8080:8080" -e SERVER_OPTIONS=${server_opts} employees-api