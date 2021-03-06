/* Copyright 2013 SpringSource.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License')
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.springsecurity.shiro

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.shiro.mgt.SecurityManager
import org.apache.shiro.realm.Realm
import org.apache.shiro.subject.SimplePrincipalCollection
import org.apache.shiro.subject.support.SubjectThreadState
import org.apache.shiro.web.session.HttpServletSession
import org.apache.shiro.web.subject.support.WebDelegatingSubject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class ShiroUtils {

	protected static Logger log = LoggerFactory.getLogger(this)

	protected ShiroUtils() {
		// static only
	}

	static void bindSubject(Authentication authentication, Realm realm, SecurityManager securityManager,
			HttpServletRequest request, HttpServletResponse response) {

		String host = request.remoteHost
		String username = authentication.principal.username

		WebDelegatingSubject subject = new WebDelegatingSubject(
				new SimplePrincipalCollection(username, realm.name), true, host,
				new HttpServletSession(request.getSession(), host),
				true, request, response, securityManager)

		if (log.debugEnabled) {
			log.debug 'Binding subject for principal {} from host {}', username, host
		}

		new SubjectThreadState(subject).bind()
	}
}
