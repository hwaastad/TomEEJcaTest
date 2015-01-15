/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.waastad.fileenterprise.rar;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.resource.ResourceException;
import javax.resource.cci.MessageListener;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.Connector;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.WorkManager;
import javax.transaction.xa.XAResource;
import org.waastad.fileenterprise.rar.inbound.FileWork;

/**
 *
 * @author Helge Waastad <helge.waastad@waastad.org>
 */
@Connector(
        displayName = "FileRA",
        vendorName = "Test Vendor",
        version = "1.0"
)
public class FileResourceAdapter implements ResourceAdapter, Serializable {

    private static final Logger logger = Logger.getLogger(FileResourceAdapter.class.getName());
    private static final long serialVersionUID = 1L;

    private WorkManager workManager;

    public FileResourceAdapter() {
        logger.info("[Start] FileResourceAdapter()");
    }

    @Override
    public void start(BootstrapContext bootstrapContext) throws ResourceAdapterInternalException {
        logger.info("[Start] start()");
        workManager = bootstrapContext.getWorkManager();
    }

    @Override
    public void stop() {
        logger.info("[stop] stop()");
    }

    @Override
    public void endpointActivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) throws ResourceException {
        logger.info("[start] endpointActivation");
        MessageEndpoint endPoint = messageEndpointFactory.createEndpoint(null);
        if (endPoint instanceof MessageListener) {
            FileWork fileWork = new FileWork(activationSpec, endPoint);
            workManager.scheduleWork(fileWork);
        }
    }

    @Override
    public void endpointDeactivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) {
        logger.info("[start] endpointDeactivation");
    }

    @Override
    public XAResource[] getXAResources(ActivationSpec[] activationSpecs) throws ResourceException {
        return new XAResource[0];
    }
}
