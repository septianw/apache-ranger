/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


package org.apache.ranger.authorization.solr.authorizer;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ranger.plugin.classloader.RangerPluginClassLoader;
import org.apache.solr.security.AuthorizationContext;
import org.apache.solr.security.AuthorizationPlugin;
import org.apache.solr.security.AuthorizationResponse;

public class RangerSolrAuthorizer implements AuthorizationPlugin {
	private static final Log LOG = LogFactory
			.getLog(RangerSolrAuthorizer.class);

	private static final String   RANGER_PLUGIN_TYPE                      = "solr";
	private static final String   RANGER_SOLR_AUTHORIZER_IMPL_CLASSNAME   = "org.apache.ranger.authorization.solr.authorizer.RangerSolrAuthorizer";

	private 		AuthorizationPlugin  	rangerSolrAuthorizerImpl 	  = null;
	private static	RangerPluginClassLoader rangerPluginClassLoader  	  = null;

	public RangerSolrAuthorizer() {
		if(LOG.isDebugEnabled()) {
			LOG.debug("==> RangerSolrAuthorizer.RangerSolrAuthorizer()");
		}

		this.init0();

		if(LOG.isDebugEnabled()) {
			LOG.debug("<== RangerSolrAuthorizer.RangerSolrAuthorizer()");
		}
	}

	private void init0(){
		if(LOG.isDebugEnabled()) {
			LOG.debug("==> RangerSolrAuthorizer.init0()");
		}

		try {
			rangerPluginClassLoader = RangerPluginClassLoader.getInstance(RANGER_PLUGIN_TYPE, this.getClass());

			@SuppressWarnings("unchecked")
			Class<AuthorizationPlugin> cls = (Class<AuthorizationPlugin>) Class.forName(RANGER_SOLR_AUTHORIZER_IMPL_CLASSNAME, true, rangerPluginClassLoader);

			activatePluginClassLoader();

			rangerSolrAuthorizerImpl = cls.newInstance();
		} catch (Exception e) {
			// check what need to be done
			LOG.error("Error Enabling RangerSolrPlugin", e);
		} finally {
			deactivatePluginClassLoader();
		}

		if(LOG.isDebugEnabled()) {
			LOG.debug("<== RangerSolrAuthorizer.init0()");
		}
	}


	@Override
	public void init(Map<String, Object> initInfo) {
		if(LOG.isDebugEnabled()) {
			LOG.debug("==> RangerSolrAuthorizer.init(Resource)");
		}
		try {
			activatePluginClassLoader();

			rangerSolrAuthorizerImpl.init(initInfo);
		} finally {
			deactivatePluginClassLoader();
		}

		if(LOG.isDebugEnabled()) {
			LOG.debug("<== RangerSolrAuthorizer.init(Resource)");
		}
	}

	@Override
	public void close() throws IOException {
		if(LOG.isDebugEnabled()) {
			LOG.debug("==> RangerSolrAuthorizer.close(Resource)");
		}
		try {
			activatePluginClassLoader();

			rangerSolrAuthorizerImpl.close();
		} finally {
			deactivatePluginClassLoader();
		}

		if(LOG.isDebugEnabled()) {
			LOG.debug("<== RangerSolrAuthorizer.close()");
		}

	}

	@Override
	public AuthorizationResponse authorize(AuthorizationContext context) {
		if(LOG.isDebugEnabled()) {
			LOG.debug("==> RangerSolrAuthorizer.init(context)");
		}
		AuthorizationResponse ret = null;
		try {
			activatePluginClassLoader();

			ret = rangerSolrAuthorizerImpl.authorize(context);
		} finally {
			deactivatePluginClassLoader();
		}

		if(LOG.isDebugEnabled()) {
			LOG.debug("<== RangerSolrAuthorizer.init(context)");
		}

		return ret;
	}

	private void activatePluginClassLoader() {
		if(rangerPluginClassLoader != null) {
			rangerPluginClassLoader.activate();
		}
	}

	private void deactivatePluginClassLoader() {
		if(rangerPluginClassLoader != null) {
			rangerPluginClassLoader.deactivate();
		}
	}

}