/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.waastad.fileenterprise.rar.inbound;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.resource.ResourceException;
import javax.resource.spi.Activation;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.InvalidPropertyException;
import javax.resource.spi.ResourceAdapter;
import org.waastad.fileenterprise.rar.api.FileMessageListener;

/**
 *
 * @author Helge Waastad <helge.waastad@waastad.org>
 */
@Activation(
        messageListeners = {FileMessageListener.class}
)
public class FileActivationSpec implements Serializable, ActivationSpec {

    private static final Logger logger = Logger.getLogger(FileActivationSpec.class.getName());
    private static final long serialVersionUID = 1L;

    private ResourceAdapter resourceAdapter;
    private String filePath;
    private String fileExt;
    private int pollingInterval;

    public FileActivationSpec() {
        logger.info("[Start] FileActivationSpec");
    }

    @Override
    public void validate() throws InvalidPropertyException {
        logger.info("[start] validation");
    }

    @Override
    public ResourceAdapter getResourceAdapter() {
        return resourceAdapter;
    }

    @Override
    public void setResourceAdapter(ResourceAdapter resourceAdapter) throws ResourceException {
        this.resourceAdapter = resourceAdapter;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public int getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(int pollingInterval) {
        this.pollingInterval = pollingInterval;
    }
}
