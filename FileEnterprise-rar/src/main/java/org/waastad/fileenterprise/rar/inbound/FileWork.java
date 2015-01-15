/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.waastad.fileenterprise.rar.inbound;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.work.Work;
import org.waastad.fileenterprise.rar.api.FileMessage;
import org.waastad.fileenterprise.rar.api.FileMessageBean;
import org.waastad.fileenterprise.rar.api.FileMessageListener;

/**
 *
 * @author Helge Waastad <helge.waastad@waastad.org>
 */
public class FileWork implements Work {

    private static final Logger logger = Logger.getLogger(FileWork.class.getName());

    private final FileActivationSpec activationSpec;
//private MessageListener messageListener;
    private final FileMessageListener messageListener;
    private boolean released;

    public FileWork(ActivationSpec activationSpec, MessageEndpoint messageEndpoint) {
        logger.info("[start] FileWork()");
        this.activationSpec = (FileActivationSpec) activationSpec;
        this.messageListener = (FileMessageListener) messageEndpoint;
        this.released = false;
    }

    @Override
    public void release() {
        this.released = true;
    }

    public boolean isReleased() {
        return released;
    }

    @Override
    public void run() {
        logger.info("[start] run()");
        int pollingInterval = activationSpec.getPollingInterval();
        //set polling interval in ms
        if (pollingInterval < 5) {
            pollingInterval = 5000;
        } else {
            pollingInterval *= 1000;
        }
        logger.log(Level.INFO, "[Set Polling interval in ms:]{0}", pollingInterval);
        while (!isReleased()) {
//start read file from directory
            try {
                logger.info("[Start reading from folder]");
                File folder = new File(activationSpec.getFilePath());
                if (folder.exists() && folder.isDirectory()) {
                    File[] files = folder.listFiles();
// process file
                    for (File file : files) {
                        FileMessageBean fBean = new FileMessageBean();
                        fBean.setRecordName("Jca Standard file record");
                        fBean.setFileName("[fileName]:" + file.getName());
                        fBean.setRecordShortDescription("JCA file record which wrappes the file data");
                        FileMessage message = new FileMessage();
                        message.setFileMesssageBean(fBean);
                        logger.info("Read file data to wrap");
                        FileInputStream fis = null;
                        try {
                            fis = new FileInputStream(file);
                            BufferedReader bf = new BufferedReader(new InputStreamReader(fis));
                            String data;
                            StringBuilder sbuffer = new StringBuilder();
                            while ((data = bf.readLine()) != null) {
                                sbuffer.append(data);
                            }
                            fBean.setData(sbuffer.toString());
                            messageListener.onMessage(message);
// sleep
                            Thread.sleep(pollingInterval);
                        } catch (FileNotFoundException e) {
                            logger.log(Level.SEVERE, e.getMessage(), e);
                            throw new RuntimeException("FileNotFoundException:" + e.getMessage());
                        } catch (IOException e) {
                            logger.log(Level.SEVERE, e.getMessage(), e);
                            throw new RuntimeException("Error on reading file:" + e.getMessage());
                        } finally {
                            try {
                                fis.close();
                            } catch (IOException e) {
                                logger.log(Level.SEVERE, e.getMessage(), e);
                                throw new RuntimeException("Error on reading file:" + e.getMessage());
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                logger.log(Level.INFO, "[Eroor on Run]{0}", e.getMessage());
                throw new RuntimeException("error in Run" + e.getMessage());
            }
        }
    }

}
