/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package corpus.sinhala.crawler.blog.controller;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CacheManager {

    final static Logger logger = Logger.getLogger(CacheManager.class);
    private static CacheManager instance=null;

    private CacheManager(){

        init();
    }

    private void init() {
        deserializeCache();
        deserializeFileID();
    }

    public static CacheManager getInstance(){
        if(instance==null){
            instance=new CacheManager();
        }
        return instance;
    }

    public Map<String, Set<String>> postCache=null;



    public synchronized void serializeCache(){
        try
        {

            FileOutputStream fileOut = new FileOutputStream("cache.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(postCache);
            out.close();
            fileOut.close();

        }catch(IOException e)
        {
            logger.error(e);
        }
    }

    public synchronized void serializeFileId(Integer fileId){
        try
        {

            FileOutputStream fileOut = new FileOutputStream("fileID.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(fileId);
            out.close();
            fileOut.close();
            //System.out.printf("Serialized data is saved in cache.ser");
        }catch(IOException e)
        {
            logger.error(e);
        }
    }

    public synchronized void deserializeCache(){
        try
        {
            FileInputStream fileIn = new FileInputStream("cache.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            postCache = (Map) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }catch(ClassNotFoundException e)
        {
            logger.error(e);

        }
        if(postCache==null){
            postCache = new HashMap<>();
        }
    }

    public synchronized Integer deserializeFileID(){
        Integer fileId =null;
        try
        {
            FileInputStream fileIn = new FileInputStream("fileID.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            fileId = (Integer) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException i)
        {
            i.printStackTrace();
        }catch(ClassNotFoundException e)
        {
            logger.error(e);
        }
        if(fileId==null){
            fileId = 0;
        }
        return fileId;
    }


}
