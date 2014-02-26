/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package se.vgregion.messagebus;

import com.liferay.portal.kernel.dao.orm.*;
import com.liferay.portal.kernel.executor.PortalExecutorManager;
import com.liferay.portal.kernel.executor.PortalExecutorManagerUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageBusException;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.sender.SynchronousMessageSender;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Bruno Farache
 */
@ContextConfiguration(locations={"/META-INF/messageBusTest.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class MessageBusTest {

    @Test
    public void testSynchronousMessage() throws MessageBusException {   	
    	messageBus.registerMessageListener(
    		"liferay/message_bus/test", new PingPongMessageListener());

    	Message message = new Message();
    	message.setPayload("ping");

    	String actual = (String)synchronousMessageSender.send(
    		"liferay/message_bus/test", message);

        assertEquals("pong", actual);
    }
    
    class PingPongMessageListener implements MessageListener {

    	public void receive(Message message) {
			message.setPayload("pong");

			messageBus.sendMessage(
				message.getResponseDestinationName(), message);
		}

    }

    @Autowired
    private MessageBus messageBus;
    
    @Autowired
    private SynchronousMessageSender synchronousMessageSender;

    @BeforeClass
    public static void init() {
        new PortalExecutorManagerUtil().setPortalExecutorManager(new PortalExecutorManager() {
            @Override
            public <T> Future<T> execute(String name, Callable<T> callable) {
                return null;
            }

            @Override
            public <T> T execute(String name, Callable<T> callable, long timeout, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return null;
            }

            @Override
            public com.liferay.portal.kernel.concurrent.ThreadPoolExecutor getPortalExecutor(String name) {
                return null;
            }

            @Override
            public com.liferay.portal.kernel.concurrent.ThreadPoolExecutor getPortalExecutor(String name, boolean createIfAbsent) {
                return null;
            }

            @Override
            public com.liferay.portal.kernel.concurrent.ThreadPoolExecutor registerPortalExecutor(String name, com.liferay.portal.kernel.concurrent.ThreadPoolExecutor threadPoolExecutor) {
                return null;
            }

            @Override
            public void shutdown() {

            }

            @Override
            public void shutdown(boolean interrupt) {

            }

            @Override
            public void shutdown(String name) {

            }

            @Override
            public void shutdown(String name, boolean interrupt) {

            }
        });

        new EntityCacheUtil().setEntityCache(new EntityCache() {
            @Override
            public void clearCache() {

            }

            @Override
            public void clearCache(String className) {

            }

            @Override
            public void clearLocalCache() {

            }

            @Override
            public Serializable getResult(boolean entityCacheEnabled, Class<?> clazz, Serializable primaryKey) {
                return null;
            }

            @Override
            public void invalidate() {

            }

            @Override
            public Serializable loadResult(boolean entityCacheEnabled, Class<?> clazz, Serializable primaryKey, SessionFactory sessionFactory) {
                return null;
            }

            @Override
            public void putResult(boolean entityCacheEnabled, Class<?> clazz, Serializable primaryKey, Serializable result) {

            }

            @Override
            public void removeCache(String className) {

            }

            @Override
            public void removeResult(boolean entityCacheEnabled, Class<?> clazz, Serializable primaryKey) {

            }
        });

        new FinderCacheUtil().setFinderCache(new FinderCache() {
            @Override
            public void clearCache() {

            }

            @Override
            public void clearCache(String className) {

            }

            @Override
            public void clearLocalCache() {

            }

            @Override
            public Object getResult(FinderPath finderPath, Object[] args, SessionFactory sessionFactory) {
                return null;
            }

            @Override
            public void invalidate() {

            }

            @Override
            public void putResult(FinderPath finderPath, Object[] args, Object result) {

            }

            @Override
            public void removeCache(String className) {

            }

            @Override
            public void removeResult(FinderPath finderPath, Object[] args) {

            }
        });
    }

}