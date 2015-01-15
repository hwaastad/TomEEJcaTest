/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.waastad.fileenterprise.rar.api;

import javax.resource.cci.MessageListener;

/**
 *
 * @author Helge Waastad <helge.waastad@waastad.org>
 */
public interface FileMessageListener extends MessageListener {

    void onMessage(FileMessage message);

}
