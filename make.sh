#!/bin/bash

clear
echo "..."

if [  -f "./server/src/com/cmgc/keys/V$1.java" ];then
    echo "error: key already exist!!!"
    exit 1
    
fi

echo "clean environment"
    rm _temp -rf
echo "starting generate ..."
    mkdir _temp
    cd _temp
    cp ../RSAGenerator.jar .
    java -jar RSAGenerator.jar -v$1

echo "starting deploy data file"
    mv data.h ../android_client/cpp/NumPro/jni/ -f
    mv V$1.java ../server/src/com/cmgc/keys/ -f

echo "starting compile..."
    cd ../server/
    ant pack
    cd ../android_client/cpp/NumPro/jni/
    ndk-build
    cd ../../../java/
    ant pack

echo "copy jar and binary..."
    cd ../../_temp
    mkdir for_client
    cp ../android_client/cpp/NumPro/libs ./for_client -r
    cp ../android_client/java/ant/dist/numpro_client.jar ./for_client

    mkdir for_server
    cp ../server/ant/dist/numpro_server.jar ./for_server
    
    tar cjvf  client.tar.gz for_client/*
    tar cjvf  server.tar.gz for_server/*
    tar cvf $1.tar *.tar.gz
    cp $1.tar ../   
    cd ..
    
echo "cleaning..."
    rm ./server/ant -rf
    rm ./android_client/cpp/NumPro/libs -rf
    rm ./android_client/cpp/NumPro/obj -rf
    rm ./android_client/java/ant -rf
    
rm _temp -rf
