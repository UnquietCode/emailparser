/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eams.mbox2eml;

/**
 * A collection of HTTP header names.
 * 
 * @see <a href="http://rfc-ref.org/RFC-TEXTS/2616/">Hypertext Transfer Protocol
 *      -- HTTP/1.1 (RFC 2616)</a>
 */
public interface HttpHeaders {

	String AUTHOR = "Author";
	/** When was the document created? */
	Property CREATION_DATE = Property.internalDate("Creation-Date");

	String MESSAGE_RECIPIENT_ADDRESS = "Message-Recipient-Address";
	String MESSAGE_FROM = "Message-From";
	String MESSAGE_TO = "Message-To";
	String MESSAGE_CC = "Message-Cc";
	String MESSAGE_BCC = "Message-Bcc";

	String CONTENT_ENCODING = "Content-Encoding";
	String CONTENT_TYPE = "Content-Type";

	/**
	 * Typically, Format may include the media-type or dimensions of the
	 * resource. Format may be used to determine the software, hardware or other
	 * equipment needed to display or operate the resource. Examples of
	 * dimensions include size and duration. Recommended best practice is to
	 * select a value from a controlled vocabulary (for example, the list of
	 * Internet Media Types [MIME] defining computer media formats).
	 */
	String FORMAT = "format";

	/**
	 * Recommended best practice is to identify the resource by means of a
	 * string or number conforming to a formal identification system. Example
	 * formal identification systems include the Uniform Resource Identifier
	 * (URI) (including the Uniform Resource Locator (URL)), the Digital Object
	 * Identifier (DOI) and the International Standard Book Number (ISBN).
	 */
	String IDENTIFIER = "identifier";

	/**
	 * An entity primarily responsible for making the content of the resource.
	 * Examples of a Creator include a person, an organisation, or a service.
	 * Typically, the name of a Creator should be used to indicate the entity.
	 */
	String CREATOR = "creator";

	/**
	 * A date associated with an event in the life cycle of the resource.
	 * Typically, Date will be associated with the creation or availability of
	 * the resource. Recommended best practice for encoding the date value is
	 * defined in a profile of ISO 8601 [W3CDTF] and follows the YYYY-MM-DD
	 * format.
	 */
	Property DATE = Property.internalDate("date");

	/**
	 * A reference to a related resource. Recommended best practice is to
	 * reference the resource by means of a string or number conforming to a
	 * formal identification system.
	 */
	String RELATION = "relation";

	/**
	 * The topic of the content of the resource. Typically, a Subject will be
	 * expressed as keywords, key phrases or classification codes that describe
	 * a topic of the resource. Recommended best practice is to select a value
	 * from a controlled vocabulary or formal classification scheme.
	 */
	String SUBJECT = "subject";

	/**
	 * A name given to the resource. Typically, a Title will be a name by which
	 * the resource is formally known.
	 */
	String TITLE = "title";
}
