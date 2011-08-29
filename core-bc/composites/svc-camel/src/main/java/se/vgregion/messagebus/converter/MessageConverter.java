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

package se.vgregion.messagebus.converter;

import com.liferay.portal.kernel.messaging.Message;

import org.apache.camel.Converter;

/**
 * Class for converting to {@link Message}. Used by Camel.
 * <p/>
 * @author Bruno Farache
 */
@Converter
public final class MessageConverter {

    private MessageConverter() {

    }

    /**
     * Converts a <code>String</code> to a {@link Message}.
     *
     * @param string string
     * @return A <code>Message</code>.
     */
    @Converter
    public static Message toMessage(String string) {
        Message message = new Message();

        message.setPayload(string);

        return message;
    }

    /**
     * Converts an <code>Exception</code> to a {@link Message}.
     *
     * @param ex ex
     * @return <code>Message</code>
     */
    @Converter
    public static Message toMessage(Exception ex) {
        Message message = new Message();

        message.setPayload(ex);

        return message;
    }

}